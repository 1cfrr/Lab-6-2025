package functions;
import java.io.*;

public class FunctionPoint implements Externalizable{
    private double x;
    private double y;

    public FunctionPoint(double x, double y){
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public void setX(double value) {
        this.x = value;
    }
    public void setY(double value){
        this.y=value;
    }

    public FunctionPoint(FunctionPoint point){
        this(point.getX(), point.getY());
    }

    public FunctionPoint(){
        this(0,0);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.readDouble();
        y = in.readDouble();
    }

    public String toString(){
        return  String.format("(%.2f; %.2f)",this.x, this.y);
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FunctionPoint FFo = (FunctionPoint) o;

        return (Math.abs(this.x-FFo.getX()) < 1e-10) && (Math.abs(this.y-((FunctionPoint) o).getY()) < 1e-10);
    }

    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        int xHash = (int) (xBits ^ (xBits >>> 32));
        int yHash = (int) (yBits ^ (yBits >>> 32));

        return xHash ^ yHash;
    }

    public Object clone(){
        return new FunctionPoint(this.x, this.y);
    }
}
