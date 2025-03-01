package cn.addenda.component.ratelimiter.helper;

import cn.addenda.component.base.exception.ComponentServiceException;
import cn.addenda.component.base.jackson.util.JacksonUtils;
import cn.addenda.component.base.lambda.TSupplier;
import cn.addenda.component.base.string.Slf4jUtils;
import cn.addenda.component.ratelimiter.RateLimiter;
import cn.addenda.component.ratelimiter.allocator.RateLimiterAllocator;
import cn.addenda.component.spring.context.ValueResolverHelper;
import cn.addenda.component.spring.util.SpELUtils;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2023/8/26 23:04
 */
public class RateLimitationSupport
        implements ApplicationContextAware, EnvironmentAware, InitializingBean {

  /**
   * {@link EnableRateLimitation#namespace()}
   */
  private String namespace;

  @Setter
  protected String spELArgsName = "spELArgs";

  private Environment environment;

  private ApplicationContext applicationContext;

  private final Map<String, RateLimiterAllocator<? extends RateLimiter>> map;

  public RateLimitationSupport(List<? extends RateLimiterAllocator<? extends RateLimiter>> rateLimiterList) {
    map = rateLimiterList.stream()
            .collect(Collectors.toMap(RateLimiterAllocator::getName, a -> a));
  }

  protected Object invokeWithinRateLimitation(
          RateLimitationAttr attr, Object[] arguments,
          TSupplier<Object> supplier, Method method) throws Throwable {
    String spEL = attr.getSpEL();
    String rawKey = SpELUtils.getKey(spEL, method, this.spELArgsName, arguments);

    if (rawKey == null || rawKey.isEmpty()) {
      String msg = String.format("The rawKey can not be null or \"\". arguments: [%s], spEL: [%s].",
              JacksonUtils.toStr(arguments), spEL);
      throw new RateLimitationHelperException(msg);
    }

    String rateLimiterAllocator = attr.getRateLimiterAllocator();
    RateLimiterAllocator<? extends RateLimiter> allocator = this.map.get(rateLimiterAllocator);
    if (allocator == null) {
      throw new RateLimitationHelperException(Slf4jUtils.format("Cannot get rateLimiterAllocator:[{}].", rateLimiterAllocator));
    }
    String key = attr.getPrefix() + ":" + rawKey;
    RateLimiter rateLimiter = allocator.allocate(key);

    try {
      if (rateLimiter.tryAcquire()) {
        return supplier.get();
      }

      Properties properties = new Properties();
      properties.put("prefix", attr.getPrefix());
      if (StringUtils.hasLength(spEL)) {
        properties.put("spEL", attr.getSpEL());
      }
      properties.put("rawKey", rawKey);
      properties.put("simpleKey", attr.getPrefix() + ":" + rawKey);
      properties.put("key", key);

      String rateLimitedMsg = attr.getRateLimitedMsg();
      String msg = ValueResolverHelper.resolveHashPlaceholder(rateLimitedMsg, properties);
      throw new ComponentServiceException(msg);
    } finally {
      allocator.delayRelease(key);
    }
  }

  protected void setNamespace(String namespace) {
    this.namespace = namespace;
    setNamespaceOfRateLimiter();
  }

  private void setNamespaceOfRateLimiter() {
    if (namespace != null) {
      this.map.values().forEach(a -> a.setNamespace(namespace));
    }
  }

  private String resolve(String value) {
    if (StringUtils.hasText(value)) {
      return this.environment.resolvePlaceholders(value);
    }
    return value;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.namespace = resolve(namespace);
    setNamespaceOfRateLimiter();
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
