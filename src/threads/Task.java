package threads;

import functions.*;

public class Task {
    private Function function;
    private double leftBorder;
    private double rightBorder;
    private double discretizationStep;
    private int tasksCount;

    public Task(int tasksCount){
        this.tasksCount=tasksCount;
    }

    public void setFunction(Function function){
        this.function = function;
    }

    public void setLeftBorder(double left) {
        this.leftBorder=left;
    }

    public void setRightBorder(double right) {
        this.rightBorder=right;
    }

    public void setDiscretizationStep(double step) {
        this.discretizationStep=step;
    }

    public void setTasksCount(int i) {
        this.tasksCount=tasksCount;
    }

    public Function getFunction() {
        return  function;
    }

    public int getTasksCount(){
        return tasksCount;
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public double getDiscretizationStep() {
        return discretizationStep;
    }


}
