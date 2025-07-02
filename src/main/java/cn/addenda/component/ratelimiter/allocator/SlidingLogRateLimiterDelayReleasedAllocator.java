package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.ReferenceCountDelayedReleaseAllocator;
import cn.addenda.component.concurrency.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.SlidingLogRateLimiter;
import lombok.ToString;

import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
@ToString
public class SlidingLogRateLimiterDelayReleasedAllocator
        extends ReferenceCountDelayedReleaseAllocator<SlidingLogRateLimiter>
        implements RateLimiterAllocator<SlidingLogRateLimiter> {

  /**
   * 总的计数阈值
   */
  private final long permits;
  /**
   * 日志的总时长（ms）
   */
  private final long duration;

  private final long delayReleaseTtl;

  public SlidingLogRateLimiterDelayReleasedAllocator(long permits, long duration, long delayReleaseTtl) {
    super(new ReentrantSegmentLockFactory(), delayReleaseTtl);
    this.permits = permits;
    this.duration = duration;
    this.delayReleaseTtl = delayReleaseTtl;
  }

  @Override
  protected Function<String, SlidingLogRateLimiter> referenceFunction() {
    return s -> new SlidingLogRateLimiter(permits, duration);
  }

}
