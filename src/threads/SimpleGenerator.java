package threads;

import functions.*;
import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private Task task;

    public SimpleGenerator(Task task){
        this.task = task;
    }

    @Override
    public void run() {
        int tasksCount = task.getTasksCount();

        for (int i = 0; i < tasksCount; i++) {
            double base = 1 + Math.random() * 9;
            double left = Math.random() * 100;
            double right = 100 + Math.random() * 100;
            double step = Math.random();
            Log logFunction = new Log(base);

            synchronized (task){
                task.setFunction(logFunction);
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setDiscretizationStep(step);
            }

            System.out.printf("Generator: Source %.4f %.4f %.4f%n", left, right, step);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Generator finished.");
    }
}
