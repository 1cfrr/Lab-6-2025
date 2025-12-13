import functions.*;
import functions.basic.*;
import threads.*;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("1. Проверка метода integrate: ");
        System.out.printf("Значение интегралла экспоненты от 0 до 1: %.8f%n",Math.exp(1)-1);
        double integrate = Functions.integrate(new Exp(),0,1,0.1);
        System.out.printf("Значение интегралла экспоненты от 0 до 1: %.8f для шага 0.1; погрешность - %.8f%n",integrate, integrate - (Math.exp(1)-1));
        integrate = Functions.integrate(new Exp(),0,1,0.01);
        System.out.printf("Значение интегралла экспоненты от 0 до 1: %.8f для шага 0.01; погрешность - %.8f%n",integrate, integrate - (Math.exp(1)-1));
        integrate = Functions.integrate(new Exp(),0,1,0.001);
        System.out.printf("Значение интегралла экспоненты от 0 до 1: %.8f для шага 0.001; погрешность - %.8f%n",integrate, integrate - (Math.exp(1)-1));
        integrate = Functions.integrate(new Exp(),0,1,0.0001);
        System.out.printf("Значение интегралла экспоненты от 0 до 1: %.8f для шага 0.0001; погрешность - %.8f%n",integrate, integrate - (Math.exp(1)-1));

        System.out.println("\n2. Проверка класса Task:");
        nonThread();
        System.out.println("\n3. Проверка классов SimpleGenerator и SimpleIntegrator:");
        simpleThreads();
        System.out.println("\n4. Проверка классов Semaphore, Generator и Integrator:");
        complicatedThreads();
    }

    public static void nonThread(){
        Task task = new Task(100);
        for(int i =0; i < 100; i++){
            double base = 1 + Math.random()*9;
            task.setLeftBorder(Math.random() * 100);
            task.setRightBorder(100 + Math.random() * 100);
            task.setDiscretizationStep(Math.random());

            System.out.printf("Source %.4f %.4f %.4f%n", task.getLeftBorder(), task.getRightBorder(), task.getDiscretizationStep());

            try {
                Log logFunc = new Log(base);
                double result = Functions.integrate(logFunc, task.getLeftBorder(), task.getRightBorder(), task.getDiscretizationStep());

                System.out.printf("Result %.4f %.4f %.4f %.6f%n", task.getLeftBorder(), task.getRightBorder(), task.getDiscretizationStep(), result);
            } catch (Exception e) {
                System.out.println("Ошибка при интегрировании: " + e.getMessage());
            }
        }
    }

    public static void simpleThreads() {
        Task task = new Task(100);

        Thread generator = new Thread(new SimpleGenerator(task));
        Thread integrator = new Thread(new SimpleIntegrator(task));

        generator.setPriority(Thread.NORM_PRIORITY);
        integrator.setPriority(Thread.NORM_PRIORITY);

        generator.start();
        integrator.start();

        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            System.out.println("Основной поток прерван при ожидании");
        }
    }

    public static void complicatedThreads() {
        Task task = new Task(100);

        Semaphore semaphore = new Semaphore();

        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);
        generator.setName("Generator");
        integrator.setName("Integrator");

        System.out.println("Запускаем потоки...\n");
        long startTime = System.currentTimeMillis();

        generator.start();
        integrator.start();
        try {
            Thread.sleep(100);
            System.out.println("Основной поток: прошло 200 мс, прерываем рабочие потоки");
            generator.interrupt();
            integrator.interrupt();

        } catch (InterruptedException e) {
            System.out.println("Основной поток прерван");
        }

        try {
            generator.join(100);
            integrator.join(100);
        } catch (InterruptedException e) {
            System.out.println("Ошибка при ожидании завершения потоков");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("РЕЗУЛЬТАТЫ:");
        System.out.printf("Общее время работы: %d мс%n", endTime - startTime);
        System.out.println("Состояние потоков:");
        System.out.println("  Generator: " + (generator.isAlive() ? "жив" : "завершён"));
        System.out.println("  Integrator: " + (integrator.isAlive() ? "жив" : "завершён"));
    }
}
