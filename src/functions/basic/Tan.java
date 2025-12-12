package functions.basic;

public class Tan extends TrigonometricFunction {
    public double getFunctionValue (double value){
        if (Math.abs( Math.cos(value))<1e-10)
            return Double.NaN;
        return Math.tan(value);
    }
}
