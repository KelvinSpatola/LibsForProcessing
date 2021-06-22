package kelvinclark.utils;

import processing.core.PApplet;
import processing.core.PMatrix2D;
import processing.core.PVector;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */


public class Rectangle {
    private final PApplet parent;
    private PMatrix2D inverseMatrix = new PMatrix2D(); // this is the inverted matrix. It stores the original coordinate system
    public PVector[] vertices = new PVector[4];
    
    public float width, height;
    private float offset;
    
    
    
    // CONSTRUCTORS
    public Rectangle(PApplet parent){
        this(parent, 0, 0);
    }
    
    // squares
    public Rectangle(PApplet parent, float width) {
        this(parent, width, width);
    }
    
    // rectangles
    public Rectangle(PApplet parent, float width, float height) {
        this.parent = parent;
        this.width = width;
        this.height = height;
        
        for(int i = 0; i < vertices.length; i++) vertices[i] = new PVector();
    }
    
    
    // update the matrices and render the shape
    public void draw(){
        updateMatrices();
        parent.rect(0, 0, width, height);
    }
    
    private void updateMatrices(){
        // this is the rendering matrix, where the rectangle is drawn
        PMatrix2D renderingMatrix = (PMatrix2D) parent.g.getMatrix();
        // whenever we update the original matrix, we calculate its inverse as well
        inverseMatrix = renderingMatrix.get(); // get a copy the rendering matrix
        inverseMatrix.invert(); // invert the matrix to obtain the reverse order of the accumulated transformations
        
        offset = (parent.g.rectMode == 3 ? 0.5f : 0); // 3 --> PApplet.CENTER
        
        float mx = renderingMatrix.m02;
        float my = renderingMatrix.m12;
        
        float ax = mx + (-width * offset);
        float ay = my + (-height * offset);
        
        float bx = mx + (width - width * offset);
        float by = my + (-height * offset);
        
        float cx = mx + (width - width * offset);
        float cy = my + (height - height * offset);
        
        float dx = mx + (-width * offset);
        float dy = my + (height - height * offset);
        
        PApplet.println(ax, ay);
        PApplet.println(bx, by);
        PApplet.println(cx, cy);
        PApplet.println(dx, dy);
        PApplet.println();
        
        ax = parent.screenX(-width * offset, -height * offset);
        ay = parent.screenY(-width * offset, -height * offset);
        bx = parent.screenX( width - width * offset, -height * offset);
        by = parent.screenY( width - width * offset, -height * offset);
        cx = parent.screenX( width - width * offset,  height - height * offset);
        cy = parent.screenY( width - width * offset,  height - height * offset);
        dx = parent.screenX(-width * offset, height - height * offset);
        dy = parent.screenY(-width * offset, height - height * offset);
        
        PApplet.println(ax, ay);
        PApplet.println(bx, by);
        PApplet.println(cx, cy);
        PApplet.println(dx, dy);
        PApplet.println();
        
        
        vertices[0].set(ax, ay); // top left
        vertices[1].set(bx, by); // top right
        vertices[2].set(cx, cy); // bottom left
        vertices[3].set(dx, dy); // bottom right
    }
    
//    private PVector screenPos(float x, float y) {
//        PMatrix2D mt = (PMatrix2D) getMatrix();
//        return new PVector(mt.m02 + x, mt.m12 + y);
//    }
    
    public boolean contains(PVector point){
        return contains(point.x, point.y);
    }
    
    public boolean contains(float x, float y) {
        PVector transformedPoint = new PVector();
        // now lets finally apply the transformations obtained by the inverse matrix to the point passed as argument.
        inverseMatrix.mult(new PVector(x, y), transformedPoint);
        
        // check if the transformed point is bounded into the rectangle on its original (inverted) state
        boolean h = (transformedPoint.x >= (-width * offset) && transformedPoint.x <= width - (width * offset));
        boolean v = (transformedPoint.y >= (-height * offset) && transformedPoint.y <= height - (height * offset));
        return h && v;
    }
    
    public boolean contains(Rectangle other){
        for(PVector v : other.vertices) {
            if(!contains(v.x, v.y)) return false;
        }
        return true;
    }
    
    public boolean intersects(Rectangle other){
        for(PVector v : this.vertices) {
            if(other.contains(v.x, v.y)) return true;
        }
        return false;
    }
    
    
    public Rectangle copy(){
        return new Rectangle(this.parent, this.width, this.height);
    }
    
    @Override
    public String toString() {
        return "width = " + width + ", height = " + height;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Float.floatToIntBits(this.width);
        hash = 23 * hash + Float.floatToIntBits(this.height);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rectangle other = (Rectangle) obj;
        if (Float.floatToIntBits(this.width) != Float.floatToIntBits(other.width)) {
            return false;
        }
        return Float.floatToIntBits(this.height) == Float.floatToIntBits(other.height);
    }
}