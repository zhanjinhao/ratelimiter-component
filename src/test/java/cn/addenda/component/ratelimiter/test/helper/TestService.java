package cn.addenda.component.ratelimiter.test.helper;

public interface TestService {

  String RATE_LIMITER_ALLOCATOR_NAME = "ratelimiter-component-test";

  void printNow1();

  void printNow2();

  void printNow3(String test);

  void printNow4(String test);

}
