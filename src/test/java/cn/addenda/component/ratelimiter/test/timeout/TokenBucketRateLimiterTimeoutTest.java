package cn.addenda.component.ratelimiter.test.timeout;

import cn.addenda.component.ratelimiter.TokenBucketRateLimiter;

/**
 * @author addenda
 * @since 2022/12/29 19:14
 */
public class TokenBucketRateLimiterTimeoutTest {

  public static void main(String[] args) {
    TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(10, 10);
    new RateLimiterTimeoutBaseTest(tokenBucketRateLimiter).test(true);
  }

}
