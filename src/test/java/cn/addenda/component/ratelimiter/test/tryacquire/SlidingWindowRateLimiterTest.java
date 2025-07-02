package cn.addenda.component.ratelimiter.test.tryacquire;

import cn.addenda.component.ratelimiter.SlidingWindowRateLimiter;

/**
 * @author addenda
 * @since 2022/12/28 14:14
 */
public class SlidingWindowRateLimiterTest {

  public static void main(String[] args) {
    SlidingWindowRateLimiter slidingWindowRateLimiter = new SlidingWindowRateLimiter(10, 1000, 100, true);
    new RateLimiterBaseTest(slidingWindowRateLimiter).test(true);
  }

}
