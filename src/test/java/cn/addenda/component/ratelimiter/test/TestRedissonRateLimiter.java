package cn.addenda.component.ratelimiter.test;

import org.junit.Assert;
import org.junit.Test;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestRedissonRateLimiter {

  @Test
  public void test1() throws Exception {

    RedissonClient redisson = RedissonClientBaseTest.redissonClient();

    RRateLimiter rr = redisson.getRateLimiter("test_change_rate");

    rr.setRate(RateType.OVERALL, 10, Duration.ofSeconds(1));
    Assert.assertEquals(rr.getConfig().getRate().longValue(), 10L);
    //check value in Redis
    rr.acquire(1);
    Stream<String> keysStream = redisson.getKeys().getKeysStream();
    System.out.println(keysStream.collect(Collectors.toSet()));
    String valueKey = redisson.getKeys().getKeysStream().filter(k -> k.contains("{test_change_rate}:value")).findAny().get();
    Long value = redisson.getAtomicLong(valueKey).get();
    Assert.assertEquals(value.longValue(), 9L);

    //change to 20/s
    rr.setRate(RateType.OVERALL, 20, Duration.ofSeconds(1));
    Assert.assertEquals(rr.getConfig().getRate().longValue(), 20L);
    //check value in Redis
    rr.acquire(1);
    value = redisson.getAtomicLong(valueKey).get();
    Assert.assertEquals(value.longValue(), 19L);

    /* Test case -- OVERALL */
    rr.setRate(RateType.OVERALL, 10, Duration.ofSeconds(1));
    Assert.assertEquals(rr.getConfig().getRate().longValue(), 10L);
    //check value in Redis
    rr.acquire(1);
    valueKey = redisson.getKeys().getKeysStream().filter(k -> k.contains("{test_change_rate}:value")).findAny().get();
    value = redisson.getAtomicLong(valueKey).get();
    Assert.assertEquals(value.longValue(), 9L);

    rr.setRate(RateType.OVERALL, 20, Duration.ofSeconds(1));
    Assert.assertEquals(rr.getConfig().getRate().longValue(), 20L);
    //check value in Redis
    rr.acquire(1);
    value = redisson.getAtomicLong(valueKey).get();
    Assert.assertEquals(value.longValue(), 19L);

    //clean all keys in test
    redisson.getKeys().deleteByPattern("*test_change_rate*");

  }

  @Test
  public void test2() throws Exception {
    RedissonClient redisson = RedissonClientBaseTest.redissonClient();

    RRateLimiter rr = redisson.getRateLimiter("addenda.limiter");

    rr.setRate(RateType.OVERALL, 10, Duration.ofSeconds(10));

    while (true) {
      rr.acquire(1);
      System.out.println(System.currentTimeMillis());
    }

  }


}
