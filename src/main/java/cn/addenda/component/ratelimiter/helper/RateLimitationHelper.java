package cn.addenda.component.ratelimiter.helper;

import cn.addenda.component.base.exception.ExceptionUtils;
import cn.addenda.component.base.lambda.FunctionConverter;
import cn.addenda.component.base.lambda.TRunnable;
import cn.addenda.component.base.lambda.TSupplier;
import cn.addenda.component.ratelimiter.RateLimiter;
import cn.addenda.component.ratelimiter.allocator.RateLimiterAllocator;

import java.util.List;

/**
 * @author addenda
 * @since 2023/9/28 20:28
 */
public class RateLimitationHelper extends RateLimitationSupport {

  public RateLimitationHelper(String namespace, List<? extends RateLimiterAllocator<? extends RateLimiter>> rateLimiterList) {
    super(rateLimiterList);
    this.setNamespace(namespace);
  }

  /**
   * 最简单的限流场景，arguments[0] 是 key
   */
  public <R> R rateLimit(String rateLimiterAllocator, TSupplier<R> supplier, Object... arguments) {
    RateLimitationAttr attr = RateLimitationAttr.builder()
            .rateLimiterAllocator(rateLimiterAllocator)
            .build();
    return rateLimit(attr, supplier, arguments);
  }

  public void rateLimit(String rateLimiterAllocator, TRunnable runnable, Object... arguments) {
    RateLimitationAttr attr = RateLimitationAttr.builder()
            .rateLimiterAllocator(rateLimiterAllocator)
            .build();
    rateLimit(attr, runnable, arguments);
  }

  /**
   * 较上一个场景，arguments[0] 是 key，prefix可以指定
   */
  public <R> R rateLimit(String rateLimiterAllocator, String prefix, TSupplier<R> supplier, Object... arguments) {
    RateLimitationAttr attr = RateLimitationAttr.builder()
            .rateLimiterAllocator(rateLimiterAllocator)
            .prefix(prefix).build();
    return rateLimit(attr, supplier, arguments);
  }

  public void rateLimit(String rateLimiterAllocator, String prefix, TRunnable runnable, Object... arguments) {
    RateLimitationAttr attr = RateLimitationAttr.builder()
            .rateLimiterAllocator(rateLimiterAllocator)
            .prefix(prefix).build();
    rateLimit(attr, runnable, arguments);
  }

  /**
   * 较上一个场景，arguments[0] 是 key，prefix可以指定，spEL可以指定
   */
  public <R> R rateLimit(String rateLimiterAllocator, String prefix, String spEL, TSupplier<R> supplier, Object... arguments) {
    RateLimitationAttr attr = RateLimitationAttr.builder()
            .rateLimiterAllocator(rateLimiterAllocator)
            .prefix(prefix).spEL(spEL).build();
    return rateLimit(attr, supplier, arguments);
  }

  public void rateLimit(String rateLimiterAllocator, String prefix, String spEL, TRunnable runnable, Object... arguments) {
    RateLimitationAttr attr = RateLimitationAttr.builder()
            .rateLimiterAllocator(rateLimiterAllocator)
            .prefix(prefix).spEL(spEL).build();
    rateLimit(attr, runnable, arguments);
  }

  public void rateLimit(RateLimitationAttr attr, TRunnable runnable, Object... arguments) {
    rateLimit(attr, FunctionConverter.toTSupplier(runnable), arguments);
  }

  public <R> R rateLimit(RateLimitationAttr attr, TSupplier<R> supplier, Object... arguments) {
    if (arguments == null || arguments.length == 0 || attr == null || supplier == null) {
      throw new RateLimitationHelperException("参数不能为空！");
    }
    try {
      return (R) invokeWithinRateLimitation(attr, arguments, supplier::get, null);
    } catch (Throwable throwable) {
      throw ExceptionUtils.wrapAsRuntimeException(throwable, RateLimitationHelperException.class);
    }
  }

}
