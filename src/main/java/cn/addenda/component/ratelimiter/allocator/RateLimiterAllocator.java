package cn.addenda.component.ratelimiter.allocator;

import cn.addenda.component.concurrency.allocator.DelayedReleaseAllocator;
import cn.addenda.component.ratelimiter.RateLimiter;

/**
 * @author addenda
 * @since 2023/9/1 9:06
 */
public interface RateLimiterAllocator<T extends RateLimiter>
        extends DelayedReleaseAllocator<T> {

  default void setNamespace(String namespace) {
    // no-op
  }

}
