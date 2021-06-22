
package kelvinclark.utils;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import processing.core.PApplet;
import processing.core.PImage;


public final class SpriteSheet {
    private final PApplet parent;
    
    public static final int DEFAULT_FPS = 60; // one sprite per second
    private int frameRate = DEFAULT_FPS;
    
    private PImage[] sprites;
    private int currentRow, currentCol;
    private final int rowsCount;
    private final int colsCount;
    
    public int width, height;
    
    
    // CONSTRUCTORS
    private SpriteSheet(PApplet parent, int rows, int cols) {
        if (rows < 1 || cols < 1)  throw new IllegalArgumentException("values for this parameter must be > 0");
        
        this.parent = parent;
        rowsCount = rows;
        colsCount = cols;
        sprites = new PImage[rows * cols];
    }
    
    public SpriteSheet(PApplet parent, String path, int rows, int cols) {
        this(parent, path, rows, cols, DEFAULT_FPS);
    }
    
    public SpriteSheet(PApplet parent, String path, int rows, int cols, int fps) {
        this(parent, rows, cols);
        createSprites(path);
        setFrameRate(fps);
    }
    
    public SpriteSheet copy() {
        SpriteSheet copy = new SpriteSheet(parent, rowsCount, colsCount);
        copy.width  = this.width;
        copy.height = this.height;
        
        for(int i = 0; i < copy.sprites.length; i++){
            copy.sprites[i] = new PImage(copy.width, copy.height, PApplet.ARGB, parent.pixelDensity);
        }
        
        for(int i = 0; i < sprites.length; i++){
            getImpl(sprites[i], 0, 0, width, height, copy.sprites[i], 0, 0);
        }
        
        copy.setFrameRate(this.getFrameRate());
        return copy;
    }
    
    
    private void createSprites(String path) {
        PImage src = parent.loadImage(path);
        //sprites = new PImage[rowsSize() * columnsSize()];
        
        width  = (int)(src.width/colsCount);
        height = (int)(src.height/rowsCount);
        
        for (int i = 0; i < sprites.length; i++) {
            int col = (i % colsCount) * width;
            int row = (i / colsCount) * height;
            sprites[i] = src.get(col, row, width, height);
            //getImpl(sprites[i], col, row, width, height, src, col, row);
            
        }
    }
    
    private void getImpl(PImage src, int srcX, int srcY, int srcWidth, int srcHeight, PImage target, int targetX, int targetY) {
        int sourceIndex = srcX + srcY * src.width;
        int targetIndex = targetX + targetY * width;
        
        for (int row = 0; row < srcHeight; row++) {
            System.arraycopy(src.pixels, sourceIndex, target.pixels, targetIndex, srcWidth);
            sourceIndex += src.width;
            targetIndex += target.width;
        }
    }
    
    
    //This is the redenring method
    public PImage render() {
        return sprites[currentCol + currentRow * colsCount];
    }
    
    public SpriteSheet getRow(int row, int start, int end) {
        currentRow = row;
        if (parent.frameCount % frameRate == 0) {
            currentCol++;
        }
        
        if (currentCol < start || currentCol > end) currentCol = start;
        return this;
    }
    
    public SpriteSheet getRow(int row) {
        getRow(row, 0, colsCount-1);
        return this;
    }
    
    public SpriteSheet getRows(){
        if (parent.frameCount % frameRate == 0) {
            currentCol++;
            
            if(currentCol > colsCount-1) {
                currentCol = 0;
                currentRow++;
                if (currentRow > rowsCount-1) currentRow = 0;
            }
        }
        return this;
    }
    
    public void setFrameRate(int fps) {
        if (fps < 1) return;
        frameRate = fps;
    }
    
    public int getFrameRate(){
        return frameRate;
    }
    
    public void setCurrentFrame(int row, int col) {
        this.currentRow = row;
        this.currentCol = col;
    }
    
    public int currentRow() {
        return currentRow;
    }
    
    public int currentColumn() {
        return currentCol;
    }
    
    public int rowsCount() {
        return rowsCount;
    }
    
    public int columnsCount() {
        return colsCount;
    }
    
    public PImage[] getSprites(){
        return sprites;
    }
    
    public SpriteSheet resizeSprites(int w, int h) {
        width = w;
        height = h;
        for (PImage sprite : sprites) {
            sprite.resize(w, h);
        }
        return this;
    }
    
    
    private void arrayCopy(PImage[] source, PImage[] destination) {
        destination = new PImage[source.length];
        
        for (int i = 0; i < source.length; i++) {
            destination[i] = source[i].get();
        }
    }
}