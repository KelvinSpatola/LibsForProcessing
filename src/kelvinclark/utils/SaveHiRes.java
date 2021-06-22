package kelvinclark.utils;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import static processing.core.PConstants.JAVA2D;


/*
 * TODO:
 * - ao criar um screenshot com o saveFrame, fazer abrir a imagem launch(sketchPath(filename)) do resultado automaticamente;
 * -
 */

public class SaveHiRes {
    private static final SaveHiRes instance = new SaveHiRes();
    private static PApplet parent;
    private static byte counter;
    
    private static PGraphics recorder;
    private static boolean isReady, hasOutput;
    private static float scaleFactor = 1;
    private static String fileName;
    
    public static int WIDTH, HEIGHT;
    
    
    // CONSTRUCTOR
    private SaveHiRes() {  }
    
    public static SaveHiRes createRecord(PApplet papplet, float scale) {
        if (counter++ < 1) { // para evitar looping eterno
            parent = papplet;
            scaleFactor = scale;
            WIDTH  = (int)(parent.width * scaleFactor);
            HEIGHT = (int)(parent.height * scaleFactor);
            
            recorder = parent.createGraphics(WIDTH, HEIGHT, JAVA2D);
            beginRecord();
            parent.setup();
            endRecord();
            isReady = true;
        }
        return instance;
    }
    
    
    public static void beginRecord(){
        parent.beginRecord(recorder);
        recorder.scale(scaleFactor);
    }
    
    public static void endRecord(){
        parent.endRecord();
    }
    
    public void saveFrame() {
        saveFrame(createFileName());
    }
    
    public void saveFrame(String fileName){
        if(!hasOutput){
            beginRecord();
            parent.draw();
            endRecord();
            recorder.save(fileName);
            hasOutput = true;
            
            System.out.println('"'+fileName+'"' + " saved to: " + parent.sketchPath());
            System.out.println(this);
        }
    }
    
    //    public void saveRecord() {
//        saveRecord(createFileName());
//    }
//
//    public void saveRecord(String fileName) {
//        recorder.save(fileName);
//        hasOutput = true;
//
//        System.out.println('"'+fileName+'"' + " saved to: " + parent.sketchPath());
//        System.out.println(this);
//    }
    
    
    private String createFileName(){
        String dateString = String.format("%d-%02d-%02d %02d.%02d.%02d",
                PApplet.year(), PApplet.month(), PApplet.day(), PApplet.hour(), PApplet.minute(), PApplet.second());
        fileName = dateString + ".png";
        return fileName;
    }
    
    public String getFileName(){
        return fileName;
    }
    
    public PImage getPImage(){
        if(!hasOutput()) throw new IllegalStateException("Make sure you call \".saveFrame(filename)\" first in order to create a PImage object.");
        return recorder.get();
    }
    
    public boolean hasOutput(){
        return hasOutput;
    }
    
    public static boolean isReady(){
        return isReady;
    }
    
    @Override
    public String toString(){
        return "recorder dimensions: " + WIDTH + " x " + HEIGHT;
    }
}