package threads;

import functions.basic.Log;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
        this.setName("Generator-Thread");
    }

    @Override
    public void run() {
        int tasksCount = task.getTasksCount();

        try {
            for (int i = 0; i < tasksCount; i++) {
                if (Thread.interrupted()) {
                    System.out.println(getName() + ": Прерван перед созданием задания");
                    throw new InterruptedException();
                }

                semaphore.beginWrite();
                double base = 1 + Math.random() * 9;
                double left = Math.random() * 100;
                double right = 100 + Math.random() * 100;
                double step = Math.random();

                Log logFunction = new Log(base);

                try {
                    task.setFunction(logFunction);
                    task.setLeftBorder(left);
                    task.setRightBorder(right);
                    task.setDiscretizationStep(step);

                    System.out.printf("%s[%d]: Source %.4f %.4f %.4f%n",
                            getName(), i, left, right, step);

                }
                finally {
                    semaphore.endWrite();
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(getName() + ": Прерван во время сна");
                    throw e;
                }
            }

            System.out.println(getName() + ": Завершил работу нормально");

        } catch (InterruptedException e) {
            System.out.println(getName() + ": Прерван с исключением");
            Thread.currentThread().interrupt();
        }
    }
}