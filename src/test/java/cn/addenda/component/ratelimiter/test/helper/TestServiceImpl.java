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
  @RateLimited(rateLimiterAllocator = "ratelimiter-component-test",
          prefix = "print", spEL = "printNow1")
  public void printNow1() {
    log.info("{}", System.currentTimeMillis());
  }

  @Override
  public void printNow2() {
    rateLimitationHelper.rateLimit(
            "ratelimiter-component-test",
            () -> {
              log.info("{}", System.currentTimeMillis());
            }, "printNow2");
  }

}
