package cn.addenda.component.ratelimiter.test.tryacquire;

import cn.addenda.component.ratelimiter.RRateLimiterWrapper;
import cn.addenda.component.ratelimiter.test.RedissonClientBaseTest;
import org.junit.Test;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.time.Duration;

/**
 * @author addenda
 * @since 2023/9/11 18:29
 */
public class RedissonRateLimiterTest {

  @Test
  public void test() throws Exception {
    RedissonClient redissonClient = RedissonClientBaseTest.redissonClient();

    RRateLimiter rateLimiter = redissonClient.getRateLimiter("ratelimiter-component:RedissonRateLimiterTest");
    rateLimiter.trySetRate(RateType.OVERALL, 10, Duration.ofSeconds(10));
    RRateLimiterWrapper rRateLimiterWrapper = new RRateLimiterWrapper(rateLimiter);
    new RateLimiterBaseTest(rRateLimiterWrapper).test(true);
  }

}
