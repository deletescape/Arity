package org.javia.arity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MoreMathTest {

  @Test
  public void test() {
    assertThat(MoreMath.intLog10(-0.03), is(0));
    assertThat(MoreMath.intLog10(0.03), is(-2));
    assertThat(MoreMath.intExp10(3), is(1000d));
    assertThat(MoreMath.intExp10(-1), is(0.1d));
  }

}
