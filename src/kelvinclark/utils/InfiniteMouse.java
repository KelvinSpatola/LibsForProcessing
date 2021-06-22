package kelvinclark.utils;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import java.awt.GraphicsEnvironment;
import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Robot;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;


/**
 *TODO:
 * -> contrainning options:
 *      - Mouse-lock (lock the mouse in the middle of window - mode: FULLSCREEN, SPAN, WINDOW/VIEWPORT)
 *
 * -> mouse attributes: sensitivity, isLocked, isVisible ???
 * -> disable method (unregister draw)
 *
 * -> O boundary quando for atribuído de forma manual através do construtor, e a janela estiver em fullScreen,
 * tem de ser recalculado para funcionar de forma relativa ao display escolhido. Caso contrário a aplicação irá
 * rodar num display e o mouse ficará restrito noutro display!!!!!!!!!!
 */


public class InfiniteMouse {
    private Robot robot;
    private Point currLocation, prevLocation;
    private boolean screenWrapping, isLocked;
    private DrawHandler drawHandler = new DrawHandler();
    
    
    public InfiniteMouse(PApplet parent){
        this(parent, true);
    }
    
    public InfiniteMouse(PApplet parent, boolean screenWrapping) {
        this.screenWrapping = screenWrapping;
        currLocation = new Point();
        prevLocation = new Point();
        try { robot = new Robot(); } catch (AWTException e) {}
        
        parent.registerMethod("draw", drawHandler);
        Boundary.init(parent);
        setBounds(Boundary.SPAN);
    }
    
    public final void setBounds(Rectangle bounds) {
        if(bounds == null) return;
        Boundary.setBounds(bounds);
    }
    
    public static class Boundary {
        public static int LEFT, TOP, RIGHT, BOTTOM;
        public static Rectangle SPAN, DISPLAY, WINDOW;
        static int displayIndex;
        
        
        static void init(PApplet p) {
            final GraphicsDevice[] screenDevices = GraphicsEnvironment
                    .getLocalGraphicsEnvironment().getScreenDevices();
            
            
            // ******************* SPAN ******************* //
            SPAN = new Rectangle();
            for(GraphicsDevice device : screenDevices)
                SPAN.add(device.getDefaultConfiguration().getBounds());
            
            
            // ****************** DISPLAY ****************** //
            final int devices = screenDevices.length;
            int sketchDisplay = p.sketchDisplay();
            if(sketchDisplay > devices) sketchDisplay = 0;
            displayIndex = Math.max(1, Math.min(sketchDisplay, devices)) - 1;
            DISPLAY = screenDevices[displayIndex].getDefaultConfiguration().getBounds();
            
            
            // ******************* WINDOW ******************* //
            Point windowLocation = getWindowLocation(p);
            int width = windowLocation.x + p.width;
            int height = windowLocation.y + p.height;
            WINDOW = new Rectangle(windowLocation.x, windowLocation.y, width, height);
        }
        
        static void setBounds(Rectangle bounds) {
            Boundary.LEFT   = bounds.x + 1;
            Boundary.TOP    = bounds.y + 1;
            Boundary.RIGHT  = (displayIndex > 0 ? bounds.x + bounds.width : bounds.width) - 2;
            Boundary.BOTTOM = bounds.height - 2;
        }
        
        static Point getWindowLocation(PApplet p) {
            Point location = new Point();
            Object surf = p.getSurface().getNative();
            
            switch (p.sketchRenderer()) {
                case PConstants.P2D:
                case PConstants.P3D:
                    location.x = ((com.jogamp.nativewindow.NativeWindow) surf).getX();
                    location.y = ((com.jogamp.nativewindow.NativeWindow) surf).getY();
                    return location;
                    
                case PConstants.JAVA2D:
                    final java.awt.Component f = ((processing.awt.PSurfaceAWT.SmoothCanvas) surf).getFrame();
                    location = f.getLocationOnScreen();
                    return location;
                    
                case PConstants.FX2D:
                    try {
                        final java.lang.reflect.Method getStage = surf.getClass().getDeclaredMethod("getStage");
                        getStage.setAccessible(true);
                        
                        final Object stage = getStage.invoke(surf);
                        final Class<?> st = stage.getClass();
                        
                        final java.lang.reflect.Method getX = st.getMethod("getX");
                        final java.lang.reflect.Method getY = st.getMethod("getY");
                        
                        location.x = ((Number) getX.invoke(stage)).intValue();
                        location.y = ((Number) getY.invoke(stage)).intValue();
                        return location;
                    }
                    catch (final ReflectiveOperationException e) {
                        System.err.println(e);
                    }
            }
            return location;
        }
    }
    
