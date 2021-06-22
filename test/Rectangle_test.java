/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import kelvinclark.utils.Rectangle;
import processing.core.*;

public class Rectangle_test extends PApplet{
    
    public static void main(String[] args) {
        PApplet.main("Rectangle_test");
    }
    
    public void settings() {
        size(600, 300);
    }
    
    Rectangle r1, r2, r3;
    int bgColor = 255;
    
    public void setup(){
        r1 = new Rectangle(this, 150f);
        r2 = new Rectangle(this, 100f);
        //r3 = new Rectangle(this, 30f);
    }
    
    public void draw() {
        background(255);
        rectMode(CENTER);
        
        push();
        translate(width/2, height/2);
        rotate(frameCount * 0.005f);
        //rotate(PI/4);
        fill(r1.contains(r2) ? color(0, 255, 0) : 120);
        r1.draw();
        pop();
        //fill(r1.intersects(r2) || r2.intersects(r1) ? color(0, 255, 0, 50) : color(120, 50));
        //drawRect(r1.vertices);
        
//        push();
//        translate(mouseX, mouseY);
//        rotate(PI/4);
//        fill(r2.intersects(r1) ? color(255, 255, 0) : 120);
//        r2.draw();
//        pop();
//        //fill(r2.contains(r3) ? color(255, 255, 0) : 120);
//        //drawRect(r2.vertices);
//
//        rectMode(CORNER);

//        push();
//        translate(3*width/4, height/2);
//        rotate(frameCount * 0.005f);
//        fill(r3.intersects(r2) || r2.intersects(r3) ? color(0, 255, 255) : 120);
//        r3.draw();
//        pop();
//fill(r3.intersects(r2) || r2.intersects(r3) ? color(0, 255, 255) : 120);
//drawRect(r3.vertices);

//println((int) frameRate);
    }
    
    void drawRect(PVector[] v){
        beginShape();
        for(PVector p : v) vertex(p.x, p.y);
        endShape(CLOSE);
        fill(0);
        text("A", v[0].x, v[0].y);
        text("B", v[1].x, v[1].y);
        text("C", v[2].x, v[2].y);
        text("D", v[3].x, v[3].y);
    }
}
