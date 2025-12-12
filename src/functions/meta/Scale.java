package functions.meta;
import functions.Function;

public class Scale implements Function {
    private Function fun;
    private double scaleX, scaleY;

    public  Scale (Function fun, double scaleX, double scaleY){
        this.fun=fun;
        this.scaleX=scaleX;
        this.scaleY=scaleY;
    }

    public double getLeftDomainBorder() {
        if (scaleX >= 0){
            return fun.getLeftDomainBorder()*scaleX;
        }
        return fun.getRightDomainBorder()*scaleX;
    }

    public double getRightDomainBorder() {
        if (scaleX >= 0){
            return fun.getRightDomainBorder()*scaleX;
        }
        return fun.getLeftDomainBorder()*scaleX;
    }

    public double getFunctionValue(double x) {
        if(Double.isNaN(fun.getFunctionValue(x/scaleX))){
            return Double.NaN;
        }
        return fun.getFunctionValue(x/scaleX)*scaleY;
    }
}
