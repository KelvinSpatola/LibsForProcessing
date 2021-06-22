package Walking_Warrior;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import java.util.ArrayList;
import processing.core.*;

public class GameController_Test extends PApplet{
    
    public static void main(String[] args) {
        PApplet.main("Walking_Warrior.GameController_Test");
    }
    
    public void settings() {
        fullScreen(P2D);
    }
    
    Player player;
    ArrayList<Coin> coins = new ArrayList();
    PVector topDownCameraView = new PVector();
    
    
    public void setup(){
        player = new Player(this, 150, 4);
        
        for (int i = 0; i < 100; i++)
            coins.add(new Coin(this, random(-width, width), random(-height, height), 50, (int)random(3)));
    }
    
    public void draw() {
        topDownCameraView.lerp(player.pos.x, player.pos.y, 0, 0.03f);
        translate(width/2 - topDownCameraView.x, height/2 - topDownCameraView.y);
        
        background(255);
        for (int i = 0; i < coins.size(); i++) {
            coins.get(i).display();
            if (player.contains(coins.get(i))) coins.remove(i);
        }
        
        player.display();
        player.update();
    }
    
    public void keyPressed() {
        if (key == CODED && keyCode == SHIFT) player.run();
    }
}
