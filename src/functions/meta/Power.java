package functions.meta;
import functions.Function;

public class Power implements Function {
    private Function fun;
    private double pow;

    public  Power (Function fun, double pow){
        this.fun=fun;
        this.pow=pow;
    }

    public double getLeftDomainBorder() {
        return fun.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return fun.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        if(Double.isNaN(fun.getFunctionValue(x))){
            return Double.NaN;
        }
        return Math.pow(fun.getFunctionValue(x),pow);
    }
}