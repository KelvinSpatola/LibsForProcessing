

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import kelvinclark.gui.DefaultButton;
import kelvinclark.gui.Button;
import processing.core.*;
import kelvinclark.utils.Timer;


public class DefaultButton_Testing extends PApplet{
    
    public static void main(String[] args) {
        PApplet.main("DefaultButton_Testing");
    }
    
    public void settings() {
        size(600, 200);
    }
    
    DefaultButton dBtn;
    int bgColor = color(255);
    int num;
    
    Timer t;
    
    public void setup(){
        dBtn = new DefaultButton(this, width/2, height/2 , 250, 50);
        dBtn.setSound(null, "TerminalEnter.mp3");
        dBtn.setColor(color(0, 10), color(0, 100), color(0));
        dBtn.noStroke();
        //dBtn.stroke(255, 0, 0);
        dBtn.strokeWeight(5);
        
        t = new Timer(this, 60);
    }
    
    public void draw() {
        background(dBtn.isLocked() ? color(255, 0, 0) : 255);
        fill(0);
        text(num, width/2, height-30);
        
        stroke(0);
        line(0, height/2, width, height/2);
        line(width/2, 0, width/2, height);
        
        //if(frameCount > 300) dBtn.setVisible(false);
    }
    
    public void buttonPressed(Button b){
        if(b == dBtn) dBtn.setLocked(!dBtn.isLocked());
    }
    
    public void buttonMouseover(Button b){
        if(b == dBtn && dBtn.isLocked() == false) num++;
//        bgColor = color(random(255));
    }
    
}
