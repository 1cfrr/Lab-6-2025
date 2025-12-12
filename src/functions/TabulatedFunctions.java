package functions;

import java.io.*;

public final class TabulatedFunctions {
    public static final double EPSILON = 1e-10;

    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создать экземпляр класса TabulatedFunctions");
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if(leftX - rightX > - EPSILON){
            throw new IllegalArgumentException("Левая граница диапазона больше или равна правой");
        }
        else if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество предлагаемых точек меньше двух");
        }
        else if (leftX - function.getLeftDomainBorder() < -EPSILON || rightX - function.getRightDomainBorder() > EPSILON){
            throw new IllegalArgumentException("Указанные границы для табулирования выходят за область определения функции");
        }

        double step = (rightX - leftX)/(pointsCount - 1);
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i =0; i < pointsCount; i++){
            points[i] = new FunctionPoint(leftX + i*step, function.getFunctionValue(leftX+i*step));
        }
        return new ArrayTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(function.getPointsCount());

        for (int i = 0; i < function.getPointsCount(); i++){
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();

        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++){
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i]=new FunctionPoint(x,y);
        }
        return new ArrayTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException{
        PrintWriter dataOut = new PrintWriter(out);

        dataOut.print(function.getPointsCount());
        dataOut.print(" ");

        for (int i = 0; i < function.getPointsCount(); i++){
            dataOut.print(function.getPointX(i));
            dataOut.print(" ");
            dataOut.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1) {
                dataOut.print(" ");
            }
        }

        dataOut.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        tokenizer.parseNumbers();

        if(tokenizer.nextToken() != StreamTokenizer.TT_NUMBER){
            throw new IOException("Ожидалось число (количество точек)");
        }
        int pointCount = (int) tokenizer.nval;

        FunctionPoint[] points = new FunctionPoint[pointCount];
        for (int i = 0; i < pointCount; i++){
            if(tokenizer.nextToken() != StreamTokenizer.TT_NUMBER){
                throw new IOException("Ожидалось число (координата x)");
            }
            double x = tokenizer.nval;
            if(tokenizer.nextToken() != StreamTokenizer.TT_NUMBER){
                throw new IOException("Ожидалось число (координата Y)");
            }
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x,y);
        }
        return new ArrayTabulatedFunction(points);
    }
}
