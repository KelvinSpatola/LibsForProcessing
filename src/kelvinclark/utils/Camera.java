package kelvinclark.utils;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import processing.core.PApplet;
import processing.core.PConstants;
import static processing.core.PConstants.LEFT;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.event.MouseEvent;
import processing.opengl.*;


/**
 * TODO: recalcular o vetor lookAt para redirecioná-lo para o sitio onde estou a "tocar" com o raio ortogonal ao plano da camera.
 * Para ja, o vetor lookAt está sempre fixo no centro da camera à distancia do camRadius.
 *
 */


public final class Camera implements PConstants {
    public static final float DEFAULT_POLAR   = 70f * DEG_TO_RAD;
    public static final float DEFAULT_AZIMUTH = 45f * DEG_TO_RAD;
    public static final float MIN_FOV = 45f * DEG_TO_RAD;
    public static final float MAX_FOV = 70f * DEG_TO_RAD;
    public static final float MIN_DISTANCE = 10f;
    public static final float MAX_DISTANCE = 100_000f;
    
    
    public static enum View { ISO, FRONT, LEFT, BACK, RIGHT, TOP, BOTTOM, CUSTOM }
    private View view = View.ISO;
    
    public static enum Projection { ORTHOGRAPHIC, PERSPECTIVE }
    private Projection proj = Projection.PERSPECTIVE;
    
    private float polar = DEFAULT_POLAR; // the polar (vertical angle)
    private float azimuth = DEFAULT_AZIMUTH;   // the Azimuth angle (horizontal angle)
    private float minPolar = -1f;
    private float maxPolar = -1f;
    
    private final PVector camPos = new PVector();
    private PVector lookAt = new PVector();
    private final PVector camUp = new PVector(0, 0, -1);
    
    public float camRadius;
    public float zoom = 1.0f;
    private float fov = MIN_FOV;
    private final float ASPECT_RATIO;
    private final float NEAR_CLIPPING_PLANE = 0.1f;
    public float far_clipping_plane;
    
    private float sensitivity = 0.1f;
    
    private final EventListener eventListener = new EventListener();
    private boolean isActive;
    private boolean inverse, viewing;
    
    private final PApplet parent;
    private final PMatrix3D camInvMat = new PMatrix3D();
    
    
    
    // CONSTRUCTOR
    //@SuppressWarnings("LeakingThisInConstructor")
    public Camera(PApplet parent, float radius){
        this.parent = parent;
        this.camRadius = Math.abs(radius);
        
        ASPECT_RATIO = (float)parent.width / (float)parent.height;
        zoom(1); // preciso corrigir isto!
        
        setActive(true);
    }
    
    
    
    public void draw() {
        switch(proj) {
            case ORTHOGRAPHIC:
                parent.ortho(-parent.width/2 * zoom, parent.width/2 * zoom,
                        -parent.height/2 * zoom, parent.height/2 * zoom);
                break;
            case PERSPECTIVE:
                float cameraZ = (float) ((parent.height/2f) / Math.tan(fov/2f));
                far_clipping_plane = cameraZ * camRadius;
                parent.perspective(getFov(), ASPECT_RATIO, NEAR_CLIPPING_PLANE, far_clipping_plane);
                break;
        }
        
        //                   *** SPHERICAL COORDINATE SYSTEM ***
        // P(x,y,z) = (r*sin(polar)*cos(azimuth), r*sin(polar)*sin(azimuth), r*cos(polar))
        if(!viewing){
            float x = (float) (lookAt.x + camRadius * Math.sin(polar) * Math.cos(azimuth));
            float y = (float) (lookAt.y + camRadius * Math.sin(polar) * Math.sin(azimuth));
            float z = (float) (lookAt.z + camRadius * Math.cos(polar));
            camPos.set(x, y, z);
        }
        parent.camera(camPos.x, camPos.y, camPos.z, lookAt.x, lookAt.y, lookAt.z, camUp.x, camUp.y, camUp.z);
        camInvMat.set(((PGraphicsOpenGL)parent.g).cameraInv);
    }
    
    
    
    private void orbit(final float dx, final float dy) {
        viewing = false;
        
        azimuth += DEG_TO_RAD * (inverse ? dx : -dx) * sensitivity;
        azimuth %= TWO_PI;
        
        polar += DEG_TO_RAD * dy * sensitivity;
        
        if(minPolar > -1f && maxPolar > -1f) polar = clamp(polar, minPolar, maxPolar);
        
        if (Math.round(polar * RAD_TO_DEG) % 180 == 0) {
            inverse = !inverse;
            polar = -polar;
            camUp.z = -camUp.z;
        }
    }
    
    private void pan(final float dx, final float dy) {
        viewing = false;
        
        PVector delta = new PVector(dx, dy, -camRadius).mult(1);
        lookAt = camInvMat.mult(delta, null);
    }
    
    private void view(final float dx, final float dy) {
        viewing = true;
        
        PVector lookAtInv = new PVector(-dx, -dy, -camRadius);
        lookAt = camInvMat.mult(lookAtInv, null);
//        azimuth += DEG_TO_RAD * (inverse ? dx : -dx) * sensitivity;
//        azimuth %= TWO_PI;
//
//polar += DEG_TO_RAD * dy * sensitivity;
//polar = clamp(polar, DEG_TO_RAD, PI - DEG_TO_RAD);
//        if (Math.round(polar * RAD_TO_DEG) % 180 == 0) {
//            inverse = !inverse;
//            polar = -polar;
//            camUp.z = -camUp.z;
//        }
    }
    
