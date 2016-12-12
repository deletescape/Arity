package org.javia.arity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EvalTest {

  @Test
  public void evalCase1() throws SyntaxException {
    assertThat(new Symbols().eval("."), is(0d));
    assertThat(new Symbols().eval("1+."), is(1d));
    assertThat(new Symbols().eval("1"), is(1d));
    assertThat(new Symbols().eval("\u03c0"), is(Math.PI));
    assertThat(new Symbols().eval("2\u00d73"), is(6d)); // 2*3
    assertThat(new Symbols().eval("1+\u221a9*2"), is(7d)); // 1+sqrt(9)*2
    assertThat(new Symbols().eval("3\u221a 4"), is(6d)); // 3*sqrt(4)
    assertThat(new Symbols().eval("\u221a16sin(2\u03c0/4)"), is(4d)); // sqrt(16)*sin(2pi/4)
    assertThat(new Symbols().eval("1+1"), is(2d));
    assertThat(new Symbols().eval("1+-1"), is(0d));
    assertThat(new Symbols().eval("-0.5"), is(-.5d));
    assertThat(new Symbols().eval("+1e2"), is(100d));
    assertThat(new Symbols().eval("1e-1"), is(.1d));
    assertThat(new Symbols().eval("1e\u22122"), is(.01d)); // unicode minus
    assertThat(new Symbols().eval("-2^3!"), is(-64d));
    assertThat(new Symbols().eval("(-2)^3!"), is(64d));
    assertThat(new Symbols().eval("-2^1^2"), is(-2d));
    assertThat(new Symbols().eval("--1"), is(1d));
    assertThat(new Symbols().eval("-3^--2"), is(-9d));
    assertThat(new Symbols().eval("1+2)(2+3"), is(15d));
    assertThat(new Symbols().eval("1+2)!^-2"), is(1. / 36d));
    assertThat(new Symbols().eval("sin(0)"), is(0d));
    assertThat(new Symbols().eval("cos(0)"), is(1d));
    assertThat(new Symbols().eval("sin(-1--1)"), is(0d));
    assertThat(new Symbols().eval("-(2+1)*-(4/2)"), is(6d));
    assertThat(new Symbols().eval("-.5E-1"), is(-.05d));
    assertThat(new Symbols().eval("2 3 4"), is(24d));
    assertThat(new Symbols().eval("pi"), is(Math.PI));
    assertThat(new Symbols().eval("e"), is(Math.E));
    assertThat(new Symbols().eval("sin(pi/2)"), is(1d));
    assertThat(new Symbols().eval("NaN"), is(Double.NaN));
    assertThat(new Symbols().eval("Inf"), is(Double.POSITIVE_INFINITY));
    assertThat(new Symbols().eval("Infinity"), is(Double.POSITIVE_INFINITY));
    assertThat(new Symbols().eval("-Inf"), is(Double.NEGATIVE_INFINITY));
    assertThat(new Symbols().eval("0/0"), is(Double.NaN));
    try {
      new Symbols().eval("1+");

    } catch (SyntaxException e) {
      assertThat(e.toString(), is("SyntaxException: unexpected ) or END in '1+' at position 2"));
    }
    try {
      new Symbols().eval("1E1.5");
    } catch (SyntaxException e) {
      assertThat(e.toString(), is("SyntaxException: invalid number '1E1.5' in '1E1.5' at position 0"));
    }
    try {
      new Symbols().eval("=");
    } catch (SyntaxException e) {
      assertThat(e.toString(), is("SyntaxException: invalid character '=' in '=' at position 0"));
    }
  }

  @Test
  public void evalCase2() throws SyntaxException {
    assertThat(new Symbols().eval("comb(11, 9)"), is(55d));
    assertThat(new Symbols().eval("perm(11, 2)"), is(110d));
    assertThat(new Symbols().eval("comb(1000, 999)"), is(1000d));
    assertThat(new Symbols().eval("perm(1000, 1)"), is(1000d));
  }

  @Test
  public void evalCase3() throws SyntaxException {
    assertThat(new Symbols().eval("abs(3-4i)"), is(5d));
    assertThat(new Symbols().eval("exp(pi*i)"), is(-1d));
  }

  @Test
  public void evalCase4() throws SyntaxException {
    assertThat(new Symbols().eval("5%"), is(0.05d));
    assertThat(new Symbols().eval("200+5%"), is(210d));
    assertThat(new Symbols().eval("200-5%"), is(190d));
    assertThat(new Symbols().eval("100/200%"), is(50d));
    assertThat(new Symbols().eval("100+200%+5%"), is(315d));
  }

  @Test
  public void evalCase5() throws SyntaxException {
    assertThat(new Symbols().eval("mod(5,3)"), is(2d));
    assertThat(new Symbols().eval("5.2 # 3.2"), is(2d));
  }

  @Test
  public void evalCase6() throws SyntaxException {
    assertThat(new Symbols().eval("100.1-100-.1"), is(0d));
    assertThat(new Symbols().eval("1.1-1+(-.1)"), is(0d));
  }

  @Test
  public void evalCase7() throws SyntaxException {
    assertThat(new Symbols().eval("log(2,8)"), is(3d));
    assertThat(new Symbols().eval("log(9,81)"), is(2d));
    assertThat(new Symbols().eval("log(4,2)"), is(.5d));
  }

  @Test
  public void evalCase8() throws SyntaxException {
    assertThat(new Symbols().eval("sin'(0)"), is(1d));
    assertThat(new Symbols().eval("cos'(0)"), is(-0d));
    assertThat(new Symbols().eval("cos'(pi/2)"), is(-1d));
    assertThat(new Symbols().eval("abs'(2)"), is(1d));
    assertThat(new Symbols().eval("abs'(-3)"), is(-1d));
  }

  @Test
  public void evalCase9() throws SyntaxException {
    assertThat(new Symbols().eval("0x0"), is(0d));
    assertThat(new Symbols().eval("0x100"), is(256d));
    assertThat(new Symbols().eval("0X10"), is(16d));
    assertThat(new Symbols().eval("0b10"), is(2d));
    assertThat(new Symbols().eval("0o10"), is(8d));
    assertThat(new Symbols().eval("sin(0x1*pi/2)"), is(1d));
  }

  @Test
  public void evalCase10() throws SyntaxException {
    assertThat(new Symbols().eval("ln(e)"), is(1d));
    assertThat(new Symbols().eval("log(10)"), is(1d));
    assertThat(new Symbols().eval("log10(100)"), is(2d));
    // assertThat(new Symbols().eval("lg(.1)"), is(-1d)); defunct: equals on floating point value
    assertThat(new Symbols().eval("log2(2)"), is(1d));
    assertThat(new Symbols().eval("lb(256)"), is(8d));
  }

  @Test
  public void evalCase11() throws SyntaxException {
    assertThat(new Symbols().eval("rnd()*0"), is(0d));
    assertThat(new Symbols().eval("rnd(5)*0"), is(0d));
  }

  @Test
  public void evalCase12() throws SyntaxException {
    assertThat(new Symbols().eval("max(2,3)"), is(3d));
    assertThat(new Symbols().eval("min(2,3)"), is(2d));
    assertThat(new Symbols().eval("cbrt(8)"), is(2d));
    assertThat(new Symbols().eval("cbrt(-8)"), is(-2d));
  }

  @Test
  public void evalCase13() throws SyntaxException {
    assertThat(new Symbols().eval("real(8.123)"), is(8.123d));
    assertThat(new Symbols().eval("imag(8.123)"), is(0d));
    assertThat(new Symbols().eval("im(sqrt(-1))"), is(1d));
    assertThat(new Symbols().eval("im(nan)"), is(Double.NaN));
  }

  @Test
  public void evalComplexCase1() throws SyntaxException {
    assertThat(new Symbols().evalComplex("sqrt(-1)^2").toString(), is(new Complex(-1, 0).toString()));
    assertThat(new Symbols().evalComplex("i").toString(), is(new Complex(0, 1).toString()));
    assertThat(new Symbols().evalComplex("sqrt(-1)").toString(), is(new Complex(0, 1).toString()));
  }

  @Test
  public void evalComplexCase2() throws SyntaxException {
    assertThat(new Symbols().evalComplex("ln(-1)").toString(), is(new Complex(0, -Math.PI).toString()));
    assertThat(new Symbols().evalComplex("i^i").toString(), is(new Complex(0.20787957635076193, 0).toString()));
    assertThat(new Symbols().evalComplex("gcd(135-14i, 155+34i)").toString(), is(new Complex(12, -5).toString()));

  }

  @Test
  public void evalComplexCase3() throws SyntaxException {
    assertThat(new Symbols().evalComplex("sign(2i)").toString(), is(new Complex(0, 1).toString()));
    assertThat(new Symbols().evalComplex("sign(nan)").toString(), is(new Complex(Double.NaN, 0).toString()));
    assertThat(new Symbols().evalComplex("sign(nan i)").toString(), is(new Complex(Double.NaN, 0).toString()));
    assertThat(new Symbols().evalComplex("sign(0)").toString(), is(new Complex(0, 0).toString()));
  }

  @Test
  public void evalComplexCase4() throws SyntaxException {
    assertThat(new Symbols().evalComplex("real(8.123)").toString(), is(new Complex(8.123, 0).toString()));
    assertThat(new Symbols().evalComplex("imag(8.123)").toString(), is(new Complex(0, 0).toString()));
    assertThat(new Symbols().evalComplex("real(1+3i)").toString(), is(new Complex(1, 0).toString()));
    assertThat(new Symbols().evalComplex("imag(1+3i)").toString(), is(new Complex(3, 0).toString()));
    assertThat(new Symbols().evalComplex("re(1+3i)").toString(), is(new Complex(1, 0).toString()));
    assertThat(new Symbols().evalComplex("im(1+3i)").toString(), is(new Complex(3, 0).toString()));
  }

}
