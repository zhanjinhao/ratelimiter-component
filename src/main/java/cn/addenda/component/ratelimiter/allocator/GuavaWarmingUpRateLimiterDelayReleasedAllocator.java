package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.ReferenceCountDelayedReleaseAllocator;
import cn.addenda.component.concurrency.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.GuavaRateLimiterWrapper;
import lombok.ToString;

import java.time.Duration;
import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
@ToString
public class GuavaWarmingUpRateLimiterDelayReleasedAllocator
        extends ReferenceCountDelayedReleaseAllocator<GuavaRateLimiterWrapper>
        implements RateLimiterAllocator<GuavaRateLimiterWrapper> {

  /**
   * 每秒允许通过的请求数量
   */
  private final double permitsPerSecond;
  /**
   * 预热时间（ms）
   */
  private final long warmupPeriod;

  private final long delayReleaseTtl;

  public GuavaWarmingUpRateLimiterDelayReleasedAllocator(double permitsPerSecond, long warmupPeriod, long delayReleaseTtl) {
    super(new ReentrantSegmentLockFactory(), delayReleaseTtl);
    this.permitsPerSecond = permitsPerSecond;
    this.warmupPeriod = warmupPeriod;
    this.delayReleaseTtl = delayReleaseTtl;
  }

  @Override
  protected Function<String, GuavaRateLimiterWrapper> referenceFunction() {
    return s -> new GuavaRateLimiterWrapper(permitsPerSecond, Duration.ofMillis(warmupPeriod));
  }

}
