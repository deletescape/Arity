package org.javia.arity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FormatTest {

  @Test
  public void formatCase1() {
    assertThat(Util.doubleToString(0.1, 0), is("0.1"));
    assertThat(Util.doubleToString(0.12, 0), is("0.12"));
    assertThat(Util.doubleToString(0.001, 0), is("0.001"));
    assertThat(Util.doubleToString(0.0012, 0), is("0.0012"));
    assertThat(Util.doubleToString(0.0000001, 0), is("1E-7"));
    assertThat(Util.doubleToString(0.00000012, 0), is("1.2E-7"));
    assertThat(Util.doubleToString(0.123456789012345, 0), is("0.123456789012345"));
  }

  @Test
  public void formatCase2() {
    assertThat(Util.doubleToString(0, 0), is("0"));
    assertThat(Util.doubleToString(1, 0), is("1"));
    assertThat(Util.doubleToString(12, 0), is("12"));
    assertThat(Util.doubleToString(1234567890., 0), is("1234567890"));
    assertThat(Util.doubleToString(1000000000., 0), is("1000000000"));
  }

  @Test
  public void formatCase3() {
    assertThat(Util.doubleToString(1.23456789012345, 0), is("1.23456789012345"));
    assertThat(Util.doubleToString(12345.6789012345, 0), is("12345.6789012345"));
    assertThat(Util.doubleToString(1234567890.12345, 0), is("1234567890.12345"));
    assertThat(Util.doubleToString(123456789012345., 0), is("1.23456789012345E14"));
    assertThat(Util.doubleToString(100000000000000., 0), is("1E14"));
    assertThat(Util.doubleToString(120000000000000., 0), is("1.2E14"));
    assertThat(Util.doubleToString(100000000000001., 0), is("1.00000000000001E14"));
  }

  @Test
  public void formatCase4() {
    assertThat(Util.doubleToString(0.1, 2), is("0.1"));
    assertThat(Util.doubleToString(0.00000012, 2), is("1.2E-7"));
    assertThat(Util.doubleToString(0.123456789012345, 2), is("0.12345678901235"));
  }

  @Test
  public void formatCase5() {
    assertThat(Util.doubleToString(0, 2), is("0"));
  }

  @Test
  public void formatCase6() {
    assertThat(Util.doubleToString(1.23456789012345, 2), is("1.2345678901235"));
    assertThat(Util.doubleToString(1.23456789012345, 3), is("1.234567890123"));
  }

  @Test
  public void formatCase7() {
    assertThat(Util.doubleToString(12345.6789012345, 0), is("12345.6789012345"));
    assertThat(Util.doubleToString(1234567890.12345, 2), is("1234567890.1235"));
    assertThat(Util.doubleToString(123456789012345., 3), is("1.234567890123E14"));
    assertThat(Util.doubleToString(100000000000001., 2), is("1E14"));
  }

  @Test
  public void formatCase8() {
    assertThat(Util.doubleToString(12345678901234567., 0), is("1.2345678901234568E16"));
    assertThat(Util.doubleToString(12345678901234567., 2), is("1.2345678901235E16"));
  }

  @Test
  public void formatCase9() {
    assertThat(Util.doubleToString(99999999999999999., 0), is("1E17"));
    assertThat(Util.doubleToString(9999999999999999., 0), is("1E16"));
    assertThat(Util.doubleToString(999999999999999., 0), is("9.99999999999999E14"));
    assertThat(Util.doubleToString(999999999999999., 2), is("1E15"));
    assertThat(Util.doubleToString(999999999999994., 2), is("9.9999999999999E14"));
  }

  @Test
  public void formatCase10() {
    assertThat(Util.doubleToString(MoreMath.log2(1 + .00002), 2), is("0.000028853612282487"));
    assertThat(Util.doubleToString(4E-4, 0), is("0.0004"));
    assertThat(Util.doubleToString(1e30, 0), is("1E30"));
  }

  @Test
  public void formatComplex() {
    assertThat(Util.complexToString(new Complex(0, -1), 10, 1), is("-i"));
    assertThat(Util.complexToString(new Complex(2.123, 0), 3, 0), is("2.1"));
    assertThat(Util.complexToString(new Complex(0, 1.0000000000001), 20, 3), is("i"));
    assertThat(Util.complexToString(new Complex(1, -1), 10, 1), is("1-i"));
    assertThat(Util.complexToString(new Complex(1, 1), 10, 1), is("1+i"));
    assertThat(Util.complexToString(new Complex(1.12, 1.12), 9, 0), is("1.12+1.1i"));
    assertThat(Util.complexToString(new Complex(1.12345, -1), 7, 0), is("1.123-i"));
  }

  @Test
  public void sizeCase1() {
    assertThat(Util.sizeTruncate("1111111110", 9), is("1.11111E9"));
    assertThat(Util.sizeTruncate("1111111110", 10), is("1111111110"));
    assertThat(Util.sizeTruncate("11111111110", 10), is("1.11111E10"));
    assertThat(Util.sizeTruncate("12.11111E9", 10), is("12.11111E9"));
    assertThat(Util.sizeTruncate("12.34567E9", 9), is("12.3456E9"));
    assertThat(Util.sizeTruncate("12345678E3", 9), is("1.2345E10"));
    assertThat(Util.sizeTruncate("-12345678E3", 9), is("-1.234E10"));
  }

  @Test
  public void sizeCase2() {
    assertThat(Util.sizeTruncate("-0.00000007", 9), is("-0.000000"));
  }

  @Test
  public void sizeCase3() {
    assertThat(Util.sizeTruncate("-1.23E123", 5), is("-1.23E123"));
    assertThat(Util.sizeTruncate("-1.2E123", 5), is("-1.2E123"));
    assertThat(Util.sizeTruncate("-1E123", 5), is("-1E123"));
    assertThat(Util.sizeTruncate("-1", 2), is("-1"));
    assertThat(Util.sizeTruncate("-1", 1), is("-1"));
    assertThat(Util.sizeTruncate("-0.02", 1), is("-0.02"));
    assertThat(Util.sizeTruncate("0.02", 1), is("0"));
  }

}
