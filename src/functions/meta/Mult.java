package functions.meta;
import functions.Function;

public class Mult implements Function {
    private Function fun1, fun2;

    public  Mult (Function fun1, Function fun2){
        this.fun1=fun1;
        this.fun2=fun2;
    }

    public double getLeftDomainBorder() {
        return Math.min(fun1.getLeftDomainBorder(),fun2.getLeftDomainBorder());
    }

    public double getRightDomainBorder() {
        return Math.max(fun1.getRightDomainBorder(),fun2.getRightDomainBorder());
    }

    public double getFunctionValue(double x) {
        if(x - getLeftDomainBorder() < -1e-10 || x - getRightDomainBorder() > 1e-10){
            return Double.NaN;
        }
        return fun1.getFunctionValue(x) * fun2.getFunctionValue(x);
    }
}
