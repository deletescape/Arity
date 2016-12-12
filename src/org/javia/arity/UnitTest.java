/*
 * Copyright (C) 2007-2009 Mihai Preda.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.javia.arity;

/**
 * Runs unit-tests.
 * <p>
 * Usage: java -jar arity.jar
 */
public class UnitTest {
  /**
   * Takes a single command-line argument, an expression; compiles and prints it.
   * <p>
   * Without arguments, runs the unit tests.
   * 
   * @throws SyntaxException
   *           if there are errors compiling the expression.
   */
  public static void main(String argv[]) throws SyntaxException, ArityException {
    int size = argv.length;
    if (size == 0) {
      System.out.println("Unit testing implementation has been removed / converted to JUnit tests in this version!");
    } else if (argv[0].equals("-profile")) {
      if (size == 1) {
        profile();
      } else {
        Symbols symbols = new Symbols();
        for (int i = 1; i < size - 1; ++i) {
          FunctionAndName fan = symbols.compileWithName(argv[i]);
          symbols.define(fan);
        }
        profile(symbols, argv[size - 1]);
      }
    } else {
      Symbols symbols = new Symbols();
      for (int i = 0; i < size; ++i) {
        FunctionAndName fan = symbols.compileWithName(argv[i]);
        symbols.define(fan);
        Function f = fan.function;
        System.out.println(argv[i] + " : " + f);
      }
    }
  }

  static void profile(Symbols symbols, String str) throws SyntaxException, ArityException {
    Function f = symbols.compile(str);
    System.out.println("\n" + str + ": " + f);

    long t1 = System.currentTimeMillis();
    for (int i = 0; i < 1000; ++i) {
      symbols.compile(str);
    }
    long t2 = System.currentTimeMillis();
    System.out.println("compilation time: " + (t2 - t1) + " us");

    double args[] = new double[f.arity()];
    t1 = System.currentTimeMillis();
    for (int i = 0; i < 100000; ++i) {
      f.eval(args);
    }
    t2 = System.currentTimeMillis();
    long delta = t2 - t1;
    System.out.println("execution time: " + (delta > 100 ? "" + delta / 100. + " us" : "" + delta + " ns"));
  }

  private static final String profileCases[] = { "(100.5 + 20009.999)*(7+4+3)/(5/2)^3!)*2", "fun1(x)=(x+2)*(x+3)",
      "otherFun(x)=(fun1(x-1)*x+1)*(fun1(2-x)+10)", "log(x+30.5, 3)^.7*sin(x+.5)" };

  private static void profile() {
    String cases[] = profileCases;
    Symbols symbols = new Symbols();
    try {
      for (int i = 0; i < cases.length; ++i) {
        symbols.define(symbols.compileWithName(cases[i]));
        profile(symbols, cases[i]);
      }
    } catch (SyntaxException e) {
      throw new Error("" + e);
    }
  }
}
