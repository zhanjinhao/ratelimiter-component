package cn.addenda.component.ratelimiter.test.helper;

import cn.addenda.component.ratelimiter.RateLimiter;
import cn.addenda.component.ratelimiter.allocator.RRateLimiterDelayedReleaseAllocator;
import cn.addenda.component.ratelimiter.allocator.RateLimiterAllocator;
import cn.addenda.component.ratelimiter.helper.EnableRateLimitation;
import cn.addenda.component.ratelimiter.helper.RateLimitationAttr;
import cn.addenda.component.ratelimiter.helper.RateLimitationHelper;
import cn.addenda.component.ratelimiter.test.RedissonClientBaseTest;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author addeda
 * @since 2024/2/27 14:03
 */
@EnableRateLimitation
public class RateLimitationTestConfiguration {

  @Bean
  public RedissonClient redissonClient() throws Exception {
    return RedissonClientBaseTest.redissonClient();
  }

  private final RateType type = RateType.OVERALL;
  private final long interval = 10000;
  private final long rate = 10;
  private final long keepAlive = 20000;
  private final boolean clearHistoricalRateConfig = true;

  @Bean
  public RRateLimiterDelayedReleaseAllocator rRateLimiterDelayReleasedAllocator(RedissonClient redissonClient) {
    RRateLimiterDelayedReleaseAllocator rRateLimiterDelayedReleaseAllocator =
            new RRateLimiterDelayedReleaseAllocator(redissonClient, type, interval, rate, keepAlive, clearHistoricalRateConfig);
    rRateLimiterDelayedReleaseAllocator.setName(TestService.RATE_LIMITER_ALLOCATOR_NAME);
    return rRateLimiterDelayedReleaseAllocator;
  }

  @Bean
  public RateLimitationHelper rateLimitationHelper(List<? extends RateLimiterAllocator<? extends RateLimiter>> rateLimiterAllocatorList) {
    return new RateLimitationHelper(RateLimitationAttr.DEFAULT_NAMESPACE, rateLimiterAllocatorList);
  }

}
