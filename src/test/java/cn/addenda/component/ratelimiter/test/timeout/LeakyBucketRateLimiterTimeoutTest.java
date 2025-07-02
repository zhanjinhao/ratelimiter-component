package cn.addenda.component.ratelimiter.test.timeout;

import cn.addenda.component.ratelimiter.LeakyBucketRateLimiter;

/**
 * @author addenda
 * @since 2022/12/28 17:13
 */
public class LeakyBucketRateLimiterTimeoutTest {

  public static void main(String[] args) {
    LeakyBucketRateLimiter leakyBucketRateLimiter = new LeakyBucketRateLimiter(1000L, 10);
    new RateLimiterTimeoutBaseTest(leakyBucketRateLimiter).test(true);
  }

}
