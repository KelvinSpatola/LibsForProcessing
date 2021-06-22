package Walking_Warrior;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import processing.core.PApplet;
import processing.core.PVector;

import kelvinclark.utils.GameController;
import kelvinclark.utils.SpriteSheet;


public class Player {
    PApplet parent;
    PVector pos = new PVector();
    PVector vel = new PVector();
    
    int size;
    float currentSpeed, walkingSpeed, runningSpeed;
    boolean isRunning;
    int offset = 0;
    
    SpriteSheet sp;
    
    
    // CONSTRUCTOR
    Player(PApplet parent, int size, float speed) {
        this.parent = parent;
        this.size = size;
        walkingSpeed = speed;
        runningSpeed = walkingSpeed * 1.75f;
        currentSpeed = walkingSpeed;
        
        pos = new PVector(0, 0);
        
        sp = new SpriteSheet(parent, "warrior.png", 8, 24);
        sp.resizeSprites(size, 0);
        sp.setFrameRate(5);
        
        GameController.init(parent);
    }
    
    void display() {
        parent.imageMode(3); //CENTER
        parent.image(sp.render(), pos.x, pos.y);
    }
    
    boolean contains(Coin c) {
        float d = pos.dist(c.pos);
        return d < size/4 + c.size/2;
    }
    
    void run() {
        isRunning = !isRunning;
        offset = isRunning ? 8 : 0;
        currentSpeed = isRunning ? runningSpeed : walkingSpeed;
    }
    
    void update() {
        switch(GameController.getDirection()) {
            
            case GameController.NORTH:
                sp.getRow(3, 8 + offset, 15 + offset);
                break;
            case GameController.SOUTH:
                sp.getRow(0, 8 + offset, 15 + offset);
                break;
            case GameController.WEST:
                sp.getRow(1, 8 + offset, 15 + offset);
                break;
            case GameController.EAST:
                sp.getRow(2, 8 + offset, 15 + offset);
                break;
            case GameController.NORTH_WEST:
                sp.getRow(7, 8 + offset, 15 + offset);
                break;
            case GameController.NORTH_EAST:
                sp.getRow(6, 8 + offset, 15 + offset);
                break;
            case GameController.SOUTH_WEST:
                sp.getRow(5, 8 + offset, 15 + offset);
                break;
            case GameController.SOUTH_EAST:
                sp.getRow(4, 8 + offset, 15 + offset);
                break;
            default:
                int dir = GameController.getLastDirection();
                
                if (dir == GameController.NORTH) sp.getRow(3, 0, 7);
                if (dir == GameController.SOUTH) sp.getRow(0, 0, 7);
                if (dir == GameController.WEST)  sp.getRow(1, 0, 7);
                if (dir == GameController.EAST)  sp.getRow(2, 0, 7);
                
                if (dir == GameController.NORTH_WEST) sp.getRow(7, 0, 7);
                if (dir == GameController.NORTH_EAST) sp.getRow(6, 0, 7);
                if (dir == GameController.SOUTH_WEST) sp.getRow(5, 0, 7);
                if (dir == GameController.SOUTH_EAST) sp.getRow(4, 0, 7);
                break;
        }
        
        vel.set(GameController.getDirectionVector());
        vel.setMag(currentSpeed);
        pos.add(vel);
    }
}
