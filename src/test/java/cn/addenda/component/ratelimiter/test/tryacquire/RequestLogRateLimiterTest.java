package cn.addenda.component.ratelimiter.test.tryacquire;

import cn.addenda.component.ratelimiter.RequestLogRateLimiter;

/**
 * @author addenda
 * @since 2022/12/28 14:43
 */
public class RequestLogRateLimiterTest {

  public static void main(String[] args) {
    RequestLogRateLimiter requestLogRateLimiter = new RequestLogRateLimiter(10, 1000);
    new RateLimiterBaseTest(requestLogRateLimiter).test(true);
  }

}
