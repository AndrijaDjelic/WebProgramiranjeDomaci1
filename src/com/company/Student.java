package com.company;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created on 09.03.2022. by Andrija inside package com.company.
 */
public class Student implements Runnable{

    private String name;
    private Double arrival;
    private Double ttc;
    private Predavac predavac;
    private CyclicBarrier cyclicBarrier;
    private int grade = 5;

    public Student(String name, Double arrival, Double ttc,int predavac, CyclicBarrier cyclicBarrier) {
        this.name = name;
        this.arrival = arrival;
        this.ttc = ttc;
        if(predavac<50){
            this.predavac = Predavac.ASISTENT;
        }else{
            this.predavac = Predavac.PROFESOR;
        }
        this.cyclicBarrier = cyclicBarrier;
    }


    @Override
    public void run() {
        long threadTime = 0;
        try {

            if(predavac.equals(Predavac.PROFESOR)){
                System.out.println("Nit broj " + name + " ceka.");
                cyclicBarrier.await();
                System.out.println("Nit" + name + " vise ne ceka.");
            }
            Thread.sleep((long) (arrival*1000));
//            System.out.println("Student " + name + " brani odbranu.");
            threadTime = System.currentTimeMillis()/1000;
            Thread.sleep((long) (ttc*1000));
            Random random = new Random();
            int sugestedGrade = random.nextInt(10);
            if(sugestedGrade>5){
                grade = sugestedGrade;
            }
            Main.atomicSum.getAndAdd(grade);
            Main.atomicGradedStudents.getAndIncrement();
//            System.out.println("Student " + name + " dobija ocenu " + grade);
            System.out.println("Thread: " + name + " Arrival: " + arrival + " Prof: " + predavac + " TTC: " + ttc + " Vreme pocetka odbrane: " + threadTime + " Score: " +grade );
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Prekinut Thread: " + name + " Arrival: " + arrival + " Prof: " + predavac + " TTC: " + ttc + " Vreme pocetka odbrane: " + threadTime + " Score: " +grade );
            Main.atomicSum.getAndAdd(grade);
            Main.atomicGradedStudents.getAndIncrement();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getArrival() {
        return arrival;
    }

    public void setArrival(Double arrival) {
        this.arrival = arrival;
    }

    public Double getTtc() {
        return ttc;
    }

    public void setTtc(Double ttc) {
        this.ttc = ttc;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Predavac getPredavac() {
        return predavac;
    }

    public void setPredavac(Predavac predavac) {
        this.predavac = predavac;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", arrival=" + arrival +
                ", ttc=" + ttc +
                ", predavac=" + predavac +
                ", grade=" + grade +
                '}'+'\n';
    }
}
