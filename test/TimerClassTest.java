

import processing.core.*;
import kelvinclark.utils.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Kelvin Clark Spátola
 */
public class TimerClassTest extends PApplet {
    
    public static void main(String[] args) {
        PApplet.main("TimerClassTest");
    }
    
    Timer t;
//    Timer[] timers = new Timer[500];
    
    public void settings() {
        size(200, 200);
    }
    
    public void setup() {
        /*
        //        ArrayList<Timer> timerList = new ArrayList();
        //
        //        for (int i = 0; i < 10; i++) {
        //            int h = (int) (24 * Math.random());
        //            int m = (int) (60 * Math.random());
        //            int s = (int) (60 * Math.random());
        //            timerList.add(new Timer(h, m, s));
        //            System.out.println(timerList.get(i).getTimer());
        //        }
        //        System.out.println();
        //        for (Timer t : timerList) {
        //            t.decrease(5, 0, 0);
        //        }
        //        System.out.println();
        //        for (int i = 0; i < timerList.size(); i++) {
        //            if (timerList.get(i).getAbsoluteTimeInHours() < 10) {
        //                timerList.remove(i);
        //            }
        //        }
        //        System.out.println();
        //        for (Timer t : timerList) {
        //            System.out.println(t.getTimer());
        //        }
        //
        //        System.out.println("size = " + timerList.size());
        
        ****************************************************************
        //        Timer t1 = new Timer(4, 20, 50);
        //        Timer t2 = new Timer(3, 20, 30);
        //        System.out.println(t1.hashCode());
        //        System.out.println(t2.hashCode());
        //
        //        for (int i = 0; i < 10; i++) {
        //            t1.decrease(15,5);
        //
        //            if (t1.equals(t2)) {
        //                System.out.println("THEY ARE EQUALS");
        //                break;
        //            } else {
        //                System.out.println("They are different");
        //            }
        //        }
        //        System.out.println(t1.getTimer(TConstants.HH_MM_SS) + " - " + t2.getTimer(TConstants.HH_MM_SS));
        //        System.out.println(t1.hashCode());
        //        System.out.println(t2.hashCode());
        //++++++++++++++++++++++++++++++++++++++++++++++++
        //        Timer t1 = new Timer(3, 20, 50);
        //        Timer t2 = new Timer(3, 20, 10);
        //
        //        t1.decrease(30);
        //        t1.decrease(5);
        //        t1.decrease(5);
        //        System.out.println(t1.getTimer());
        //        System.out.println(t1.equals(t2));
        //        Map<Timer, Timer> timers = new HashMap<>();
        //        timers.put(t1, t1);
        //        timers.put(t2, t2);
        //        timers.put(t3, t3);
        //        if (timers.containsValue(t3)) {
        //            System.out.println("Timer found in the collection");
        //            System.out.println(timers.size());
        //        } else {
        //            System.out.println("Timer NOT found in the collection");
        //        }
        //++++++++++++++++++++++++++++++++++++++++++++++++
        ArrayList<Timer> timerList = new ArrayList();
        //
        for (int i = 0; i < 10; i++) {
        int h = (int) (24 * Math.random());
        int m = (int) (60 * Math.random());
        int s = (int) (60 * Math.random());
        timerList.add(new Timer(h, m, s));
        System.out.println(timerList.get(i).getTimer(TConstants.HH_MM_SS));
        }
        System.out.println("");
        
        Collections.sort(timerList);
        
        for (Timer t : timerList) {
        System.out.println(t.getTimer(TConstants.HH_MM_SS));
        }
        System.exit(0);
        } */
        
        textAlign(CENTER, CENTER);
        textSize(50);
        
        t = new Timer(this, 1, 0, 0);
        t.start();
        
//        for(int i = 0; i < timers.length; i++){
//            timers[i] = new Timer(this, 10);
//            timers[i]. start();
//        }
    }
    
    int color = 255;
    
    public void draw() {
        background((t.compareTo(new Timer(this, 50)) == 0) ? color(255, 0, 0) : 255);
        
        fill(0);
        text(t.getTimer(Timer.HH_MM_SS), width/2, height/3);
        
        fill(color);
        circle(width/2, 2*height/3, 30);
        println((int)frameRate);
        
//text(60 - (millis()/1000), width/2, 2*height/3);
//if(t.isStopped()) noLoop();
    }
    
//    int num = 0;
    public void onTic(Timer t){
        color = color(random(255), random(255), random(255));
//        num++;
    }
    
    @Override
    public void keyPressed() {
        if (key == ENTER) {
            t.start();
        } else if (key == 's') {
            t.stop();
        } else if (key == 'r') {
            t.restart();
        } else if (key == 'd') {
            t.decrease(10);
        } else if (key == 'i') {
            t.increase(10);
        } else if (key == 'p') {
            println(t);
        } else if (key == ' ') {
            t.setTimer((int) random(86400));
        }
        
    }
}
