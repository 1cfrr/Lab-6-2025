package threads;

import functions.*;

public class SimpleIntegrator implements Runnable{
    private Task task;

    public SimpleIntegrator(Task task){
        this.task = task;
    }

    @Override
    public void run() {
        int tasksCount = task.getTasksCount();

        for (int i = 0; i < tasksCount; i++) {
            synchronized (task) {
                if (task.getFunction() == null) {
                }
                else {
                    double left = task.getLeftBorder();
                    double right = task.getRightBorder();
                    double step = task.getDiscretizationStep();

                    try {
                        double result = Functions.integrate(task.getFunction(), left, right, step);

                        System.out.printf("Integrator: Result %.4f %.4f %.4f %.6f%n", left, right, step, result);

                    } catch (IllegalArgumentException e) {
                        System.out.printf("Integrator: ERROR - %s%n", e.getMessage());
                    }

                    task.setFunction(null);
                }
            }
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    System.out.println("Integrator interrupted");
                    Thread.currentThread().interrupt();
                    break;
                }

        }
        System.out.println("Integrator finished.");
    }
}
