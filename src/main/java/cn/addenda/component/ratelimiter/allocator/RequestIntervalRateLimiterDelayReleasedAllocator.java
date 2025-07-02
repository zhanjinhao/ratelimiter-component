package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.ReferenceCountDelayedReleaseAllocator;
import cn.addenda.component.concurrency.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.RequestIntervalRateLimiter;
import lombok.ToString;

import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
@ToString
public class RequestIntervalRateLimiterDelayReleasedAllocator
        extends ReferenceCountDelayedReleaseAllocator<RequestIntervalRateLimiter>
        implements RateLimiterAllocator<RequestIntervalRateLimiter> {

  /**
   * 每两次请求之间的间隔（ms）
   */
  private final long interval;

  private final long delayReleaseTtl;

  public RequestIntervalRateLimiterDelayReleasedAllocator(long interval, long delayReleaseTtl) {
    super(new ReentrantSegmentLockFactory(), delayReleaseTtl);
    this.interval = interval;
    this.delayReleaseTtl = delayReleaseTtl;
  }

  @Override
  protected Function<String, RequestIntervalRateLimiter> referenceFunction() {
    return s -> new RequestIntervalRateLimiter(interval);
  }

}
