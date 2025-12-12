package functions.meta;
import functions.Function;

public class Shift implements Function {
    private Function fun;
    private double shiftX, shiftY;

    public  Shift (Function fun, double shiftX, double shiftY){
        this.fun=fun;
        this.shiftX= shiftX;
        this.shiftY= shiftY;
    }

    public double getLeftDomainBorder() {
        return fun.getLeftDomainBorder() + shiftX;
    }

    public double getRightDomainBorder() {
        return fun.getRightDomainBorder() + shiftX;
    }

    public double getFunctionValue(double x) {
        if(Double.isNaN(fun.getFunctionValue(x - shiftX))){
            return Double.NaN;
        }
        return fun.getFunctionValue(x - shiftX) + shiftY;
    }
}
