package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.ReferenceCountDelayedReleaseAllocator;
import cn.addenda.component.concurrency.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.RRateLimiterWrapper;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RKeys;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

@ToString(exclude = {"redissonClient"})
@Slf4j
public class RRateLimiterDelayedReleaseAllocator
        extends ReferenceCountDelayedReleaseAllocator<RRateLimiterWrapper>
        implements RateLimiterAllocator<RRateLimiterWrapper>, ApplicationListener<ContextRefreshedEvent> {

  private final RedissonClient redissonClient;
  private final RateType type;
  private final long interval;
  private final long rate;
  private final long keepAlive;
  private final boolean clearHistoricalRateConfig;

  private String namespace;

  private final long delayReleaseTtl;

  public RRateLimiterDelayedReleaseAllocator(RedissonClient redissonClient,
                                             RateType type, long interval, long rate, long keepAlive,
                                             boolean clearHistoricalRateConfig) {
    // delayReleaseTtl是keepAlive的1/3，用以保证本地的限流器比服务器的限流器更早过期
    super(new ReentrantSegmentLockFactory(), keepAlive / 3);
    this.redissonClient = redissonClient;
    this.type = type;
    this.interval = interval;
    this.rate = rate;
    this.keepAlive = keepAlive;
    this.clearHistoricalRateConfig = clearHistoricalRateConfig;
    this.delayReleaseTtl = keepAlive / 3;
  }

  @Override
  protected Function<String, RRateLimiterWrapper> referenceFunction() {
    return new Function<String, RRateLimiterWrapper>() {
      @Override
      public RRateLimiterWrapper apply(String name) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(getRateLimiterName(name));
        boolean b = rateLimiter.trySetRate(type, rate, Duration.ofMillis(interval), Duration.ofMillis(keepAlive));
        log.info("Create RRateLimiter[{}] using args: type[{}], rate[{}], interval[{}], keepAlive[{}]. The local keepAlive is [{}]. The result of trySetRate is [{}].",
                getRateLimiterName(name), type, rate, interval, keepAlive, getDelayReleaseTtl(), b);
        return new RRateLimiterWrapper(rateLimiter);
      }
    };
  }

  @Override
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  private String getRateLimiterName(String name) {
    return getFullPrefix() + name;
  }

  private String getFullPrefix() {
    return "ratelimiter:"
            + Optional.ofNullable(namespace).map(s -> s + ":").orElse("")
            + getName() + ":";
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    clearIfNecessary();
  }

  public void clearIfNecessary() {
    if (clearHistoricalRateConfig) {
      RKeys keys = redissonClient.getKeys();
      // 删除所有匹配的key
      keys.deleteByPattern(getFullPrefix() + "*");
    }
  }

}
