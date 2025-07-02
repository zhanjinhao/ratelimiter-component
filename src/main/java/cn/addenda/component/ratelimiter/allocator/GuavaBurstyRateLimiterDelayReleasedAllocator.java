package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.ReferenceCountDelayedReleaseAllocator;
import cn.addenda.component.concurrency.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.GuavaRateLimiterWrapper;
import lombok.ToString;

import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
@ToString
public class GuavaBurstyRateLimiterDelayReleasedAllocator
        extends ReferenceCountDelayedReleaseAllocator<GuavaRateLimiterWrapper>
        implements RateLimiterAllocator<GuavaRateLimiterWrapper> {

  /**
   * 每秒允许通过的请求数量
   */
  private final double permitsPerSecond;

  private final long delayReleaseTtl;

  public GuavaBurstyRateLimiterDelayReleasedAllocator(double permitsPerSecond, long delayReleaseTtl) {
    super(new ReentrantSegmentLockFactory(), delayReleaseTtl);
    this.permitsPerSecond = permitsPerSecond;
    this.delayReleaseTtl = delayReleaseTtl;
  }

  @Override
  protected Function<String, GuavaRateLimiterWrapper> referenceFunction() {
    return s -> new GuavaRateLimiterWrapper(permitsPerSecond);
  }

}
