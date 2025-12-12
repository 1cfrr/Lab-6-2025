package functions.meta;
import functions.Function;

public class Composition implements Function {
    private Function fun1, fun2;
    public  Composition (Function fun1, Function fun2){
        this.fun1=fun1;
        this.fun2=fun2;
    }

    public double getLeftDomainBorder() {
        return fun2.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return fun2.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        double valueF2 = fun2.getFunctionValue(x);
        if(Double.isNaN(valueF2)|| valueF2 - fun1.getLeftDomainBorder() < -1e-10 || valueF2 - fun1.getRightDomainBorder() > 1e-10){
            return Double.NaN;
        }
        return fun1.getFunctionValue(fun2.getFunctionValue(x));
    }
}
