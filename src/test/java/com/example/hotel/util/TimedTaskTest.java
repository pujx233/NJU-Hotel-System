package com.example.hotel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedTaskTest {
    private OrderServiceSimulator simulator;
    public TimedTaskTest(){
        this(new TimerSimulator());
    }
    public TimedTaskTest(OrderServiceSimulator simulator){
        this.simulator = simulator;
    }

    public void testSimulator(String simulatorType){
        System.out.println("Start Running "+simulatorType);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        OrderServiceSimulator orderServiceSimulator = this.simulator;
        String voice = null;
        long delay = 1L;
        boolean validInput = false;
        while(true){
            try{
                String command = bf.readLine().trim();
                if(command.equals("end")){
                    System.out.println("End Running");
                    return;
                }
                String[] arg = command.split(",");
                if(arg.length==2){
                    voice = arg[0] + " —— heard "+ arg[1]+"s later after typing";
                    delay = Long.parseLong(arg[1]);
                }else {
                    throw new Exception();
                }
                validInput = true;
            }catch (Exception e){
                System.out.println("input failed");
            }
            if(validInput){
                orderServiceSimulator.runTimedTask(voice,delay);
                validInput = false;
            }
        }
    }

    public void testTimer(){
        this.simulator = new TimerSimulator();
        testSimulator("Timer");
    }

    public void testScheduledExecutorService(){
        this.simulator = new ScheduledExecutorServiceSimulator();
        testSimulator("ScheduledExecutorService");
    }

    public static void main(String[] args) {
        TimedTaskTest test = new TimedTaskTest();
        test.testTimer();
        test.testScheduledExecutorService();
    }
}

abstract class OrderServiceSimulator{

    //手动输入命令测试，输入格式：字符串（随意）,延迟时间（单位s）
    abstract public void runTimedTask(String voice,long second);

    public void saySomething(String voice){
        DateTimeFormatter df_toSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = df_toSecond.format(LocalDateTime.now());
        System.out.println(now+"发生 "+voice);
    }
}

class TimerSimulator extends OrderServiceSimulator{
    @Override
    public void runTimedTask(String voice,long second){
        Timer timer = new Timer();
        long delay = second * 1000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                saySomething(voice);
            }
        },delay);//delay的位置上填入延迟的毫秒数
        System.out.println("Timer线程已调，静待"+second+"s后");
    }

}

class ScheduledExecutorServiceSimulator extends OrderServiceSimulator{
    @Override
    public void runTimedTask(String voice, long second) {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        long delay = second * 1000;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                saySomething(voice);
            }
        };
        service.schedule(runnable,delay, TimeUnit.MILLISECONDS);
        System.out.println("ScheduledExecutorService线程已调，静待"+second+"s后");
    }

}
