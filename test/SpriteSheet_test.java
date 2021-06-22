/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import processing.core.*;
import kelvinclark.utils.SpriteSheet;

public class SpriteSheet_test extends PApplet{
    
    public static void main(String[] args) {
        PApplet.main("SpriteSheet_test");
    }
    
    public void settings() {
        size(400, 400);
    }
    
    SpriteSheet sp1, sp2, sp3, sp4;
    
    public void setup(){
        sp1 = new SpriteSheet(this, "Coin_Sprite_Sheet_2.png", 3, 6);
        sp1.resizeSprites(width/2, height/2);
        sp1.setFrameRate(20);
        
        sp2 = new SpriteSheet(this, "Coin_Sprite_Sheet.png", 3, 6, 5).resizeSprites(width/4, 0);
        sp2.setFrameRate(10);
        
        sp3 = sp1.copy();//.resizeSprites(150, 50);
        //sp3.setFrameRate(15);
        
        //sp4 = new SpriteSheet(this, sp1.getSprites());
        //sp4 = sp1.copy().resizeSprites(50, 0);
        //sp4.setFrameRate(2);
    }
    
    public void draw() {
        background(255);
        
        image(sp1.getRow(2).render(), 0, 0);
        
        image(sp2.getRow(1).render(), width/2, 0);
        
        image(sp3.getRow(sp2.currentRow()).render(), 0, height/2);
        
        //image(sp4.getRows().render(), width/2, height/2);
        
        surface.setTitle("fps: " + (int)frameRate);
    }
}
