package SaveHiRes;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import kelvinclark.utils.SaveHiRes;
import processing.core.*;

public class SaveHiRes_test extends PApplet{
    
    public static void main(String[] args) {
        PApplet.main("SaveHiRes.SaveHiRes_test");
    }
    
    public void settings() {
        size(900, 600, P2D);
    }
    
    SaveHiRes output;
    int val = 1;
    
    public void setup(){
        output = SaveHiRes.createRecord(this, 5f);
        
        textFont(createFont("Georgia", 60));
        textAlign(CENTER, CENTER);
        background(255);
        
        fill(255, 50);
        rect(0, 0, width, height);

        for (int i = 0; i < 10; i++) new Particle().display();

        println("SAVING " + val++);

        output.saveFrame();
        launch(sketchPath(output.getFileName()));
        exit();
    }
    
//    public void draw() {
//        //output.beginRecord();
//        
//        fill(255, 50);
//        rect(0, 0, width, height);
//        
//        for (int i = 0; i < 100; i++) new Particle().display();
//        
//        //output.endRecord();
//        
//        fill(255, 0, 0);
//        text("TESTE", width/2, height/2);
//        
//        //println((int)frameRate);
//    }
    
    public void keyPressed(){
        if(key == ' ') {
            output.saveFrame();
            launch(sketchPath(output.getFileName()));
            exit();
        }
    }
    
    
    
    
    
    public static char[] charTable = { // length = 63;
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        ' '
    };
    
    class Particle {
        final float x, y, angle, size, inc;
        float alpha = 255;
        final char ch;
        
        Particle() {
            x = random(width);
            y = random(height);
            angle = random(TWO_PI);
            ch = charTable[(int)random(charTable.length)];
            size = random(60);
            inc = random(20);
        }
        
        Particle display() {
            push();
            translate(x, y);
            rotate(angle);
            textSize(size);
            fill(0, alpha);
            text(ch, 0, 0);
            pop();
            return this;
        }
        
        Particle update() {
            alpha -= inc;
            return this;
        }
    }
}
