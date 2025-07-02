package cn.addenda.component.ratelimiter.test.helper;

import cn.addenda.component.ratelimiter.helper.RateLimitationHelper;
import cn.addenda.component.ratelimiter.helper.RateLimited;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TestServiceImpl implements TestService {

  @Autowired
  private RateLimitationHelper rateLimitationHelper;

  @Override
  @RateLimited(rateLimiterAllocator = RATE_LIMITER_ALLOCATOR_NAME,
          prefix = "print", spEL = "printNow1")
  public void printNow1() {
    log.info("{}", System.currentTimeMillis());
  }

  @Override
  public void printNow2() {
    rateLimitationHelper.rateLimit(
            RATE_LIMITER_ALLOCATOR_NAME,
            () -> {
              log.info("{}", System.currentTimeMillis());
            }, "printNow2");
  }

  @Override
  @RateLimited(rateLimiterAllocator = RATE_LIMITER_ALLOCATOR_NAME,
          prefix = "print", spEL = "#args[0]")
  public void printNow3(String test) {
    log.info("{} : {}", test, System.currentTimeMillis());
  }

  @Override
  public void printNow4(String test) {
    rateLimitationHelper.rateLimit(
            RATE_LIMITER_ALLOCATOR_NAME,
            "print",
            "#args[0]",
            () -> {
              log.info("{} : {}", test, System.currentTimeMillis());
            }, test);
  }

}
