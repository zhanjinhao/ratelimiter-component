package cn.addenda.component.ratelimiter.helper;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/8/26 23:05
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {

  /**
   * {@link RateLimitationAttr#getRateLimiterAllocator()}
   */
  String rateLimiterAllocator();

  /**
   * {@link RateLimitationAttr#getPrefix()}
   */
  String prefix() default RateLimitationAttr.DEFAULT_PREFIX;

  /**
   * {@link RateLimitationAttr#getSpEL()}
   */
  String spEL() default "";

  /**
   * {@link RateLimitationAttr#getRateLimitedMsg()}
   */
  String rateLimitedMsg() default RateLimitationAttr.DEFAULT_RATE_LIMITED_MSG;

}
