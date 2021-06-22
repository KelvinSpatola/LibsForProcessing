package kelvinclark.gui;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import processing.core.PApplet;
import processing.event.MouseEvent;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import ddf.minim.Minim;
import ddf.minim.AudioPlayer;

public abstract class Button {
    
    // processing related fields
    protected final PApplet parent;
    
    // callback event related fields
    protected final Method mousePressedEvent;
    protected final Method mouseReleasedEvent;
    protected final Method mouseClickedEvent;
    protected final Method mouseoverEvent;
    protected static final String PRESS_EVENT = "buttonPressed";
    protected static final String RELEASE_EVENT = "buttonReleased";
    protected static final String CLICK_EVENT = "buttonClicked";
    protected static final String MOUSEOVER_EVENT = "buttonMouseover";
    
    // constants
    public static final int LEFT   = PApplet.LEFT;
    public static final int RIGHT  = PApplet.RIGHT;
    public static final int CENTER = PApplet.CENTER;
    
    protected static final int CIRCULAR = 0;
    protected static final int RECTANGULAR = 1;
    
    // dimensions
    protected float x, y;
    protected float width, height;
    protected static int displayMode = CENTER;
    
    // background colors
    protected int defaultColor   = 0; // default color
    protected int mouseoverColor = defaultColor;
    protected int clickedColor   = defaultColor;
    
    // Effect States
    protected boolean hasMouseoverSound, hasClickSound;
    protected boolean isVisible = true;
    protected boolean enabled = true;
    
    // Click and Event States
    protected boolean isLocked, isPressed;
    
    // Other
    protected int type;
    protected boolean fromInside = false;
    
    // media
    protected Minim theMinim;
    protected AudioPlayer mouseoverSound;
    protected AudioPlayer clickSound;
    
    // CONSTRUCTORS
    @SuppressWarnings("LeakingThisInConstructor")
    public Button(PApplet parent) {
        this.parent = parent;
        
        parent.registerMethod("draw", this);
        parent.registerMethod("mouseEvent", this);
        
        mousePressedEvent  = registerEventMethod(PRESS_EVENT, new Class[] { Button.class });
        mouseReleasedEvent = registerEventMethod(RELEASE_EVENT, new Class[] { Button.class });
        mouseClickedEvent  = registerEventMethod(CLICK_EVENT, new Class[] { Button.class });
        mouseoverEvent     = registerEventMethod(MOUSEOVER_EVENT, new Class[] { Button.class });
    }
    
    
    abstract void draw();
    
    public void setColor(int defaultState) {
        setColor(defaultState, defaultState, defaultState);
    }
    
    public void setColor(int defaultState, int mouseOverState) {
        setColor(defaultState, mouseOverState, mouseOverState);
    }
    
    public void setColor(int defaultState, int mouseOverState, int clickedState) {
        defaultColor   = defaultState;
        mouseoverColor = mouseOverState;
        clickedColor   = clickedState;
    }
    
    public final void setSound(String mouseover, String click) {
        if (mouseover == null && click == null) throw new IllegalArgumentException("You must assign at least one valid argument");
        theMinim = new Minim(parent);
        if(mouseover != null) {
            mouseoverSound = theMinim.loadFile(mouseover);
            hasMouseoverSound = true;
        }
        if(click != null) {
            clickSound = theMinim.loadFile(click);
            hasClickSound = true;
        }
    }
    
    protected final void playClickSFX() {
        if (!hasClickSound) return;
        clickSound.rewind();
        clickSound.play();
    }
    
    protected final void playMouseoverSFX() {
        if(isEnabled()){
            if (!hasMouseoverSound) return;
            boolean isFinished = mouseoverSound.position() == mouseoverSound.length();
            if (isFinished) { // did the sound end?
                mouseoverSound.rewind();
                mouseoverSound.pause();
                if (isInside()) fromInside = true;
            }
            
            if (isInside() && !fromInside) {
                if (isFinished == false) { // the sound has not finished
                    mouseoverSound.rewind();
                    mouseoverSound.play();
                    fromInside = true;
                }
                mouseoverSound.play();
            }
        }
    }
    
    public final void setLocked(boolean locked){
        isLocked = locked;
    }
    
    public final boolean isLocked(){
        return isLocked;
    }
    
    public final boolean isPressed(){
        return isPressed;
    }
    
    public final void setVisible(boolean isVisible){
        this.isVisible = isVisible;
    }
    
    public final boolean isVisible(){
        return isVisible;
    }
    
    public final void setEnabled(boolean state){
        enabled = state;
    }
    
    public final boolean isEnabled(){
        return enabled;
    }
    
    public final void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    protected boolean isInside() {
        if (type == CIRCULAR) {
            float d = PApplet.dist(parent.mouseX, parent.mouseY, x, y);
            
            if (d < width/2) return true;
            else {
                fromInside = false;
                return false;
            }
        } else if (type == RECTANGULAR) {
            boolean horizontal = (parent.mouseX >= x - width/2 && parent.mouseX <= x + width/2);
            boolean vertical  = (parent.mouseY >= y - height/2 && parent.mouseY <= y + height/2);
            
            if (horizontal && vertical) return true;
            else {
                fromInside = false;
                return false;
            }
        }
        return false;
    }
    
    public final void mouseEvent(MouseEvent m) {
        // MouseEvent options can be PRESS, RELEASE, CLICK, DRAG, MOVE
        
        if (isEnabled() && isInside() && isVisible) {
            switch (m.getAction()) {
                
                case MouseEvent.PRESS:
                    invokeMethod(mousePressedEvent, new Object[] { this });
                    isPressed = true;
                    playClickSFX();
                    break;
                    
                case MouseEvent.RELEASE:
                    invokeMethod(mouseReleasedEvent, new Object[] { this });
                    isPressed = false;
                    break;
                    
                case MouseEvent.CLICK:
                    invokeMethod(mouseClickedEvent, new Object[] { this });
                    break;
            }
        }
    }
    
    protected final Method registerEventMethod(String methodName, Class... args){
        try {
            return parent.getClass().getMethod(methodName, args);
        } catch(NoSuchMethodException | SecurityException e){
//            System.err.println("Your sketch needs to implement \"void "+methodName+"(Button b)\"");
        }
        return null;
    }
    
    @SuppressWarnings("UnusedAssignment") // for the 'method = null;' line
    protected final void invokeMethod(Method method, Object... args){
        if(method != null) {
            try {
                method.invoke(parent, args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.err.println("failed to call method " + method.toString() + " inside your sketch");
                method = null;
            }
        }
    }
}