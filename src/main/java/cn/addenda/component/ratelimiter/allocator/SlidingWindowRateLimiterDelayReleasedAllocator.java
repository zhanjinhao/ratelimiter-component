package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.ReferenceCountDelayedReleaseAllocator;
import cn.addenda.component.concurrency.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.SlidingWindowRateLimiter;
import lombok.ToString;

import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
@ToString
public class SlidingWindowRateLimiterDelayReleasedAllocator
        extends ReferenceCountDelayedReleaseAllocator<SlidingWindowRateLimiter>
        implements RateLimiterAllocator<SlidingWindowRateLimiter> {

  /**
   * 总的计数阈值
   */
  private final long permits;
  /**
   * 窗口的总时长（ms）
   */
  private final long duration;
  /**
   * 单个窗口的时长（ms）
   */
  private final long windowDuration;
  /**
   * 窗口的个数
   */
  private final int windowCount;
  /**
   * 是否是乐观模式：<p/>
   * 1.乐观模式可能会造成流量超出预期，但吞吐量大<p/>
   * 2.非乐观模式保证流量一定不会超出预期，但吞吐量小
   */
  private final boolean optimistic;

  private final long delayReleaseTtl;

  public SlidingWindowRateLimiterDelayReleasedAllocator(long permits, long duration, long windowDuration, boolean optimistic, long delayReleaseTtl) {
    super(new ReentrantSegmentLockFactory(), delayReleaseTtl);
    this.permits = permits;
    this.duration = duration;
    this.windowDuration = windowDuration;
    this.windowCount = (int) (duration / windowDuration);
    this.optimistic = optimistic;
    this.delayReleaseTtl = delayReleaseTtl;
  }

  @Override
  protected Function<String, SlidingWindowRateLimiter> referenceFunction() {
    return s -> new SlidingWindowRateLimiter(permits, duration, windowDuration, optimistic);
  }

}
