package cn.addenda.component.ratelimiter.test.tryacquire;

import cn.addenda.component.ratelimiter.LeakyBucketRateLimiter;

/**
 * @author addenda
 * @since 2022/12/28 15:14
 */
public class LeakyBucketRateLimiterTest {

  public static void main(String[] args) {
    LeakyBucketRateLimiter leakyBucketRateLimiter = new LeakyBucketRateLimiter(200L, 2);
    new RateLimiterBaseTest(leakyBucketRateLimiter).test(true);
  }

}
