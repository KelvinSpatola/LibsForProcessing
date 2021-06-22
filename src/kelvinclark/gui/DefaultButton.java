package kelvinclark.gui;

import processing.core.PApplet;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */
public class DefaultButton extends Button{
    private boolean hasStroke = true;
    private int strokeColor = 0;
    private float strokeWeight = 1;
    
    public DefaultButton(PApplet parent, float x, float y, int w){
        super(parent);
        this.x = x;
        this.y = y;
        width  = w;
        height = width;
        
        type = CIRCULAR;
    }
    
    public DefaultButton(PApplet parent, float x, float y, int w, int h){
        super(parent);
        this.x = x;
        this.y = y;
        width  = w;
        height = h;
        
        type = RECTANGULAR;
    }
    
    @Override
    public void draw() {
        if(isVisible){
            parent.pushStyle();
            
            if(hasStroke) {
                parent.stroke(strokeColor);
                parent.strokeWeight(strokeWeight);
            }
            else parent.noStroke();
            
            if (isLocked) parent.fill(clickedColor);
            else parent.fill(isInside() ? (isPressed ? clickedColor : mouseoverColor) : defaultColor);
            
            if (type == CIRCULAR) parent.ellipse(x, y, width, width);
            else if (type == RECTANGULAR) {
                parent.rectMode(displayMode);
                parent.rect(x, y, width, height);
            }
            
            parent.popStyle();
            
            if (isInside() &&  isEnabled()) invokeMethod(mouseoverEvent, new Object[] { this });
            playMouseoverSFX();
            
        }
    }
    
    public void noStroke(){
        hasStroke = false;
    }
    public void stroke(int grayColor) {
        strokeColor = grayColor;
        hasStroke = true;
    }
    public void stroke(int red, int green, int blue) {
        strokeColor = parent.color(red, green, blue);
        hasStroke = true;
    }
    public void strokeWeight(float weight){
        strokeWeight = weight;
    }
    
}