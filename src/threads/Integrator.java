package threads;

import functions.Functions;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
        this.setName("Integrator-Thread");
    }

    @Override
    public void run() {
        int tasksCount = task.getTasksCount();

        try {
            for (int i = 0; i < tasksCount; i++) {
                if (Thread.interrupted()) {
                    System.out.println(getName() + ": Прерван перед обработкой");
                    throw new InterruptedException();
                }

                semaphore.beginRead();

                boolean taskProcessed = false;

                try {
                    if (task.getFunction() != null) {
                        double left = task.getLeftBorder();
                        double right = task.getRightBorder();
                        double step = task.getDiscretizationStep();

                        try {
                            double result = Functions.integrate(task.getFunction(), left, right, step);

                            System.out.printf("%s[%d]: Result %.4f %.4f %.4f %.6f%n", getName(), i, left, right, step, result);

                        } catch (IllegalArgumentException e) {
                            System.out.printf("%s[%d]: ERROR - %s%n", getName(), i, e.getMessage());
                        }

                        // Очищаем задание
                        task.setFunction(null);
                        taskProcessed = true;
                    }

                } finally {
                    semaphore.endRead();
                }

                if (taskProcessed) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        System.out.println(getName() + ": Прерван после обработки");
                        throw e;
                    }
                } else {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        System.out.println(getName() + ": Прерван во время ожидания");
                        throw e;
                    }
                }
            }

            System.out.println(getName() + ": Завершил работу.");

        } catch (InterruptedException e) {
            System.out.println(getName() + ": Прерван с исключением.");
            Thread.currentThread().interrupt(); // Восстанавливаем флаг
        }
    }
}