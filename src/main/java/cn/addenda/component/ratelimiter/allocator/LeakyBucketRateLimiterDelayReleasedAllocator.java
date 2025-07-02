package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.ReferenceCountDelayedReleaseAllocator;
import cn.addenda.component.concurrency.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.LeakyBucketRateLimiter;
import lombok.ToString;

import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
@ToString
public class LeakyBucketRateLimiterDelayReleasedAllocator
        extends ReferenceCountDelayedReleaseAllocator<LeakyBucketRateLimiter>
        implements RateLimiterAllocator<LeakyBucketRateLimiter> {

  /**
   * 最大等待时间（ms）
   */
  private final long maxQueueingTime;
  /**
   * 每秒允许通过的请求数量
   */
  private final double permitsPerSecond;

  private final long delayReleaseTtl;

  public LeakyBucketRateLimiterDelayReleasedAllocator(long maxQueueingTime, double permitsPerSecond, long delayReleaseTtl) {
    super(new ReentrantSegmentLockFactory(), delayReleaseTtl);
    this.maxQueueingTime = maxQueueingTime;
    this.permitsPerSecond = permitsPerSecond;
    this.delayReleaseTtl = delayReleaseTtl;
  }

  @Override
  protected Function<String, LeakyBucketRateLimiter> referenceFunction() {
    return s -> new LeakyBucketRateLimiter(maxQueueingTime, permitsPerSecond);
  }

}
