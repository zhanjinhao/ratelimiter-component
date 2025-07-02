package cn.addenda.component.ratelimiter.test.timeout;

import cn.addenda.component.base.util.SleepUtils;
import cn.addenda.component.ratelimiter.RateLimiter;
import cn.addenda.component.ratelimiter.test.RateLimiterTestPojo;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author addenda
 * @since 2022/12/28 18:59
 */
public class RateLimiterTimeoutBaseTest {

  SecureRandom r = new SecureRandom();

  RateLimiter rateLimiter;

  public RateLimiterTimeoutBaseTest(RateLimiter rateLimiter) {
    this.rateLimiter = rateLimiter;
  }

  public void test(boolean outputAcquireSuccess) {
    AtomicLong acquireTimes = new AtomicLong(0L);
    AtomicLong passTimes = new AtomicLong(0L);
    List<Thread> threadList = new ArrayList<>();
    BlockingQueue<RateLimiterTestPojo> blockingQueue = new LinkedBlockingDeque<>();
    for (int i = 0; i < 100; i++) {
      threadList.add(new Thread(() -> {
        while (true) {
          long start = System.currentTimeMillis();
          boolean b = rateLimiter.tryAcquire(TimeUnit.MILLISECONDS, 2000);
          long end = System.currentTimeMillis();
          acquireTimes.incrementAndGet();
          if (b) {
            if (end - start >= 2000) {
              blockingQueue.offer(new RateLimiterTestPojo(start, end, true));
            } else {
              blockingQueue.offer(new RateLimiterTestPojo(start, end, false));
            }
            passTimes.incrementAndGet();
          }
          try {
            Thread.sleep(r.nextInt(50) + 50);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }));
    }

    if (outputAcquireSuccess) {
      new Thread(() -> {
        RateLimiterTestPojo pre = null;
        while (true) {
          try {
            RateLimiterTestPojo take = blockingQueue.take();
            take.print(pre);
            pre = take;
          } catch (InterruptedException e) {

          }
        }
      }).start();
    }

    for (Thread thread : threadList) {
      thread.start();
    }

    while (true) {
      SleepUtils.sleep(TimeUnit.SECONDS, 10);
      System.out.println("acquireTimes  : " + acquireTimes.get());
      System.out.println("passTimes     : " + passTimes.get());
    }
  }
}
