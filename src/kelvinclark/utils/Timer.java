package kelvinclark.utils;
/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

/**
 *
 * TODO:
 *      - Introduzir um temporizador com repetição (em loop) / ex: 3, 2, 1, 0, (repete) 3, 2, 1, 0, (repete) 3, ...
 *      pode até ser um método "setLoop(true)" que raz recomeçar o temporizador assim que ele chega a zero.
 * *
 *      - Registar o método "void onFinish(Timer t)" (o que fazer ao finalizar o timer)
 *
 *      - criar método "getTimeSpan()" para obter o intervalo de tempo passado entre o início do temporizador e o tmepo atual
 *
 *      - criar método "isTicking()" que retorna true no momento de passagem de um segundo para outro
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import processing.core.PApplet;

public final class Timer implements Comparable<Timer>, Runnable {
    
    // timer constants
    static public final int SS = 0;
    static public final int MM_SS = 1;
    static public final int HH_MM_SS = 2;
    static private final int ONE_SECOND = 1000; // 1 second = 1000 milliseconds
    static private final int SECONDS_PER_MINUTE = 60; // 1 minute = 60 seconds
    static private final int MINUTES_PER_HOUR = 60; // 1 hour = 60 minutes
    static private final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR; // 1 hour = (60 seconds) * (60 minutes) = 3600 seconds
    static private final long MAX_LONG_VALUE = Long.MAX_VALUE;
    static private final int MAX_INT_VALUE   = Integer.MAX_VALUE;
    
    // processing related fields
    private final PApplet parent;
    private final Method onTicEvent;
    private Thread thread;
    
    // timer related fields
    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    private final long millisOffset = System.currentTimeMillis(); // Time in milliseconds when the applet was started.
    private long savedTime;  // increments the millis() value since the sketch started running
    
    // miscellaneous
    private int startedMilliseconds, inicialSecond, inicialMinute, inicialHour;
    private int absoluteTimeInMilliseconds, absoluteTimeInSeconds;
    
    // state related fields
    private boolean running;
    private boolean stopped;
    
    
    static {
        System.out.println("===========================");
        System.out.println("   TIMER by Kelvin Clark");
        System.out.println("===========================");
    }
    // ***************** CONSTRUCTOR *****************
    
    @SuppressWarnings("LeakingThisInConstructor")
    public Timer(PApplet app, int ... time) {
        parent = app;
        
        onTicEvent = registerEventMethod("onTic", new Class[] { Timer.class });
        
        if (time.length > 3) throw new RuntimeException("The constructor only receives up to three parameters");
        
        for (int i = 0; i < time.length; i++) {
            if (time[i] < 0) throw new IllegalArgumentException("This method expects only positive int numbers");
        }
        this.running = false;
        this.stopped = true;
        setTimer(time);
        
        thread = new Thread(this);
    }
    // ***********************************************
    
    
    /*
    * SYSTEM METHODS - Methods related to all calculations made to run the timer
    */
    
    @Override
    public synchronized void run() {
        while(isRunning()){
            if (absoluteTimeInSeconds <= 0) stop();
            if (isRunning()) {
                if ((System.currentTimeMillis() - millisOffset - savedTime) >= ONE_SECOND) {
                    absoluteTimeInSeconds--;
                    invokeMethod(onTicEvent, new Object[] { this });
                    savedTime = System.currentTimeMillis() - millisOffset;
                }
            }
        }
        System.out.println("Thread terminated: " + thread.getName());
    }
    
    
    /*
    * STATE METHODS - Methods related to the current state of the object
    */
    
    public void start() { // Start the timer
        if (!isRunning()) {
            running = true;
            stopped = false;
            if(!thread.isAlive()) {
                thread = new Thread(this);
                thread.start();
            }
        }
    }
    
    public void stop() {
        running = false;
        stopped = true;
    }
    
    public void restart() {
        setTimer(inicialHour, inicialMinute, inicialSecond);
        System.out.println("restarting");
        stop();
    }
    
    
    /*
    * GETTER METHODS - Methods that deals with all the object's outputs.
    */
    
    public boolean isRunning() {
        return running;
    }
    public boolean isStopped() {
        return stopped;
    }
    public int getSeconds() {
        return absoluteTimeInSeconds % SECONDS_PER_MINUTE;
    }
    public int getMinutes() {
        return absoluteTimeInSeconds % SECONDS_PER_HOUR / MINUTES_PER_HOUR;
    }
    public int getHours() {
        return absoluteTimeInSeconds / SECONDS_PER_HOUR;
    }
    public int getAbsoluteTimeInSeconds() {
        return absoluteTimeInSeconds;
    }
    public int getAbsoluteTimeInMinutes() { // DEVERIA RETORNAR UM FLOAT?? EX: 5.3 MINUTOS, EM VEZ DE 5 MINUTOS?
        return (absoluteTimeInSeconds / SECONDS_PER_MINUTE);
    }
    public int getAbsoluteTimeInHours() { // FAZ ALGUM SENTIDO TER ESTE MÉTODO??
        return (absoluteTimeInSeconds / SECONDS_PER_HOUR);
    }
    public String getTimer(int format) {
        String clock = "";
        
        switch(format) {
            case SS:
                clock = PApplet.nf(getAbsoluteTimeInSeconds(), 2);
                break;
            case MM_SS:
                clock = PApplet.nf(getAbsoluteTimeInMinutes(), 2) + ":" + PApplet.nf(getSeconds(), 2);
                break;
            case HH_MM_SS:
                clock = PApplet.nf(getAbsoluteTimeInHours(), 2) + ":" + PApplet.nf(getMinutes(), 2) + ":" + PApplet.nf(getSeconds(), 2);
                break;
        }
        return clock;
    }
    
    
    /*
    * SETTER METHODS - Methods that deals with all the object's inputs and outputs
    */
    
    
    public void setTimer(int ... time) {
        if (time.length > 3) throw new RuntimeException("This method only receives up to three parameters");
        
        for (int i = 0; i < time.length; i++) {
            if (time[i] < 0) throw new IllegalArgumentException("This method expects only positive int numbers");
        }
        absoluteTimeInSeconds = toAbsolute(time);
        inicialSecond = getSeconds();
        inicialMinute = getMinutes();
        inicialHour   = getHours();
    }
    
    
    /*
    * UTILITY METHODS - Methods that deals with all sort of things xD
    */
    
    private int toAbsolute(int ... time) {
        long hh, mm, ss, absolute = 0;
        
        switch(time.length) {
            case 1:
                absolute = time[0];
                break;
            case 2:
                mm = time[0];
                ss = time[1];
                absolute = (mm * SECONDS_PER_MINUTE) + ss;
                break;
            case 3:
                hh = time[0];
                mm = time[1];
                ss = time[2];
                absolute = (hh * SECONDS_PER_HOUR) + (mm * SECONDS_PER_MINUTE) + ss;
                break;
        }
        if (absolute > MAX_INT_VALUE) throw new IllegalArgumentException("You are passing a number that breaks the range of int numbers");
        return (int)absolute;
    }
    
    public void decrease(int ... time) {
        if (time.length > 3) throw new RuntimeException ("This method only receives up to three parameters");
        
        for (int i = 0; i < time.length; i++) {
            if (time[i] < 0) throw new IllegalArgumentException("This method expects only positive int numbers");
        }
        
        int decrement = toAbsolute(time);
        
        if (absoluteTimeInSeconds < decrement) { // In the last moment...
            absoluteTimeInSeconds = 0;
            System.err.println("It cannot decrease anymore");
            return;
        }
        
        int old_ss = getSeconds();
        int old_mm = getMinutes();
        int old_hh = getHours();
        int new_ss = decrement % SECONDS_PER_MINUTE; // decrement % 60
        int new_mm = decrement % SECONDS_PER_HOUR / MINUTES_PER_HOUR; // decrement % 3600 / 60
        int new_hh = decrement / SECONDS_PER_HOUR; // decrement / 3600
        
        absoluteTimeInSeconds -= decrement; // updating absoluteTimeInSeconds
        
        System.out.println("Decreasing by "+decrement+" seconds (" + PApplet.nf(new_hh, 2) + ":" + PApplet.nf(new_mm, 2) + ":" + PApplet.nf(new_ss, 2)
                + ") - from: " + PApplet.nf(old_hh, 2) + ":" + PApplet.nf(old_mm, 2) + ":" + PApplet.nf(old_ss, 2)
                + " to: " + PApplet.nf(getHours(), 2) + ":" + PApplet.nf(getMinutes(), 2) + ":" + PApplet.nf(getSeconds(), 2));
    }
    
    public void increase(int ... time) {
        if (time.length > 3) throw new RuntimeException ("This method only receives up to three parameters");
        
        for (int i = 0; i < time.length; i++) {
            if (time[i] < 0) throw new IllegalArgumentException("This method expects only positive int numbers");
        }
        
        int increment = toAbsolute(time);
        
        int old_ss = getSeconds();
        int old_mm = getMinutes();
        int old_hh = getHours();
        int new_ss = increment % SECONDS_PER_MINUTE; // decrement % 60
        int new_mm = increment % SECONDS_PER_HOUR / MINUTES_PER_HOUR; // decrement % 3600 / 60
        int new_hh = increment / SECONDS_PER_HOUR; // decrement / 3600
        
        absoluteTimeInSeconds += increment; // updating absoluteTimeInSeconds
        
        System.out.println("Increasing by "+increment+" seconds (" + PApplet.nf(new_hh, 2) + ":" + PApplet.nf(new_mm, 2) + ":" + PApplet.nf(new_ss, 2)
                + ") - from: " + PApplet.nf(old_hh, 2) + ":" + PApplet.nf(old_mm, 2) + ":" + PApplet.nf(old_ss, 2)
                + " to: " + PApplet.nf(getHours(), 2) + ":" + PApplet.nf(getMinutes(), 2) + ":" + PApplet.nf(getSeconds(), 2));
    }
    
    @Override
    public String toString() {
        String initialTimer = "Initial timer: "+PApplet.nf(inicialHour, 2)+":"+PApplet.nf(inicialMinute, 2)+":"+PApplet.nf(inicialSecond, 2);
        String currentTimer = "Current timer: "+getTimer(HH_MM_SS);
        String absolute     = "Absolute time in seconds: " + absoluteTimeInSeconds;
        return initialTimer + " | " + currentTimer + " | " + absolute;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.absoluteTimeInSeconds;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Timer other = (Timer) obj;
        return this.absoluteTimeInSeconds == other.absoluteTimeInSeconds;
    }
    
    @Override
    public int compareTo(Timer t) {
        return this.absoluteTimeInSeconds - t.absoluteTimeInSeconds;
    }
    
    private Method registerEventMethod(String methodName, Class... args){
        try {
            return parent.getClass().getMethod(methodName, args);
        } catch(NoSuchMethodException | SecurityException e){
//            System.err.println("Your sketch needs to implement \"void "+methodName+"(SpriteSheet s)\"");
        }
        return null;
    }
    
    @SuppressWarnings("UnusedAssignment") // for the 'method = null;' line
    private synchronized void invokeMethod(Method method, Object... args){
        if(method != null) {
            try {
                method.invoke(parent, args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.err.println("failed to call method " + method.toString() + " inside your sketch");
                method = null;
            }
        }
    }
}
