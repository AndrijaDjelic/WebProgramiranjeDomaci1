package com.company;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static BlockingQueue<Student> studentList = new LinkedBlockingQueue<>();
    static AtomicInteger atomicSum = new AtomicInteger(0);
    static AtomicInteger atomicGradedStudents = new AtomicInteger(0);

    public static Student findAndRemoveStudentFromList(Predavac predavac){
        Student student = null;
        boolean flag =false;
        for (Student st:studentList) {
            if(st.getPredavac().equals(predavac)){
                student = st;
                flag = true;
            }
        }
        if(flag){
            studentList.remove(student);
        }
        return student;

    }

    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            studentList.add(new Student("Ime" + i, Math.round(random.nextDouble()*10.0) / 10.0, Math.round(ThreadLocalRandom.current().nextDouble(0.5, 1)*10.0)/10.0,
                    random.nextInt(100), cyclicBarrier));

        }
        System.out.println(studentList.toString());
        System.out.println("Starting simulation");

        boolean flag = false;

        while(true){

            ThreadPoolExecutor executor =
                    (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

//            Timer timer = new Timer();
            try {
                Thread asistentThread = null;
                if(!studentList.isEmpty()){
                    Student st= findAndRemoveStudentFromList(Predavac.ASISTENT);
                    if(st!=null){
                        asistentThread = new Thread(st);
//                        timer.schedule(new TimeOutTask(asistentThread, timer), 5*1000);
//                        asistentThread.start();
                         executor.submit(asistentThread);
                    }
                }



                Thread profesorThread1 = null;
                if(!studentList.isEmpty()){
                    Student st= findAndRemoveStudentFromList(Predavac.PROFESOR);
                    if(st!=null){
                        profesorThread1 = new Thread(st);
//                        timer.schedule(new TimeOutTask(profesorThread1, timer), 5*1000);
                    }

                }
                Thread profesorThread2 = null;
                if(!studentList.isEmpty()){

                    Student st= findAndRemoveStudentFromList(Predavac.PROFESOR);
                    if(st!=null){
                        profesorThread2 = new Thread(st);
//                        timer.schedule(new TimeOutTask(profesorThread2, timer), 5*1000);
                    }
                }

                if(profesorThread1 !=null){
//                    profesorThread1.start();
                    executor.submit(profesorThread1);
                }
                if( profesorThread2 !=null){
//                    profesorThread2.start();
                    executor.submit(profesorThread2);
                }

                /*while(atomicBoolean.get()){

                }
                atomicBoolean.set(true);*/
                Thread.sleep(5000);

                /**
                 * prekidanje threadova
                 */
                if (asistentThread != null && asistentThread.isAlive()) {
                        asistentThread.interrupt();
                }
                if(profesorThread1 !=null && profesorThread1.isAlive()){
                        profesorThread1.interrupt();
                }
                if(profesorThread2 !=null && profesorThread2.isAlive()){
                        profesorThread2.interrupt();

                }
                if (asistentThread != null) {
                    asistentThread.join();
                }
                if (profesorThread1 != null) {
                    profesorThread1.join();
                }

                if (profesorThread2 != null) {
                    profesorThread2.join();
                }

                /**
                 * prvo shutdown thread pool
                 */

                executor.shutdownNow();
                System.out.println("zavrseni threadovi");
                if(studentList.isEmpty()){
                    System.out.println("Simulacija zavrsena");
//                    awaitTerminationAfterShutdown(executor);
                    double sum = (double) atomicSum.get();
                    double numberOfNumbers = (double)atomicGradedStudents.get();
                    double prosecnaOcena = sum/numberOfNumbers;
                    System.out.println("Prosecna ocena: " + prosecnaOcena);

                    return;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public static void awaitTerminationAfterShutdown(ThreadPoolExecutor threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
