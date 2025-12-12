package functions;
import functions.meta.*;

public final class Functions {

    private Functions(){
        throw new AssertionError("Нельзя создать экземпляр класса Functions");
    }

    public static Function shift(Function f, double shiftX, double shiftY){
        return new Shift(f,shiftX,shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY){
        return new Scale(f,scaleX,scaleY);
    }

    public static Function sum(Function f1,Function f2 ){
        return new Sum(f1,f2);
    }

    public static Function mult(Function f1,Function f2){
        return new Mult(f1,f2);
    }

    public static Function power(Function f, double pow){
        return new Power(f,pow);
    }

    public static Function composition (Function f1,Function f2){
        return new Composition(f1,f2);
    }

    public static double integrate (Function function, double leftX, double rightX, double step){
        if (function.getLeftDomainBorder() - leftX > -1e-10||function.getRightDomainBorder() - rightX < 1e-10){
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
        }
        if (leftX - rightX > - 1e-10){
            throw new IllegalArgumentException("Левая граница диапазона больше или равна правой");
        }

        if(step<=0){
            throw new IllegalArgumentException("Шаг дискретизации должен быть больше нуля");
        }
        double integral = 0.0;
        double current = leftX;

        while (current < rightX) {
            double next = Math.min(current + step, rightX);
            double f_current = function.getFunctionValue(current);
            double f_next = function.getFunctionValue(next);

            double trapezoidArea = (f_current + f_next) * (next - current) / 2.0;
            integral += trapezoidArea;

            current = next;
        }

        return integral;
    }
}
