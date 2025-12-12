package functions.basic;
import functions.Function;

public class Log implements Function{
    private double base;

    public Log(double intBase){
        if(intBase<=0||Math.sqrt(intBase-1)<1e-10){
            throw new IllegalArgumentException("Основание логарифма должно быть больше нуля и не должно ровняться единице");
        }
        this.base=intBase;
    }

    public double getFunctionValue(double value){
        if (value<=0)
            return Double.NaN;
        return Math.log(value)/Math.log(base);
    }

    public double getLeftDomainBorder(){
        return 0;
    }
    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY;
    }
}