    /**
     * This is just a wrapper class to encapsulate <code>draw()</code> and
     * prevent it from being public to the user
     *
     */
    protected class DrawHandler {
        
        public void draw() {
            prevLocation = currLocation.getLocation();
            currLocation = MouseInfo.getPointerInfo().getLocation();
            
            if(isLocked){
                final int centerX = Boundary.LEFT + Boundary.RIGHT / 2;
                final int centerY = Boundary.BOTTOM / 2;
                robot.mouseMove(centerX, centerY);
                return;
            }
            
            if(screenWrapping) {
                
                // hit left side
                if (currLocation.x < Boundary.LEFT) {
                    robot.mouseMove( Boundary.RIGHT, currLocation.y);
                    currLocation.x = Boundary.RIGHT;
                    prevLocation.x = Boundary.RIGHT;
                }
                // hit right side
                if (currLocation.x > Boundary.RIGHT) {
                    robot.mouseMove( Boundary.LEFT, currLocation.y);
                    currLocation.x = Boundary.LEFT;
                    prevLocation.x = Boundary.LEFT;
                }
                // hit top
                if (currLocation.y < Boundary.TOP) {
                    robot.mouseMove( currLocation.x, Boundary.BOTTOM);
                    currLocation.y = Boundary.BOTTOM;
                    prevLocation.y = Boundary.BOTTOM;
                }
                // hit bottom
                if (currLocation.y > Boundary.BOTTOM) {
                    robot.mouseMove( currLocation.x, Boundary.TOP);
                    currLocation.y = Boundary.TOP;
                    prevLocation.y = Boundary.TOP;
                }
            } else {
                
                // hit left side
                if (currLocation.x < Boundary.LEFT) {
                    robot.mouseMove( Boundary.LEFT, currLocation.y);
                    currLocation.x = Boundary.LEFT;
                }
                // hit right side
                if (currLocation.x > Boundary.RIGHT) {
                    robot.mouseMove( Boundary.RIGHT, currLocation.y);
                    currLocation.x = Boundary.RIGHT;
                }
                // hit top
                if (currLocation.y < Boundary.TOP) {
                    robot.mouseMove( currLocation.x, Boundary.TOP);
                    currLocation.y = Boundary.TOP;
                }
                // hit bottom
                if (currLocation.y > Boundary.BOTTOM) {
                    robot.mouseMove( currLocation.x, Boundary.BOTTOM);
                    currLocation.y = Boundary.BOTTOM;
                }
            }
        }
    }
    
    /**
     * The current position of the mouse in regards to the selected display
     *
     * @see <code>setLocation()</code>
     * @see <code>getX()</code>
     * @see <code>getY()</code>
     * @see <code>prevX()</code>
     * @see <code>prevY()</code>
     * @return a PVector with the current location of the mouse.
     */
    public PVector getLocation(){
        return new PVector(currLocation.x, currLocation.y);
    }
    
    /**
     * The current position of the mouse in regards to the selected display
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void setLocation(int x, int y) {
        robot.mouseMove(x, y);
    }
    
    /**
     * The current horizontal coordinate of the mouse.
     *
     * @see <code>getLocation()</code> for detailed description about mouse values
     * @see <code>getY()</code>
     * @return the x coordinate.
     */
    public int getX(){
        return currLocation.x;
    }
    
    /**
     * The current vertical coordinate of the mouse.
     *
     * @see <code>getLocation()</code> for detailed description about mouse values
     * @see <code>getX()</code>
     * @return the current x coordinate.
     */
    public int getY(){
        return currLocation.y;
    }
    
    /**
     * The horizontal position of the mouse in the frame previous to the current frame.
     *
     * @see <code>getLocation()</code> for detailed description about mouse values
     * @see <code>prevY()</code>
     * @return the previous x coordinate.
     */
    public int prevX(){
        return prevLocation.x;
    }
    
    /**
     * The vertical position of the mouse in the frame previous to the current frame.
     *
     * @see <code>getLocation()</code> for detailed description about mouse values
     * @see <code>prevX()</code>
     * @return the previous y coordinate.
     */
    public int prevY(){
        return prevLocation.y;
    }
    
    /**
     * Calculates and returns a new 2D vector with mouse movement direction
     *
     * @return the direction vector of the mouse
     */
    public PVector getDirection() {
        return delta().normalize();
    }
    
    public PVector delta() {
        return new PVector(currLocation.x - prevLocation.x, currLocation.y - prevLocation.y);
    }
    
    /**
     * Calculates and returns the resulting angle of mouse movement (in radians)
     *
     * @return the angle.
     */
    public float getAngle(){
        return (float) Math.atan2(getDirection().y, getDirection().x);
    }
    
    public void lock(){
        isLocked = true;
    }
    
    public void unlock(){
        isLocked = false;
    }
}