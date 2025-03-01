package cn.addenda.component.ratelimiter.helper;

import cn.addenda.component.base.exception.ExceptionUtils;
import cn.addenda.component.ratelimiter.RateLimiter;
import cn.addenda.component.ratelimiter.allocator.RateLimiterAllocator;
import cn.addenda.component.spring.util.AnnotationUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.List;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
public class RateLimitationInterceptor extends RateLimitationSupport implements MethodInterceptor {

  public RateLimitationInterceptor(List<? extends RateLimiterAllocator<? extends RateLimiter>> rateLimiterList) {
    super(rateLimiterList);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    RateLimited annotation = AnnotationUtils.extractAnnotation(invocation, RateLimited.class);

    if (annotation == null) {
      return invocation.proceed();
    }

    RateLimitationAttr attr = RateLimitationAttr.builder()
            .prefix(annotation.prefix())
            .spEL(annotation.spEL())
            .rateLimiterAllocator(annotation.rateLimiterAllocator())
            .rateLimitedMsg(annotation.rateLimitedMsg())
            .build();

    try {
      return invokeWithinRateLimitation(attr, invocation.getArguments(), invocation::proceed, invocation.getMethod());
    } catch (Throwable throwable) {
      throw ExceptionUtils.unwrapThrowable(throwable);
    }
  }

}
