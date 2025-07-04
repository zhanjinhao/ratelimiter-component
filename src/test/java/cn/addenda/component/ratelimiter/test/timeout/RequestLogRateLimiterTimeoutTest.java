package cn.addenda.component.ratelimiter.test.timeout;

import cn.addenda.component.ratelimiter.RequestLogRateLimiter;

/**
 * @author addenda
 * @since 2022/12/28 14:43
 */
public class RequestLogRateLimiterTimeoutTest {

  public static void main(String[] args) {
    RequestLogRateLimiter requestLogRateLimiter = new RequestLogRateLimiter(10, 1000);
    new RateLimiterTimeoutBaseTest(requestLogRateLimiter).test(true);
  }

}
