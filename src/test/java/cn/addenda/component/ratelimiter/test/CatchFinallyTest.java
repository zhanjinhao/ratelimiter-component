package cn.addenda.component.ratelimiter.test;

import cn.addenda.component.base.exception.ServiceException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

public class CatchFinallyTest {

  @Test
  public void test() {
    AtomicReference<String> catchReference = new AtomicReference<>();
    AtomicReference<String> finallyReference = new AtomicReference<>();

    Assert.assertThrows(ServiceException.class, () -> {
      try {
        throw new NullPointerException();
      } catch (Exception e) {
        catchReference.set("catch");
        throw new ServiceException();
      } finally {
        finallyReference.set("finally");
      }
    });

    Assert.assertNotNull(catchReference.get());
    Assert.assertNotNull(finallyReference.get());
  }

}
