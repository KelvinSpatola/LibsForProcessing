package kelvinclark.utils;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import java.util.Map;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;


public class GameController {
    public static final int
            NONE  =  -1,
            NORTH = 119, // 119 -> 'w'
            WEST  =  97, //  97 -> 'a'
            SOUTH = 115, // 115 -> 's'
            EAST  = 100, // 100 -> 'd'
            NORTH_WEST = -(NORTH + WEST),
            NORTH_EAST = -(NORTH + EAST),
            SOUTH_WEST = -(SOUTH + WEST),
            SOUTH_EAST = -(SOUTH + EAST);
    
    private static final KeyEventHandler keyEventHandler = new KeyEventHandler();
    private static final Map<Integer, Boolean> map = new HashMap();
    private static int direction = NONE;
    
    
    public static void init(PApplet parent){
        parent.registerMethod("keyEvent", keyEventHandler);
        
        map.put(NORTH, false);
        map.put(WEST,  false);
        map.put(SOUTH, false);
        map.put(EAST,  false);
        map.put(NORTH_WEST, false);
        map.put(NORTH_EAST, false);
        map.put(SOUTH_WEST, false);
        map.put(SOUTH_EAST, false);
    }
    
    protected static class KeyEventHandler {
        
        public void keyEvent(KeyEvent e) {
            switch (e.getAction()) {
                case KeyEvent.PRESS:
                    setKeyInput((int) Character.toLowerCase(e.getKey()), true);
                    break;
                case KeyEvent.RELEASE:
                    setKeyInput((int) Character.toLowerCase(e.getKey()), false);
                    break;
            }
        }
        
        void setKeyInput(int key, boolean modifier) {
            if(!map.containsKey(key)) return;
            
            map.replace(key, modifier);
            map.replace(NORTH_WEST, map.get(NORTH) && map.get(WEST));
            map.replace(NORTH_EAST, map.get(NORTH) && map.get(EAST));
            map.replace(SOUTH_WEST, map.get(SOUTH) && map.get(WEST));
            map.replace(SOUTH_EAST, map.get(SOUTH) && map.get(EAST));
        }
    }
    
    public static int getDirection() {
        if(!map.values().contains(true)) return NONE;
        
        map.forEach((k, v) -> { if(v) direction = k; });
        return direction;
    }
    
    public static PVector getDirectionVector() {
        switch(getDirection()) {
            case NORTH:
                return new PVector(0, -1);
            case SOUTH:
                return new PVector(0, 1);
            case WEST:
                return new PVector(-1, 0);
            case EAST:
                return new PVector(1, 0);
            case NORTH_WEST:
                return new PVector(-1, -1).normalize();
            case NORTH_EAST:
                return new PVector(1, -1).normalize();
            case SOUTH_WEST:
                return new PVector(-1, 1).normalize();
            case SOUTH_EAST:
                return new PVector(1, 1).normalize();
            default:
                return new PVector(0, 0);
        }
    }
    
    public static int getLastDirection(){
        return direction;
    }
}