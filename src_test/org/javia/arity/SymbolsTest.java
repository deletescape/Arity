package org.javia.arity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SymbolsTest {

  @Test
  public void case1() throws SyntaxException {
    Symbols symbols = new Symbols();
    symbols.define("a", 1);
    assertThat(symbols.eval("a"), is(1d));

    symbols.pushFrame();
    assertThat(symbols.eval("a"), is(1d));
    symbols.define("a", 2);
    assertThat(symbols.eval("a"), is(2d));
    symbols.define("a", 3);
    assertThat(symbols.eval("a"), is(3d));

    symbols.popFrame();
    assertThat(symbols.eval("a"), is(1d));
  }

  @Test
  public void case2() throws SyntaxException {
    Symbols symbols = new Symbols();
    symbols.pushFrame();
    symbols.add(Symbol.makeArg("base", 0));
    symbols.add(Symbol.makeArg("x", 1));
    assertThat(symbols.lookupConst("x").op, is(VM.LOAD1));
    symbols.pushFrame();
    assertThat(symbols.lookupConst("base").op, is(VM.LOAD0));
    assertThat(symbols.lookupConst("x").op, is(VM.LOAD1));
    symbols.popFrame();
    assertThat(symbols.lookupConst("base").op, is(VM.LOAD0));
    assertThat(symbols.lookupConst("x").op, is(VM.LOAD1));
    symbols.popFrame();
    assertThat(symbols.lookupConst("x").op, is(VM.LOAD0));
  }

  @Test
  public void testRecursiveEval() throws SyntaxException {
    Symbols symbols = new Symbols();
    symbols.define("myfun", new MyFun());
    Function f = symbols.compile("1+myfun(x)");
    assertThat(f.eval(0), is(2d));
    assertThat(f.eval(1), is(1d));
    assertThat(f.eval(2), is(0d));
    assertThat(f.eval(3), is(-1d));
  }

  class MyFun extends Function {
    Symbols symbols = new Symbols();
    Function f;

    MyFun() {
      try {
        f = symbols.compile("1-x");
      } catch (SyntaxException e) {
        System.out.println("" + e);
      }
    }

    @Override
    public double eval(double x) {
      return f.eval(x);
    }

    @Override
    public int arity() {
      return 1;
    }
  }
}
