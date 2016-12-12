package org.javia.arity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ComplexTest {

  @Test
  public void case1() {
    assertThat(new Complex(-1, 0).pow(new Complex(0, 1)), is(new Complex(0.04321391826377226, 0)));
    assertThat(new Complex(-1, 0).pow(new Complex(1, 1)), is(new Complex(-0.04321391826377226, 0)));
  }

  @Test
  public void case2() {
    assertThat(new Complex(-1, 0).abs(), is(1d));
    assertThat(new Complex(Math.E * Math.E, 0).log(), is(new Complex(2, 0)));
    assertThat(new Complex(-1, 0).log(), is(new Complex(0, Math.PI)));
  }

  @Test
  public void case3() {
    // assertThat(new Complex(2, 0).exp(), is(new Complex(Math.E * Math.E, 0)));
    assertThat(new Complex(0, Math.PI).exp(), is(new Complex(-1, 0)));
  }

  @Test
  public void case4() {
    assertThat(MoreMath.lgamma(1), is(0d));
    assertThat(new Complex(1, 0).lgamma(), is(new Complex(0, 0)));
  }

  @Test
  public void case5() {
    assertThat(new Complex(0, 0).factorial(), is(new Complex(1, 0)));
    assertThat(new Complex(1, 0).factorial(), is(new Complex(1, 0)));
    // assertThat(new Complex(0, 1).factorial(), is(new Complex(0.49801566811835596,
    // -0.1549498283018106)));
    assertThat(new Complex(-2, 1).factorial(), is(new Complex(-0.17153291990834815, 0.32648274821006623)));
    assertThat(new Complex(4, 0).factorial(), is(new Complex(24, 0)));
    // assertThat(new Complex(4, 3).factorial(), is(new Complex(0.016041882741649555,
    // -9.433293289755953)));
  }

}
