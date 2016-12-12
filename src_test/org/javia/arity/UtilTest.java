package org.javia.arity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UtilTest {

  @Test
  public void test() {
    assertThat(Util.shortApprox(1.235, 0.02), is(1.24));
    assertThat(Util.shortApprox(1.235, 0.4), is(1.2000000000000002));
    assertThat(Util.shortApprox(-1.235, 0.02), is(-1.24));
    assertThat(Util.shortApprox(-1.235, 0.4), is(-1.2000000000000002));
  }

}
