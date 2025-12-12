package functions;
import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable{
    public static final double EPSILON = 1e-10;
    private FunctionPoint[] points;
    private int pointsCount;

    public ArrayTabulatedFunction() {
        this.points = new FunctionPoint[0];
        this.pointsCount = 0;
    }

    public ArrayTabulatedFunction( FunctionPoint[] FPs){
        if (FPs.length < 2) {
            throw new IllegalArgumentException("Количество предлагаемых точек меньше двух");
        }

        for (int i = 0; i<FPs.length-1;i++){
            if (FPs[i].getX()-FPs[i+1].getX()>EPSILON){
                throw new IllegalArgumentException("Точки в массиве не упорядочены по значению абсциссы");
            }
        }
        this.pointsCount = FPs.length;
        this.points = new FunctionPoint[pointsCount];

        for (int i =0; i < FPs.length; i++){
            points[i]=new FunctionPoint(FPs[i]);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if(leftX - rightX > - EPSILON){
            throw new IllegalArgumentException("Левая граница диапазона больше или равна правой");
        }
        else if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество предлагаемых точек меньше двух");
        }
        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount];

        double step = (rightX - leftX)/(pointsCount - 1);
        for (int i =0; i < pointsCount; i++){
            points[i]=new FunctionPoint(leftX + i * step,0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        if(leftX - rightX > - EPSILON){
            throw new IllegalArgumentException("Левая граница диапазона больше или равна правой");
        }
        else if (values.length<2) {
            throw new IllegalArgumentException("Количество предлагаемых точек меньше двух");
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount];

        double step = (rightX-leftX)/(pointsCount-1);
        for (int i =0; i<pointsCount; i++){
            points[i]=new FunctionPoint(leftX+i*step,values[i]);
        }
    }

    public double getLeftDomainBorder(){
        return points[0].getX();
    }

    public double getRightDomainBorder(){
        return points[pointsCount-1].getX();
    }

    public double getFunctionValue(double x){
        double LX ,RX;
        double LY ,RY;
        for (int i = 0; i<pointsCount-1;i++){
            if(Math.abs(x - points[i].getX())<EPSILON){
                return points[i].getY();
            }
            LX= points[i].getX();
            RX= points[i+1].getX();
            LY=points[i].getY();
            RY= points[i+1].getY();
            if(LX - x < -EPSILON && RX - x > EPSILON){
                return ((x-LX)*(RY-LY))/(RX-LX)+LY;
            }
        }
        if(Math.abs(x-points[pointsCount-1].getX())<EPSILON)
            return points[pointsCount-1].getY();
        return Double.NaN;
    }

    public int getPointsCount(){
        return pointsCount;
    }

    public FunctionPoint getPoint(int index){
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        return new FunctionPoint(points[index].getX(),points[index].getY());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        else if(index == 0){
            if(point.getX() - points[1].getX() > -EPSILON){
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
        }
        else if(index == pointsCount-1){
            if(point.getX() - points[pointsCount-2].getX() < EPSILON){
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
        }
        else if (points[index-1].getX() - point.getX() > -EPSILON || points[index + 1].getX() - point.getX() < EPSILON) {
            throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
        }
        points[index]= new FunctionPoint(point.getX(),point.getY());
    }

    public double getPointX(int index){
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException{
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        else if(index == 0){
            if(x - points[1].getX() > -EPSILON){
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
        }
        else if(index == pointsCount-1){
            if(x - points[pointsCount-2].getX() < EPSILON){
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
        }
        else if (points[index - 1].getX() - x >-EPSILON|| x - points[index + 1].getX() < EPSILON){
            throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
        }
        points[index].setX(x);
    }

    public double getPointY(int index){
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y){
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        points[index].setY(y);
    }

    public void deletePoint(int index){
        if(pointsCount<3){
            throw new IllegalStateException("В массиве находятся только две точки");
        }
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        System.arraycopy(points,index+1,points,index,pointsCount-index-1);
        points[pointsCount-1]=null;
        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        for (int i = 1; i < pointsCount; i++) {
            if (point.getX() - points[i].getX() < -EPSILON && point.getX() - points[i - 1].getX() > EPSILON) {
                FunctionPoint[] newPoints = new FunctionPoint[pointsCount + 1];
                System.arraycopy(points, 0, newPoints, 0, i);
                newPoints[i] = point;
                System.arraycopy(points, i, newPoints, i + 1, pointsCount - i);
                points = newPoints;
                i = pointsCount;
                pointsCount+=1;
            }
            else if(Math.abs(point.getX()-points[i].getX())<EPSILON||Math.abs(point.getX()-points[i-1].getX())<EPSILON){
                throw new InappropriateFunctionPointException("В наборе точек функции есть точка, абсцисса которой совпадает с абсциссой добавляемой точки");
            }
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            sb.append(points[i].toString());
            if (i < pointsCount - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object o){
        if (o == this) return true;

        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction other = (ArrayTabulatedFunction) o;

            if (this.pointsCount != other.pointsCount) {
                return false;
            }

            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(other.points[i])) {
                    return false;
                }
            }
            return true;
        }

        if (!(o instanceof TabulatedFunction)) {
            return false;
        }

        TabulatedFunction other = (TabulatedFunction) o;
        if (this.pointsCount != other.getPointsCount()) {
            return false;
        }

        for (int i = 0; i < pointsCount; i++) {
            if (!this.getPoint(i).equals(other.getPoint(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = pointsCount;

        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }

        return hash;
    }

    public Object clone(){
        FunctionPoint[] ClonePoints = new FunctionPoint[pointsCount];

        for(int i = 0; i < pointsCount; i++){
            ClonePoints[i] = (FunctionPoint) points[i].clone();
        }

        return new ArrayTabulatedFunction(ClonePoints);
    }
}
