/*
 * Copyright (C) 2007-2009 Mihai Preda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.javia.arity;

class EvalCase {
    String expr;
    double result;
    Complex cResult;
    
    static final double ERR = -2, FUN = -3;

    EvalCase(String expr, double result) {
        this.expr = expr;
        this.result = result;
    }

    EvalCase(String expr, Complex result) {
        this.expr = expr;
        this.cResult = result;
    }
}

class SizeCase {
    public SizeCase(int size, String v, String s) {
        this.size = size;
        this.val = v;
        this.res = s;
    }
    
    public int size;
    public String val;
    public String res;
}

/**
   Runs unit-tests.<p>
   Usage: java -jar arity.jar
*/
public class UnitTest {
    /**
       Takes a single command-line argument, an expression; compiles and prints it.<p>
       Without arguments, runs the unit tests.
       @throws SyntaxException if there are errors compiling the expression.
     */
    public static void main(String argv[]) throws SyntaxException, ArityException {
        int size = argv.length;
        if (size == 0) {
            runUnitTests();
            //profile();
        } else if (argv[0].equals("-profile")) {
            if (size == 1) {
                profile();
            } else {
                Symbols symbols = new Symbols();
                for (int i = 1; i < size - 1; ++i) {
                    FunctionAndName fan = symbols.compileWithName(argv[i]);
                    symbols.define(fan);
                }
                profile(symbols, argv[size-1]);
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
        System.out.println("execution time: " + (delta > 100 ? ""+delta/100.+" us" :  ""+delta+" ns"));
    }

    private static final String profileCases[] = {
        //"1+1",
        "(100.5 + 20009.999)*(7+4+3)/(5/2)^3!)*2",
        "fun1(x)=(x+2)*(x+3)",
        "otherFun(x)=(fun1(x-1)*x+1)*(fun1(2-x)+10)",
        "log(x+30.5, 3)^.7*sin(x+.5)"
    };

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

    static void runUnitTests() {
        checkCounter = 0;
        
        check(Math.log(-1), Double.NaN);
        check(Math.log(-0.03), Double.NaN);
        check(MoreMath.intLog10(-0.03), 0);
        check(MoreMath.intLog10(0.03), -2);
        check(MoreMath.intExp10(3), 1000);
        check(MoreMath.intExp10(-1), 0.1);

        check(Util.shortApprox( 1.235, 0.02),  1.24);
        check(Util.shortApprox( 1.235, 0.4),   1.2000000000000002);
        check(Util.shortApprox(-1.235, 0.02), -1.24);
        check(Util.shortApprox(-1.235, 0.4),  -1.2000000000000002);

        if (!allOk) {
            System.out.println("\n*** Some tests FAILED ***\n");
            System.exit(1);
        } else {
            System.out.println("\n*** All tests passed OK ***\n");
        }
    }

    static boolean equal(Complex a, Complex b) {
        return equal(a.re, b.re) && equal(a.im, b.im);
    }

    static boolean equal(double a, Complex c) {
        return equal(a, c.re) 
            && (equal(0, c.im) ||
                Double.isNaN(a) && Double.isNaN(c.im));
    }

    static boolean equal(double a, double b) {
        return a == b || 
            (Double.isNaN(a) && Double.isNaN(b)) ||
            Math.abs((a - b) / b) < 1E-15 || Math.abs(a - b) < 1E-15;
    }

    static void check(double v1, double v2) {
        ++checkCounter;
        if (!equal(v1, v2)) {
            allOk = false;
            System.out.println("failed check #" + checkCounter + ": expected " + v2 + " got " + v1);
        }
    }

    static void check(Complex v1, Complex v2) {
        ++checkCounter;
        if (!(equal(v1.re, v2.re) && equal(v1.im, v2.im))) {
            allOk = false;
            System.out.println("failed check #" + checkCounter + ": expected " + v2 + " got " + v1);
        }
    }
    
    static void check(boolean cond) {
        ++checkCounter;
        if (!cond) {
            allOk = false;
            //Log.log("check " + checkCounter + " failed");
        }
    }

    static boolean allOk = true;
    static int checkCounter = 0;
}