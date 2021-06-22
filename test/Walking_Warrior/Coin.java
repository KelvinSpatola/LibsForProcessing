package Walking_Warrior;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import kelvinclark.utils.SpriteSheet;
import processing.core.PApplet;
import processing.core.PVector;

public class Coin {
    PApplet parent;
    PVector pos;
    int size, type;
    
    SpriteSheet sp;
    
    // CONSTRUCTOR
    Coin(PApplet parent, float x, float y, int size, int type) {
        this.parent = parent;
        sp = new SpriteSheet(parent, "Coin_Sprite_Sheet_2.png", 3, 6);
        sp.resizeSprites(size, 0);
        sp.setFrameRate(type == 0 ? 3 : type == 1 ? 6 : 9);
        
        pos = new PVector(x, y);
        this.size = size;
        this.type = type;
    }
    
    void display() {
        sp.getRow(type);
        parent.imageMode(3); //CENTER
        parent.image(sp.render(), pos.x, pos.y, size, size);
    }
}
