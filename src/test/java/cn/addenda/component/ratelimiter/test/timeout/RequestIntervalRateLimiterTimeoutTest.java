package cn.addenda.component.ratelimiter.test.timeout;

import cn.addenda.component.ratelimiter.RequestIntervalRateLimiter;

/**
 * @author addenda
 * @since 2022/12/28 17:13
 */
public class RequestIntervalRateLimiterTimeoutTest {

  public static void main(String[] args) {
    RequestIntervalRateLimiter requestIntervalRateLimiter = new RequestIntervalRateLimiter(10d);
    new RateLimiterTimeoutBaseTest(requestIntervalRateLimiter).test(true);
  }

}
