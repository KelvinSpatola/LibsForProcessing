/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import kelvinclark.utils.Toggle;
import kelvinclark.utils.Rectangle;
import processing.core.*;

public class Toggle_Test extends PApplet{
    
    public static void main(String[] args) {
        PApplet.main(Toggle_Test.class);
    }
    
    public void settings() {
        size(500, 100);
    }
    
    Toggle toggle, t2;
    Foo foo;
    Rectangle rect;
    
    public void setup() {
        //surface.setLocation(displayWidth + width, displayHeight/2);
        foo = new Foo();
        rect = new Rectangle(this, 50, 100);
        
        toggle = new Toggle(this)
                .set('p', "printMessage", false, "yellow:", color(255, 255, 0))
                .set('q', "meth1", true)
                .set('w', "meth2", true)
                .set('e', "meth3", true)
                .set('r', "meth4", true)
                .set('t', "meth5", true)
                .set('g', rect, "draw")
                .set(' ', "exit");
        
        t2 = new Toggle(this)
                .set('i', foo, "invertColors");
        
        println(toggle);
        println(toggle.hashCode());
        println(t2.hashCode());
        println(toggle.equals(t2));
        
    }
    int index = 1;
    public void draw() {
        background(0);
        
        //toggle.get("invertColors");
        toggle.get();
        
        fill(255);
        rect(0, 40, width, 20);
        
        //toggle.get("meth" + index);
        //if(frameCount % 60 == 0) {index++;println(index);}
        //if(index >= 6) index = 1;
        
        toggle.get("invertColors");
        toggle.get("meth3");
    }

    public void meth1() {
        fill(255, 0, 0);
        square(width/5*0, 0, width/5);
    }
    public void meth2() {
        fill(255, 255 ,0);
        square(width/5*1, 0, width/5);
    }
    public void meth3() {
        fill(0, 255, 0);
        square(width/5*2, 0, width/5);
    }
    public void meth4() {
        fill(0, 255, 255);
        square(width/5*3, 0, width/5);
    }
    public void meth5() {
        fill(0, 0, 255);
        square(width/5*4, 0, width/5);
    }
    public void printMessage(String text, int val){
        println(text, val);
    }
    
    class Foo{
        void invertColors(){
            textSize(30);
            textAlign(CENTER, CENTER);
            fill(0);
            text("INVERT", width/2, height/2);
            filter(INVERT);
        }
    }
    
    
//    @Override
//    protected void handleKeyEvent(KeyEvent event) {
//        if (!keyRepeatEnabled && event.isAutoRepeat()) return;
//
//        handleMethods("keyEvent", new Object[] { event });
//
//        keyEvent = event;
//        key = event.getKey();
//        keyCode = event.getKeyCode();
//
//        switch (event.getAction()) {
//            case KeyEvent.PRESS:
//                keyPressed = true;
//                keyPressed(keyEvent);
//                break;
//            case KeyEvent.RELEASE:
//                keyPressed = false;
//                keyReleased(keyEvent);
//                break;
//            case KeyEvent.TYPE:
//                keyTyped(keyEvent);
//                break;
//        }
//
//        if (event.getAction() == KeyEvent.PRESS) {
//            if (key == ESC) exit();
//        }
//    }
//
}