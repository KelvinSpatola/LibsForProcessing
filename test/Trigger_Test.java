 /**
  *
  * @author Kelvin Spátola (Ov3rM1nD_)
  */

import kelvinclark.utils.Trigger;
import processing.core.*;

public class Trigger_Test extends PApplet{
    
    public static void main(String[] args) {
        PApplet.main(Trigger_Test.class);
    }
    
    public void settings() {
        size(200, 100);
    }
    
    Trigger mousehover, mouseClick, frameTick;
    int cor = 255;
    
    public void setup(){
        mousehover = new Trigger(this);
        mouseClick = new Trigger(this);
        frameTick  = new Trigger(this);
    }
    
    public void draw() {
        background(255);
        fill(cor);
        rect(width/2, 0, width/2, height);
        fill(0);
        text(frameTick.getNumExecutions(), 3*width/4, height/2);
        
        mousehover.callTrigger(mouseX > width/2);
        mouseClick.callTrigger(mousehover.isActive() && mousePressed);
        frameTick .callTrigger(mousehover.isActive() && (frameCount % 60 == 0));
        
        if(mousehover.isActive() && frameTick.getNumExecutions() > 5) System.exit(0);
        //if(mousehover.isActive()) cor = color(random(255), random(255), random(255));
    }
    
    public void triggerEvent(Trigger trigger){
        //if(trigger == mousehover) cor = color(random(255), random(255), random(255));
        //if(trigger == mouseClick) System.exit(0);
        cor = color(random(255), random(255), random(255));
    }
}