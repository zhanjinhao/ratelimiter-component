package cn.addenda.component.ratelimiter.test.helper;

import cn.addenda.component.base.util.SleepUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

public class TestRateLimitation {

  AnnotationConfigApplicationContext context;

  @Before
  public void before() {
    context = new AnnotationConfigApplicationContext();
    context.register(RateLimitationTestConfiguration.class, TestServiceImpl.class);
    context.refresh();
  }

  @Test
  public void testAnnotation() {
    TestService testService = context.getBean(TestService.class);
    Integer failedCount = 0;
    Integer successCount = 0;
    while (true) {
      SleepUtils.sleep(TimeUnit.MILLISECONDS, 10);
      try {
        testService.printNow1();
        successCount++;
        if (successCount == 5) {
          SleepUtils.sleep(TimeUnit.MILLISECONDS, 6667);
        }
        if (successCount == 10) {
          SleepUtils.sleep(TimeUnit.MILLISECONDS, 20000);
        }
      } catch (Exception e) {
        failedCount++;
      }
      if (failedCount == 10000000) {
        break;
      }
    }
  }


  @Test
  public void testLambda() {
    TestService testService = context.getBean(TestService.class);
    Integer failedCount = 0;
    Integer successCount = 0;
    while (true) {
      SleepUtils.sleep(TimeUnit.MILLISECONDS, 10);
      try {
        testService.printNow2();
        successCount++;
        if (successCount == 5) {
          SleepUtils.sleep(TimeUnit.MILLISECONDS, 10000);
        }
        if (successCount == 10) {
          SleepUtils.sleep(TimeUnit.MILLISECONDS, 20000);
        }
      } catch (Exception e) {
        failedCount++;
      }
      if (failedCount == 10000000) {
        break;
      }
    }
  }

  // 带参数的测试案例

  @Test
  public void testAnnotationWithParam() throws Exception {
    TestService testService = context.getBean(TestService.class);

    Thread thread1 = new Thread(() -> {
      Integer failedCount = 0;
      Integer successCount = 0;
      while (true) {
        SleepUtils.sleep(TimeUnit.MILLISECONDS, 10);
        try {
          testService.printNow3("test1");
          successCount++;
          if (successCount == 5) {
            SleepUtils.sleep(TimeUnit.MILLISECONDS, 10000);
          }
          if (successCount == 10) {
            SleepUtils.sleep(TimeUnit.MILLISECONDS, 20000);
          }
        } catch (Exception e) {
          failedCount++;
        }
      }
    });

    Thread thread2 = new Thread(() -> {
      Integer failedCount = 0;
      Integer successCount = 0;
      while (true) {
        SleepUtils.sleep(TimeUnit.MILLISECONDS, 10);
        try {
          testService.printNow3("test2");
          successCount++;
          if (successCount == 5) {
            SleepUtils.sleep(TimeUnit.MILLISECONDS, 10000);
          }
          if (successCount == 10) {
            SleepUtils.sleep(TimeUnit.MILLISECONDS, 20000);
          }
        } catch (Exception e) {
          failedCount++;
        }
      }
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

  }

  // 带参数的测试案例

  @Test
  public void testLambdaWithParam() throws Exception {
    TestService testService = context.getBean(TestService.class);

    Thread thread1 = new Thread(() -> {
      Integer failedCount = 0;
      Integer successCount = 0;
      while (true) {
        SleepUtils.sleep(TimeUnit.MILLISECONDS, 10);
        try {
          testService.printNow4("test1");
          successCount++;
          if (successCount == 5) {
            SleepUtils.sleep(TimeUnit.MILLISECONDS, 10000);
          }
          if (successCount == 10) {
            SleepUtils.sleep(TimeUnit.MILLISECONDS, 20000);
          }
        } catch (Exception e) {
          failedCount++;
        }
      }
    });

    Thread thread2 = new Thread(() -> {
      Integer failedCount = 0;
      Integer successCount = 0;
      while (true) {
        SleepUtils.sleep(TimeUnit.MILLISECONDS, 10);
        try {
          testService.printNow4("test2");
          successCount++;
          if (successCount == 5) {
            SleepUtils.sleep(TimeUnit.MILLISECONDS, 10000);
          }
          if (successCount == 10) {
            SleepUtils.sleep(TimeUnit.MILLISECONDS, 20000);
          }
        } catch (Exception e) {
          failedCount++;
        }
      }
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();
  }

  @After
  public void close() {
    if (context != null) {
      context.close();
    }
  }

}
