package cn.addenda.component.ratelimiter.test;

import cn.addenda.component.base.datetime.DateUtils;
import cn.addenda.component.base.string.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RateLimiterTestPojo {

  private Long start;

  private Long end;

  private boolean ifError;

  public RateLimiterTestPojo(Long start, Long end, boolean ifError) {
    this.start = start;
    this.end = end;
    this.ifError = ifError;
  }

  public void print(RateLimiterTestPojo pre) {
    if (pre == null) {
      pre = this;
    }
    String a = start + "(" + format(start) + ")" + " -> " + end + "(" + format(end) + ")" + " : wait(" + StringUtils.expandWithZero(String.valueOf(end - start), 4) + ")" + " : interval(" + (this.end - pre.end) + ")";
    if (ifError) {
      System.err.println(a);
    } else {
      System.out.println(a);
    }
  }

  private String format(long ts) {
    return DateUtils.format(DateUtils.timestampToLocalDateTime(ts), DateUtils.yMdHmsS_FORMATTER);
  }

}
