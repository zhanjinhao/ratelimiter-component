package cn.addenda.component.ratelimiter;

import cn.addenda.component.base.datetime.DateUtils;
import cn.addenda.component.base.exception.SystemException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/9/7 18:19
 */
public class RateLimiterException extends SystemException {

  public RateLimiterException() {
  }

  public RateLimiterException(String message) {
    super(message);
  }

  public RateLimiterException(String message, Throwable cause) {
    super(message, cause);
  }

  public RateLimiterException(Throwable cause) {
    super(cause);
  }

  public RateLimiterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public String moduleName() {
    return "ratelimiter";
  }

  @Override
  public String componentName() {
    return "ratelimiter";
  }

  public static RateLimiterException timeout(long start, int permits, TimeUnit timeUnit, long timeout) {
    LocalDateTime startTime = DateUtils.dateToLocalDateTime(new Date(start));
    LocalDateTime expireTime = startTime.plus(timeout, ChronoUnit.MILLIS);
    String msg = String.format("acquire [%s] permit(s) timeout. start: [%s]. timeout: [%s]ms. expireTime: [%s].",
            permits, DateUtils.format(startTime, DateUtils.yMdHmsS_FORMATTER), timeUnit.toMillis(timeout), expireTime);
    return new RateLimiterException(msg);
  }

  public static RateLimiterException exceed(long start, int permits, long maxQueueingTime) {
    LocalDateTime startTime = DateUtils.dateToLocalDateTime(new Date(start));
    String msg = String.format("acquire [%s] permit(s) and exceed max queueing time. now: [%s]. maxQueueingTime: [%s].",
            permits, DateUtils.format(startTime, DateUtils.yMdHmsS_FORMATTER), maxQueueingTime);
    return new RateLimiterException(msg);
  }

}
