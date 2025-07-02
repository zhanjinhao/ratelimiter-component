package cn.addenda.component.ratelimiter.test.timeout;

import cn.addenda.component.ratelimiter.RRateLimiterWrapper;
import cn.addenda.component.ratelimiter.test.RedissonClientBaseTest;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.time.Duration;

/**
 * @author addenda
 * @since 2023/9/11 18:29
 */
public class RedissonRateLimiterTimeoutTest {

  public static void main(String[] args) {
    RedissonClient redissonClient = RedissonClientBaseTest.redissonClient();

    RRateLimiter rateLimiter = redissonClient.getRateLimiter("ratelimiter-component:RedissonRateLimiterTimeoutTest");
    rateLimiter.trySetRate(RateType.OVERALL, 10, Duration.ofSeconds(1));
    RRateLimiterWrapper rRateLimiterWrapper = new RRateLimiterWrapper(rateLimiter);
    new RateLimiterTimeoutBaseTest(rRateLimiterWrapper).test(true);
  }
}
