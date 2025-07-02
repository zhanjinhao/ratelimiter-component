package cn.addenda.component.ratelimiter.test.tryacquire;

import cn.addenda.component.base.util.SleepUtils;
import cn.addenda.component.ratelimiter.GuavaRateLimiterWrapper;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2022/12/29 19:14
 */
public class GuavaRateLimiterTest {

  public static void main(String[] args) {
    GuavaRateLimiterWrapper guavaRateLimiterWrapper = new GuavaRateLimiterWrapper(10, 1, TimeUnit.SECONDS);
    new RateLimiterBaseTest(guavaRateLimiterWrapper).test(true);
  }


  /**
   * 突发模式
   */
  @Test
  public void test0() {
    // 1739621245230
    // 1739621245230
    // 1739621245230
    // 1739621245230
    // 1739621245392
    // 1739621245593
    // 1739621245793
    RateLimiter rateLimiter = RateLimiter.create(5);
    SleepUtils.sleep(TimeUnit.MILLISECONDS, 600);
    for (int i = 0; i < 100000000; i++) {
      if (rateLimiter.tryAcquire()) {
        System.out.println(System.currentTimeMillis());
      }
    }
  }


  /**
   * 透支未来令牌
   */
  @Test
  public void test1() {
    // Get 10 tokens spend 0.000000 s
    // Get 10 tokens spend 4.498882 s
    // Get 10 tokens spend 2.998378 s
    // Get 10 tokens spend 3.000321 s
    // end
    // Get 10 tokens spend 2.991381 s
    // Get 10 tokens spend 2.999528 s
    // Get 10 tokens spend 2.992478 s
    // Get 10 tokens spend 3.000219 s
    // end
    // Get 10 tokens spend 2.986078 s
    // Get 10 tokens spend 2.999933 s
    // Get 10 tokens spend 2.996434 s
    RateLimiter r = RateLimiter.create(2, 3, TimeUnit.SECONDS);
    while (true) {
      System.out.printf("Get 10 tokens spend %f s%n", r.acquire(6));
      System.out.printf("Get 10 tokens spend %f s%n", r.acquire(6));
      System.out.printf("Get 10 tokens spend %f s%n", r.acquire(6));
      System.out.printf("Get 10 tokens spend %f s%n", r.acquire(6));
      System.out.println("end");
    }

  }


  /**
   * 预热模式
   */
  @Test
  public void test2() {
    RateLimiter rateLimiter = RateLimiter.create(2, 2, TimeUnit.SECONDS);
//    SleepUtils.sleep(TimeUnit.MILLISECONDS, 1000);

    for (int i = 0; i < 100; i++) {
      System.out.println(rateLimiter.acquire());
    }
  }

}
