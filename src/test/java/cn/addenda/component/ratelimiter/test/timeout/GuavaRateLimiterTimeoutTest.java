package cn.addenda.component.ratelimiter.test.timeout;

import cn.addenda.component.ratelimiter.GuavaRateLimiterWrapper;

import java.time.Duration;

/**
 * @author addenda
 * @since 2022/12/29 19:14
 */
public class GuavaRateLimiterTimeoutTest {

  public static void main(String[] args) {
    GuavaRateLimiterWrapper guavaRateLimiterWrapper = new GuavaRateLimiterWrapper(10, Duration.ofSeconds(1));
    new RateLimiterTimeoutBaseTest(guavaRateLimiterWrapper).test(true);
  }

}
