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

        check(Util.doubleToString(Double.NEGATIVE_INFINITY, 5).equals("-Infinity"));
        check(Util.doubleToString(Double.NaN, 5).equals("NaN"));
        
        Complex c = new Complex();
        Complex d = new Complex();
        Complex e = new Complex();

        check(Util.complexToString(c.set(0, -1), 10, 1).equals("-i"));
        check(Util.complexToString(c.set(2.123, 0), 3, 0).equals("2.1"));
        check(Util.complexToString(c.set(0, 1.0000000000001), 20, 3).equals("i"));
        check(Util.complexToString(c.set(1, -1), 10, 1).equals("1-i"));
        check(Util.complexToString(c.set(1, 1), 10, 1).equals("1+i"));
        check(Util.complexToString(c.set(1.12, 1.12), 9, 0).equals("1.12+1.1i"));
        check(Util.complexToString(c.set(1.12345, -1), 7, 0).equals("1.123-i"));
        
        check(c.set(-1, 0).pow(d.set(0, 1)), e.set(0.04321391826377226, 0));
        check(c.set(-1, 0).pow(d.set(1, 1)), e.set(-0.04321391826377226, 0));

        check(c.set(-1, 0).abs(), 1);
        check(c.set(Math.E * Math.E, 0).log(), d.set(2, 0));
        check(c.set(-1, 0).log(),              d.set(0, Math.PI));

        check(c.set(2, 0).exp(),       d.set(Math.E*Math.E, 0));
        check(c.set(0, Math.PI).exp(), d.set(-1, 0));
        
        check(MoreMath.lgamma(1), 0);
        check(c.set(1, 0).lgamma(), d.set(0, 0));

        check(c.set(0, 0).factorial(), d.set(1, 0));
        check(c.set(1, 0).factorial(), d.set(1, 0));        
        check(c.set(0, 1).factorial(), d.set(0.49801566811835596, -0.1549498283018106));
        check(c.set(-2, 1).factorial(), d.set(-0.17153291990834815, 0.32648274821006623));
        check(c.set(4, 0).factorial(), d.set(24, 0));
        check(c.set(4, 3).factorial(), d.set(0.016041882741649555, -9.433293289755953));

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

        check(testRecursiveEval());

        check(testFrame());

        if (!allOk) {
            System.out.println("\n*** Some tests FAILED ***\n");
            System.exit(1);
        } else {
            System.out.println("\n*** All tests passed OK ***\n");
        }
    }

    static boolean testFrame() {
        boolean ok = true;
        try {
            Symbols symbols = new Symbols();
            symbols.define("a", 1);
            ok = ok && symbols.eval("a") == 1;
        
            symbols.pushFrame();
            ok = ok && symbols.eval("a") == 1;
            symbols.define("a", 2);
            ok = ok && symbols.eval("a") == 2;
            symbols.define("a", 3);
            ok = ok && symbols.eval("a") == 3;            

            symbols.popFrame();
            ok = ok && symbols.eval("a") == 1;
            
            // ----

            symbols = new Symbols();
            symbols.pushFrame();
            symbols.add(Symbol.makeArg("base", 0));
            symbols.add(Symbol.makeArg("x", 1));
            ok = ok && symbols.lookupConst("x").op == VM.LOAD1;
            symbols.pushFrame();
            ok = ok && symbols.lookupConst("base").op == VM.LOAD0;
            ok = ok && symbols.lookupConst("x").op == VM.LOAD1;
            symbols.popFrame();
            ok = ok && symbols.lookupConst("base").op == VM.LOAD0;
            ok = ok && symbols.lookupConst("x").op == VM.LOAD1;
            symbols.popFrame();
            ok = ok && symbols.lookupConst("x").op == VM.LOAD0;            
        } catch (SyntaxException e) {
            return false;
        }
        return ok;
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

    static boolean testRecursiveEval() {
        Symbols symbols = new Symbols();
        symbols.define("myfun", new MyFun());
        try {
            Function f = symbols.compile("1+myfun(x)");
            return 
                f.eval(0) == 2 && 
                f.eval(1) == 1 && 
                f.eval(2) == 0 && 
                f.eval(3) == -1;
        } catch (SyntaxException e) {
            System.out.println("" + e);
            allOk = false;
            return false;
        }
    }
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

    public double eval(double x) {
        return f.eval(x);
    }

    public int arity() {
        return 1;
    }
}
