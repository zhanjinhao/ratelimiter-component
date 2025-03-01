package cn.addenda.component.ratelimiter.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * @author addenda
 * @since 2023/8/26 23:05
 */
@Setter
@Getter
@ToString
@Builder
public class RateLimitationAttr {

  public static final String DEFAULT_NAMESPACE = "namespace";

  public static final String DEFAULT_PREFIX = "prefix";

  public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

  public static final String DEFAULT_RATE_LIMITED_MSG = "系统繁忙，请稍后再试！";

  @Builder.Default
  private String prefix = DEFAULT_PREFIX;

  private String spEL;

  private String rateLimiterAllocator;

  @Builder.Default
  private String rateLimitedMsg = DEFAULT_RATE_LIMITED_MSG;

}
