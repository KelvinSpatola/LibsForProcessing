 /**
  *
  * @author Kelvin Spátola (Ov3rM1nD_)
  */

import java.awt.Rectangle;
import processing.core.*;
import kelvinclark.utils.InfiniteMouse;

public class InfiniteMouse_Test extends PApplet{
    
    public static void main(String[] args) {
        PApplet.main(InfiniteMouse_Test.class);
    }
    
    InfiniteMouse mouse;
    
    int grid = 100;
    PVector bpos = new PVector();
    float bsize = grid;
    float bspeedX, bspeedY, bspeedZ;
    boolean input1, input2, input3, input4;
    float cameraRotateX, cameraRotateY, cameraSpeed, angle;
    int gridCount = 50;
    PVector pos, speed;
    float accelMag, gravity = 0.3f;
    
    public void settings() {
        //fullScreen(P3D, 2);
        size(900, 600, P3D);
    }
    
    public void setup(){       
        mouse = new InfiniteMouse(this, false);
        
        Rectangle r = new Rectangle(0, 0, displayWidth/2, displayHeight/2);
        Rectangle span = InfiniteMouse.Boundary.SPAN;
        Rectangle display = InfiniteMouse.Boundary.DISPLAY;
        Rectangle window = InfiniteMouse.Boundary.WINDOW;
        mouse.setBounds(window);
        //mouse.lock();
        
        textAlign(CENTER, CENTER);
        
        cameraSpeed = TWO_PI / width;
        cameraRotateY = -PI/6;
        pos = new PVector();
        speed = new PVector();
        accelMag = 2;
    }
    
    public void draw() {
        background(255);
        //noise();
        
        player();
        crosshair();
    }
    
    void drawGrid(int count) {
        translate(-pos.x, 0, -pos.y);
        stroke(255);
        float size = (count -1) * bsize*2;
        for (int i = 0; i < count; i++) {
            float pos = map(i, 0, count-1, -0.5f * size, 0.5f * size);
            line(pos, 0, -size/2, pos, 0, size/2);
            line(-size/2, 0, pos, size/2, 0, pos);
        }
    }
    
    void crosshair(){
        push();
        resetMatrix();
        ortho();
        noLights();
        //noFill();
        //stroke(255, 0, 0);
        //circle(0, 0, 50);
        //line(-25, 0, 25, 0);
        //line(0, -25, 0, 25);
        fill(0, 255, 0);
        textSize(30);
        textAlign(CENTER, CENTER);
        text("Kelvin Clark", 0, 0);
        pop();
    }
    
    void player(){
        perspective();
        updateRotation();
        lights();
        translate(width/2, height/10);
        rotateX(cameraRotateY);
        rotateY(cameraRotateX);
        background(125);
        pushMatrix();
        translate(bpos.x, height/2 + bpos.y, bpos.z);
        stroke(255);
        fill(0);
        rotateY(atan2(speed.x, speed.y));
        box(bsize);
        translate(0, -bsize/2, bsize/2);
        fill(200, 0, 0);
        sphereDetail(10);
        noStroke();
        sphere(bsize/4);
        popMatrix();
        
        PVector accel = getMovementDir().rotate(cameraRotateX).mult(accelMag);
        speed.add(accel);
        pos.add(speed);
        speed.mult(0.9f);
        translate(0, height/2+bsize/2);
        drawGrid(gridCount);
        
        //angle += mouse.getDirection().x * 0.1;
    }
    
    void updateRotation() {
        cameraRotateX += mouse.delta().x * cameraSpeed;
        cameraRotateY -= mouse.delta().y * cameraSpeed;
        cameraRotateY = constrain(cameraRotateY, -QUARTER_PI, 0);
    }
    
    PVector getMovementDir() {
        return pressedDir.copy().normalize();
    }
    
    boolean wPressed, sPressed, aPressed, dPressed;
    PVector pressedDir = new PVector();
    
    public void keyPressed() {
        switch(key) {
            case 'w':
                wPressed = true;
                pressedDir.y = -1;
                break;
            case 's':
                sPressed = true;
                pressedDir.y = 1;
                break;
            case 'a':
                aPressed = true;
                pressedDir.x = -1;
                break;
            case 'd':
                dPressed = true;
                pressedDir.x = 1;
                break;
            case ' ':
                int x = (int)random(InfiniteMouse.Boundary.LEFT, InfiniteMouse.Boundary.RIGHT);
                int y = (int)random(InfiniteMouse.Boundary.TOP, InfiniteMouse.Boundary.BOTTOM);
                mouse.setLocation(x, y);
                break;
        }
    }
    
    public void keyReleased() {
        switch(key) {
            case 'w':
                wPressed = false;
                pressedDir.y = sPressed ? 1 : 0;
                break;
            case 's':
                sPressed = false;
                pressedDir.y = wPressed ? -1 : 0;
                break;
            case 'a':
                aPressed = false;
                pressedDir.x = dPressed ? 1 : 0;
                break;
            case 'd':
                dPressed = false;
                pressedDir.x = aPressed ? -1 : 0;
                break;
        }
    }
    
    float mx, my, xoff = random(50000), yoff = random(50000);
    void noise(){
        mx = noise(xoff) * InfiniteMouse.Boundary.RIGHT;
        mx += InfiniteMouse.Boundary.LEFT;
        my = noise(yoff) * InfiniteMouse.Boundary.BOTTOM;
        mouse.setLocation((int)mx, (int)my);
        xoff += 0.01;
        yoff += 0.01;
    }
}
