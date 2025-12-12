package functions;
import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable{
    public static final double EPSILON = 1e-10;
    private FunctionNode head;
    private int pointsCount;
    private FunctionNode lastNode;
    private int lastIndex;

    private static class FunctionNode {
        FunctionPoint point;
        FunctionNode previous;
        FunctionNode next;

        FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
            this.point = point;
            this.previous = prev;
            this.next = next;
        }
        FunctionNode(FunctionPoint point) {
            this.point = point;
            this.previous = null;
            this.next = null;
        }
    }

    private FunctionNode getNodeByIndex(int index){
        FunctionNode node;
        int startIndex;

        if(lastIndex != -1 && Math.abs(index - lastIndex) < index && pointsCount - 1 - index > Math.abs(index - lastIndex)) {
            node = lastNode;
            startIndex=lastIndex;
        }
        else if (Math.abs(index - lastIndex) < index){
            node=head.next;
            startIndex=0;
        }
        else{
            node=head.previous;
            startIndex=pointsCount-1;
        }

        if(index<=startIndex){
            for(int i = startIndex; i>index; i--){
                node = node.previous;
            }
        }
        else{
            for(int i = startIndex; i<index; i++){
                node = node.next;
            }
        }
        lastNode = node;
        lastIndex = index;
        return node;
    }

    private FunctionNode addNodeToTail(){
        FunctionNode newNode = new FunctionNode(null,head.previous,head);
        head.previous.next = newNode;
        head.previous=newNode;
        pointsCount++;
        lastNode = newNode;
        lastIndex = pointsCount - 1;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index){
        FunctionNode node = (index == pointsCount) ? head : getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(null,node.previous,node);
        node.previous.next=newNode;
        node.previous=newNode;

        pointsCount++;
        lastIndex=index;
        lastNode=newNode;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index){
        FunctionNode nodeDel = getNodeByIndex(index);
        nodeDel.previous.next= nodeDel.next;
        nodeDel.next.previous= nodeDel.previous;
        pointsCount--;
        if (lastIndex == index) {
            lastNode = (index == pointsCount) ? head.previous : nodeDel.next;
            lastIndex = (index == pointsCount) ? pointsCount - 1 : index;
        } else if (lastIndex > index) {
            lastIndex--;
        }
        return nodeDel;
    }

    private void initializeList() {
        head = new FunctionNode(null);
        head.previous = head;
        head.next = head;
        pointsCount = 0;
        lastNode = head;
        lastIndex = -1;
    }

    public LinkedListTabulatedFunction() {
        initializeList();
    }

    public LinkedListTabulatedFunction(FunctionPoint[] FPs){
        if (FPs.length < 2) {
            throw new IllegalArgumentException("Количество предлагаемых точек меньше двух");
        }

        for (int i = 0; i<FPs.length-1;i++){
            if (FPs[i].getX()-FPs[i+1].getX()>EPSILON){
                throw new IllegalArgumentException("Точки в массиве не упорядочены по значению абсциссы");
            }
        }

        initializeList();
        for (FunctionPoint fp : FPs) {
            addNodeToTail().point = new FunctionPoint(fp);
        }
    }

    public LinkedListTabulatedFunction(double leftX,double rightX, int pointsCount){
        if(leftX - rightX > - EPSILON){
            throw new IllegalArgumentException("Левая граница диапазона больше или равна правой");
        }
        else if (pointsCount<2) {
            throw new IllegalArgumentException("Количество предлагаемых точек меньше двух");
        }

        initializeList();
        double step = (rightX-leftX)/(pointsCount-1);
        for (int i =0; i<pointsCount; i++){
            addNodeToTail().point= new FunctionPoint(leftX+i*step,0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values){
        if(leftX - rightX > - EPSILON){
            throw new IllegalArgumentException("Левая граница диапазона больше или равна правой");
        }
        else if (values.length < 2) {
            throw new IllegalArgumentException("Количество предлагаемых точек меньше двух");
        }

        initializeList();
        double step = (rightX-leftX)/(values.length-1);
        for (int i =0; i<values.length; i++){
            addNodeToTail().point= new FunctionPoint(leftX+i*step,values[i]);
        }
    }

    public double getLeftDomainBorder(){
        return head.next.point.getX();
    }

    public double getRightDomainBorder(){
        return head.previous.point.getX();
    }

    public double getFunctionValue(double x) {
        FunctionNode node = head.next;
        while (node != head) {
            if (Math.abs(x - node.point.getX()) < EPSILON) {
                return node.point.getY();
            }
            else if (node.next != head && node.point.getX() - x < -EPSILON && node.next.point.getX() - x > EPSILON) {
                double leftX = node.point.getX();
                double rightX = node.next.point.getX();
                double leftY = node.point.getY();
                double rightY = node.next.point.getY();
                return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
            }
            node = node.next;
        }
        return Double.NaN;
    }

    public int getPointsCount(){
        return pointsCount;
    }

    public FunctionPoint getPoint(int index){
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }

        else if (index == 0){
            if (point.getX() - getNodeByIndex(1).point.getX() > -EPSILON) {
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
        }

        else if (index == pointsCount - 1){
            if (point.getX() - getNodeByIndex(pointsCount - 2).point.getX() < EPSILON) {
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
        }

        else if (point.getX() - getNodeByIndex(index - 1).point.getX() < EPSILON || point.getX() - getNodeByIndex(index + 1).point.getX() > -EPSILON) {
            throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
        }

        getNodeByIndex(index).point = new FunctionPoint(point);
    }

    public double getPointX(int index){
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }

        else if (index == 0){
            if (x - getNodeByIndex(1).point.getX() > -EPSILON){
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
        }

        else if (index == pointsCount - 1 ) {
            if(x - getNodeByIndex(pointsCount - 2).point.getX() < EPSILON) {
                throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
            }
        }

        else if (x - getNodeByIndex(index - 1).point.getX() < EPSILON || x - getNodeByIndex(index + 1).point.getX() > -EPSILON) {
            throw new InappropriateFunctionPointException("Координата x задаваемой точки лежит вне интервала");
        }

        getNodeByIndex(index).point.setX(x);
    }

    public double getPointY(int index){
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y){
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index){
        if(pointsCount<3){
            throw new IllegalStateException("В массиве находятся только две точки");
        }
        if (index<0||index>=pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Введённый индекс выходит за границы набора точек или меньше нуля");
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = head.next;
        while (node != head) {
            if (Math.abs(node.point.getX() - point.getX()) < EPSILON) {
                throw new InappropriateFunctionPointException("Точка с такой координатой x уже существует");
            }
            node = node.next;
        }

        int insertIndex = 0;
        node = head.next;
        while (node != head && point.getX() - node.point.getX() > EPSILON) {
            node = node.next;
            insertIndex++;
        }
        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        FunctionNode current = head.next;
        while (current != head) {
            current.point.writeExternal(out);
            current = current.next;
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        initializeList();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            FunctionPoint point = new FunctionPoint();
            point.readExternal(in);
            try {
                addPoint(point);
            } catch (InappropriateFunctionPointException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("{");

        FunctionNode currentNode = head.next;
        for (int i = 0; i < pointsCount; i++){
            str.append(currentNode.point.toString());
            currentNode = currentNode.next;
            if (i<pointsCount-1){
                str.append(", ");
            }
        }
        str.append("}");
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TabulatedFunction)) {
            return false;
        }

        FunctionNode currentNode = head.next;
        if (o instanceof LinkedListTabulatedFunction){
            LinkedListTabulatedFunction LLTFo = (LinkedListTabulatedFunction) o;
            FunctionNode currentNodeLLTFo = LLTFo.head.next;
            if (LLTFo.pointsCount != this.pointsCount)
                return false;
            for (int i = 0; i < pointsCount; i++){
                if (!currentNode.point.equals(currentNodeLLTFo.point))
                    return false;
                currentNode = currentNode.next;
                currentNodeLLTFo = currentNodeLLTFo.next;
            }
            return true;
        }

        else{
            TabulatedFunction TFo = (TabulatedFunction) o;
            if (TFo.getPointsCount() != this.pointsCount)
                return false;
            for(int i = 0; i < pointsCount; i++){
                if(!currentNode.point.equals(TFo.getPoint(i)))
                    return false;
                currentNode = currentNode.next;
            }
        }
        return true;
    }

    public int hashCode(){
        int hash = pointsCount;

        FunctionNode currentNode = head.next;
        for (int i = 0; i < pointsCount; i++){
            hash ^= currentNode.point.hashCode();
            currentNode = currentNode.next;
        }
        return hash;
    }

    public Object clone(){
        FunctionPoint[] ClonePoints = new FunctionPoint[pointsCount];

        FunctionNode currentNode = head.next;
        for(int i = 0; i < pointsCount; i++){
            ClonePoints[i] = (FunctionPoint) currentNode.point.clone();
            currentNode = currentNode.next;
        }

        return new LinkedListTabulatedFunction(ClonePoints);
    }
}
