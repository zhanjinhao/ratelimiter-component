package cn.addenda.component.ratelimiter.test.tryacquire;

import cn.addenda.component.ratelimiter.RequestIntervalRateLimiter;

/**
 * @author addenda
 * @since 2022/12/28 17:13
 */
public class RequestIntervalRateLimiterTest {

  public static void main(String[] args) {
    RequestIntervalRateLimiter requestIntervalRateLimiter = new RequestIntervalRateLimiter(2d);
    new RateLimiterBaseTest(requestIntervalRateLimiter).test(true);
  }

}
