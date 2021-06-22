package kelvinclark.gui;

import processing.core.PApplet;
import processing.core.PFont;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */
public class TextButton extends Button {
    
    private final String defaultStateText;
    private final String mouseoverStateText;
    private final String clickedStateText;
    private float textSize;
    
    private int textAlignment = LEFT;
    private float xoff;
    
    private boolean hasUnderline;
    
    private final String DEFAULT_FONT = "Lucida Sans";
    private PFont textFont;
   
    
    // CONSTRUCTORS
    public TextButton(PApplet parent, String defaultStateText, float size, float x, float y) {
        super(parent);
        this.defaultStateText = defaultStateText;
        this.mouseoverStateText = defaultStateText;
        this.clickedStateText = defaultStateText;
        updateValues(size);
        this.x = x;
        this.y = y;
        setFont(DEFAULT_FONT);
    }
    
    public TextButton(PApplet parent, String defaultStateText, String mouseoverStateText, float size, float x, float y) {
        super(parent);
        this.defaultStateText = defaultStateText;
        this.mouseoverStateText = mouseoverStateText;
        this.clickedStateText = mouseoverStateText;
        updateValues(size);
        this.x = x;
        this.y = y;
        setFont(DEFAULT_FONT);
    }
    
    public TextButton(PApplet parent, String defaultStateText, String mouseoverStateText, String clickedStateText, float size, float x, float y) {
        super(parent);
        this.defaultStateText = defaultStateText;
        this.mouseoverStateText = mouseoverStateText;
        this.clickedStateText = clickedStateText;
        updateValues(size);
        this.x = x;
        this.y = y;
        setFont(DEFAULT_FONT);
    }
    
    @Override
    public void draw(){
        if(isVisible){
            parent.pushStyle();
            parent.textFont(textFont, textSize);
            parent.textAlign(textAlignment);
            
            String textToDisplay;
            int colorToFill;
            
            if (isLocked) {
                textToDisplay = clickedStateText;
                colorToFill   = clickedColor;
            }
            else {
                if (isInside()) {
                    textToDisplay = isPressed ? clickedStateText : mouseoverStateText;
                    colorToFill   = isPressed ? clickedColor : mouseoverColor;
                }
                else {
                    textToDisplay = defaultStateText;
                    colorToFill   = defaultColor;
                }
            }
            parent.fill(colorToFill);
            parent.text(textToDisplay, x, y);
            
            if(hasUnderline){
                parent.strokeCap(PApplet.SQUARE);
                parent.stroke(colorToFill);
                parent.strokeWeight(textSize/10);
                parent.line(x + xoff, y + textSize/10, x + width + xoff, y + textSize/10);
            }
            parent.popStyle();
            
            if (isInside() &&  isEnabled()) invokeMethod(mouseoverEvent, new Object[] { this });
            playMouseoverSFX();
        }
    }
    
    public final void setFont(String fontName){
        parent.pushStyle();
        textFont = parent.createFont(fontName, textSize);
        parent.textFont(textFont);
        updateValues(textSize);
        parent.popStyle();
    }
    
    public final void textAlign(int alignment){
        switch (alignment) {
            case LEFT:
                xoff = 0;
                break;
            case CENTER:
                xoff = -width/2;
                break;
            case RIGHT:
                xoff = -width;
                break;
            default:
                xoff = 0;
                System.err.println("textAlign() can only be LEFT, CENTER or RIGHT. Alignment will be asigned with the default value of LEFT");
                break;
        }
        this.textAlignment = alignment;
    }
    
    public void setUnderline(boolean state){
        this.hasUnderline = state;
    }
    
    private void updateValues(float size){
        parent.pushStyle();
        textSize = size;
        parent.textSize(size);
        width  = parent.textWidth(defaultStateText);
        height = (parent.textAscent() + parent.textDescent()) * 0.6f;
        textAlign(textAlignment);
        parent.popStyle();
    }
    
    @Override
    public boolean isInside() {
        boolean horizontal = parent.mouseX >= x + xoff && parent.mouseX <= x + width + xoff;
        boolean vertical   = parent.mouseY >= y - height && parent.mouseY <= y + (hasUnderline ? textSize/10 : 0);
        if (horizontal && vertical) return true;
        else {
            fromInside = false;
            return false;
        }
    }
}