    public void zoom(final int delta){        
        float zoomSpeed = PApplet.map(camRadius, MIN_DISTANCE, MAX_DISTANCE, 1f, 5000f);
        
        camRadius += delta * zoomSpeed;
        camRadius = clamp(camRadius, MIN_DISTANCE, MAX_DISTANCE);
        
        fov = PApplet.map(camRadius, MIN_DISTANCE, MAX_DISTANCE, MAX_FOV, MIN_FOV);
    }
    
    
    public class EventListener {
        float mouseX, mouseY, pmouseX, pmouseY;
        
        public final void mouseEvent(final MouseEvent e) {
            final int action = e.getAction();
            final int button = e.getButton();
            
            if (action == MouseEvent.PRESS || action == MouseEvent.DRAG) {
                pmouseX = mouseX;
                pmouseY = mouseY;
                mouseX = e.getX();
                mouseY = e.getY();
            }
            
            switch (e.getAction()) {
                case MouseEvent.PRESS:
                    isActive = true;
                    break;
                    
                case MouseEvent.RELEASE:
                    isActive = false;
                    //viewing = false; para resolver o problema do zoom quando é antecedido pelo view() ?
                    break;
                    
                case MouseEvent.WHEEL:
                    zoom(e.getCount());
                    break;
                    
                case MouseEvent.DRAG:
                    if (isActive) {
                        final float dx = pmouseX - mouseX;
                        final float dy = pmouseY - mouseY;
                        
                        if (button == LEFT) orbit(dx, dy);
                        if (button == RIGHT) view(dx, dy);
                        if (button == CENTER) pan(dx, dy);
                        break;
                    }
            }
        }
    }
    
    public void setActive(final boolean state) {
        if (state == isActive) return;
        
        isActive = state;
        if (isActive) {
            parent.registerMethod("mouseEvent", eventListener);
            parent.registerMethod("draw", this);
        } else {
            parent.unregisterMethod("mouseEvent", eventListener);
            parent.unregisterMethod("draw", this);
        }
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    
    
    public Camera setCameraView(final View v) {
        //if(v == view) return this;
        
        view = v;
        setPolar(DEFAULT_POLAR);
        
        switch(view) {
            case ISO:    setAzimuth(QUARTER_PI); break;
            case FRONT:  setAzimuth(HALF_PI); break;
            case LEFT:   setAzimuth(PI); break;
            case BACK:   setAzimuth(3 * HALF_PI); break;
            case RIGHT:  setAzimuth(0); break;
            case TOP:    setPolar(DEG_TO_RAD); break;
            case BOTTOM: setPolar(PI - DEG_TO_RAD); break;
            case CUSTOM:
                setPolar(DEFAULT_POLAR);
                setAzimuth(DEFAULT_AZIMUTH);
                break;
        }
        return this;
    }
    
    public View getView(){
        return view;
    }
    
    public Camera setProjection(Projection p) {
        proj = p;
        return this;
    }
    
    public Projection getCameraProjection(){
        return proj;
    }
    
    
    
    public Camera setPolar(float angle){
        polar = angle;
        return this;
    }
    
    public float getPolar() {
        return polar;
    }
    
    public Camera setAzimuth(float angle){
        azimuth = angle;
        return this;
    }
    
    public float getAzimuth() {
        return azimuth;
    }
    
    public Camera setFov(float angle){
        if(angle < 0) angle = 0;
        fov = angle;
        return this;
    }
    
    public float getFov(){
        return fov * zoom;
    }
    
    public float getRadius(){
        return camRadius;
    }
    
    
    
    public Camera setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
        return this;
    }
    
    public float getSensitivity() {
        return sensitivity;
    }
    
    
    
    public Camera setPosition(PVector pos){
        setPosition(pos.x, pos.y, pos.z);
        return this;
    }
    
    public Camera setPosition(float x, float y, float z){
        camPos.set(x,y,z);  // nao seria melhor mudar para camPos = new PVector(x, y, z); ?
        //camRadius = x; // ??????
        return this;
    }
    
    public PVector getPosition(){
        return camPos;
    }
    
    public Camera setLookAt(PVector pos){
        setLookAt(pos.x, pos.y, pos.z);
        return this;
    }
    
    public Camera setLookAt(float x, float y, float z){
        lookAt.set(x, y, z);
        return this;
    }
    
    public PVector getLookAt(){
        return lookAt;
    }
    
    public Camera setCameraUp(float upX, float upY, float upZ){
        camUp.set(upX, upY, upZ);
        return this;
    }
    
    public PVector getCameraUp(){
        return camUp;
    }
    
    
    
    public void changeLookAtTo(float x, float y, float z){
        changeLookAtTo(new PVector(x, y, z));
    }
    
    public void changeLookAtTo(PVector newLookAt){
        camRadius = PVector.dist(camPos, newLookAt);
        lookAt.set(newLookAt);
    }
    
    
    public void beginHUD(){
        parent.hint(DISABLE_DEPTH_TEST);
        parent.push();
        parent.resetMatrix();
        parent.ortho(0, parent.width, -parent.height, 0);
        parent.noLights();
    }
    
    public void endHUD(){
        parent.pop();
        parent.hint(ENABLE_DEPTH_TEST);
    }
    
    public void beginFacingScreen(){
        parent.push();
        parent.rotateX(-PI/2);
        parent.rotateY(-getAzimuth() + HALF_PI);
        parent.rotateX(-getPolar() + HALF_PI);
    }
    
    public void endFacingScreen(){
        parent.pop();
    }
    
    
    
    public Camera clampPolar(float min, float max){
        minPolar = min;
        maxPolar = max;
        return this;
    }
     
    private float clamp(float val, float min, float max) {
        if (val > max) return max;
        if (val < min) return min;
        return val;
    }
}
