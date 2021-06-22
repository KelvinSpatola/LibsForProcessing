package kelvinclark.gui;

import processing.core.PApplet;
import processing.core.PImage;


/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */
public class ImgButton extends Button{
    
    private final PImage[] backgroundImg = new PImage[3]; // [0] = default state, [1] = mouseover state, [2] = clicked state

    
    // CIRCULAR
    public ImgButton(PApplet parent, String defaultImg, float x, float y, int w) { //circular
        super(parent);
        this.x = x;
        this.y = y;
        width  = w;
        height = width;
        
        type = CIRCULAR;
        
        // TODO: implementar o tratamento de exception para o caso de o utilizador nao criar uma pasta "data"
        backgroundImg[0] = parent.loadImage(parent.sketchPath(defaultImg));
        backgroundImg[0].resize((int)width, (int)height);
        backgroundImg[1] = backgroundImg[0];
        backgroundImg[2] = backgroundImg[0];
    }
    
    public ImgButton(PApplet parent, String defaultImg, String mouseoverImg, float x, float y, int w) { //circular
        super(parent);
        this.x = x;
        this.y = y;
        width  = w;
        height = width;
        
        type = CIRCULAR;
        
        // TODO: implementar o tratamento de exception para o caso de o utilizador nao criar uma pasta "data"
        backgroundImg[0] = parent.loadImage(parent.sketchPath(defaultImg));
        backgroundImg[0].resize((int)width, (int)height);
        
        backgroundImg[1] = parent.loadImage(parent.sketchPath(mouseoverImg));
        backgroundImg[1].resize((int)width, (int)height);
        
        backgroundImg[2] = backgroundImg[1];
    }
    
    public ImgButton(PApplet parent, String defaultImg, String mouseoverImg, String clickedImg, float x, float y, int w) { //circular
        super(parent);
        this.x = x;
        this.y = y;
        width  = w;
        height = width;
        
        type = CIRCULAR;
        
        // TODO: implementar o tratamento de exception para o caso de o utilizador nao criar uma pasta "data"
        backgroundImg[0] = parent.loadImage(parent.sketchPath(defaultImg));
        backgroundImg[0].resize((int)width, (int)height);
        
        backgroundImg[1] = parent.loadImage(parent.sketchPath(mouseoverImg));
        backgroundImg[1].resize((int)width, (int)height);
        
        backgroundImg[2] = parent.loadImage(parent.sketchPath(clickedImg));
        backgroundImg[2].resize((int)width, (int)height);
    }
    
    // RECTANGULAR
    public ImgButton(PApplet parent, String defaultImg, float x, float y, int w, int h) {
        super(parent);
        this.x = x;
        this.y = y;
        width  = w;
        height = h;
        
        type = RECTANGULAR;
        
        // TODO: implementar o tratamento de exception para o caso de o utilizador nao criar uma pasta "data"
        backgroundImg[0] = parent.loadImage(parent.sketchPath(defaultImg));
        backgroundImg[0].resize((int)width, (int)height);
        backgroundImg[1] = backgroundImg[0];
        backgroundImg[2] = backgroundImg[0];
    }
    
    public ImgButton(PApplet parent, String defaultImg, String mouseoverImg, float x, float y, int w, int h) { 
        super(parent);
        this.x = x;
        this.y = y;
        width  = w;
        height = h;
        
        type = RECTANGULAR;
        
        // TODO: implementar o tratamento de exception para o caso de o utilizador nao criar uma pasta "data"
        backgroundImg[0] = parent.loadImage(parent.sketchPath(defaultImg));
        backgroundImg[0].resize((int)width, (int)height);
        
        backgroundImg[1] = parent.loadImage(parent.sketchPath(mouseoverImg));
        backgroundImg[1].resize((int)width, (int)height);
        
        backgroundImg[2] = backgroundImg[1];
    }
    
    public ImgButton(PApplet parent, String defaultImg, String mouseoverImg, String clickedImg, float x, float y, int w, int h) {
        super(parent);
        this.x = x;
        this.y = y;
        width  = w;
        height = h;
        
        type = RECTANGULAR;
        
        // TODO: implementar o tratamento de exception para o caso de o utilizador nao criar uma pasta "data"
        backgroundImg[0] = parent.loadImage(parent.sketchPath(defaultImg));
        backgroundImg[0].resize((int)width, (int)height);
        
        backgroundImg[1] = parent.loadImage(parent.sketchPath(mouseoverImg));
        backgroundImg[1].resize((int)width, (int)height);
        
        backgroundImg[2] = parent.loadImage(parent.sketchPath(clickedImg));
        backgroundImg[2].resize((int)width, (int)height);
    }

    
    @Override
    public void draw(){
        if(isVisible){
            parent.pushStyle();
            parent.imageMode(displayMode);
            
            int imgIndex;
            
            if (isLocked) imgIndex = 2;
            else {
                if (isInside()) imgIndex = isPressed ? 2 : 1;
                else imgIndex = 0;
            }
            parent.imageMode(CENTER);
            parent.image(backgroundImg[imgIndex], x, y, width, height);
            
            parent.popStyle();
            
            if (isInside() &&  isEnabled()) invokeMethod(mouseoverEvent, new Object[] { this });
            playMouseoverSFX();
        }
    }
    
}