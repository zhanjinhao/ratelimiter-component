package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.ReferenceCountDelayedReleaseAllocator;
import cn.addenda.component.concurrency.allocator.factory.ReentrantSegmentLockFactory;
import cn.addenda.component.ratelimiter.TokenBucketRateLimiter;
import lombok.ToString;

import java.util.function.Function;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
@ToString
public class TokenBucketRateLimiterDelayReleasedAllocator
        extends ReferenceCountDelayedReleaseAllocator<TokenBucketRateLimiter>
        implements RateLimiterAllocator<TokenBucketRateLimiter> {

  /**
   * 令牌桶的容量
   */
  private final long capacity;
  /**
   * 每秒产生的令牌的数量
   */
  private final long permitsPerSecond;

  private final long delayReleaseTtl;

  public TokenBucketRateLimiterDelayReleasedAllocator(long capacity, long permitsPerSecond, long delayReleaseTtl) {
    super(new ReentrantSegmentLockFactory(), delayReleaseTtl);
    this.capacity = capacity;
    this.permitsPerSecond = permitsPerSecond;
    this.delayReleaseTtl = delayReleaseTtl;
  }

  @Override
  protected Function<String, TokenBucketRateLimiter> referenceFunction() {
    return s -> new TokenBucketRateLimiter(capacity, permitsPerSecond);
  }

}
