/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author kelvi
 */
import java.util.concurrent.*;

public class NewClass {
    public static void main(String[] args){
        
        BeeperControl beeper = new BeeperControl();
        beeper.beepForAnHour();
    }
    
}

class BeeperControl {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    int num = 0;
    
    public void beepForAnHour() {
        final Runnable beeper = () -> {
            System.out.println(num++);
        };
        
        // beeper task
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, 1, TimeUnit.SECONDS);
        
        // stop task
        scheduler.schedule(() -> {
            beeperHandle.cancel(true);
            System.exit(0);
        }, 10, TimeUnit.SECONDS);
    }
}