package kelvinclark.utils;


/**
 * Utility class that allows the creation of custom events that helps user to manipulate the flow of code written inside draw().
 * This is useful to prevent draw() from making multiple calls to a block of code that is within an active condition.
 *
 * @author Kelvin Clark Spátola
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import processing.core.PApplet;
import processing.event.KeyEvent;


public class Toggle {
    private static final HashSet<String> SKETCH_METHODS = new HashSet();
    private static final LinkedHashMap<Character, MethodWrapper> methodWrapperMap = new LinkedHashMap();
    private static boolean disableDraw;
    private static PApplet papplet;
    private static Toggle instance;
    private KeyEventHandler keyEventHandler = new KeyEventHandler();
    private DrawHandler drawHandler = new DrawHandler();
    
    
    /**
     * Initializes the class
     *
     * @param sketch
     */

    
    public Toggle(PApplet sketch) {
        if (instance != null) return;
        instance = this;
        
        papplet = sketch;
        
        papplet.registerMethod("keyEvent", keyEventHandler);
        papplet.registerMethod("draw", drawHandler);
        
        for(Method m : papplet.getClass().getMethods()) SKETCH_METHODS.add(m.getName());
    }
    
    
    public Toggle set(final char ch, final String methodName) {
        return set(ch, papplet, methodName, false, new Object[]{});
    }
    
    public Toggle set(final char ch, final String methodName, final boolean state) {
        return set(ch, papplet, methodName, state, new Object[]{});
    }
    
    public Toggle set(final char ch, final Object obj, final String methodName) {
        return set(ch, obj, methodName, false, new Object[]{});
    }
    // vvv PRECISO CORRIGIR ESSES SETTERS PARA ABARCAREM TODAS AS COMBINAÇOES POSSÍVEIS
    public Toggle set(final char ch, final Object obj, final String methodName, final boolean state) {
        return set(ch, obj, methodName, state, new Object[]{});
    }
    
    public Toggle set(final char ch, final String methodName, final boolean state, Object...values) {
        return set(ch, papplet, methodName, state, values);
    }//^^^
    
    public Toggle set(final char ch, final Object obj, final String methodName, final boolean state, Object...values) {
        if(methodName == null || obj == null) {
            throw new IllegalArgumentException("You cannot pass a null value as an argument.");
        }
        
        if(!(obj instanceof PApplet)){
            for(Method m : obj.getClass().getDeclaredMethods()) SKETCH_METHODS.add(m.getName());
        }
        
        if(!SKETCH_METHODS.contains(methodName)) {
            throw new IllegalArgumentException("There is no public void " + methodName + "() method inside " + obj.getClass().getName() + " class");
        }
        
        if(methodWrapperMap.containsKey(ch)) {
            throw new IllegalArgumentException("You can only use one key for each method.");
        }
        
        methodWrapperMap.values().stream().filter(m -> m.getName().equals(methodName)).forEachOrdered(m -> {
            throw new IllegalArgumentException("You cannot assign more than one key to " + methodName + "() method.");
        });
        
        methodWrapperMap.put(ch, new MethodWrapper(obj, methodName, state, values));
        return this;
    }
    
    protected class DrawHandler {
        public void draw() {
            methodWrapperMap.values().stream().filter(m -> m.isActive && !m.isSelect).forEachOrdered(m -> {
                m.invokeMethod();
            });
        }
    }
    
    public void get() {
        if(!disableDraw) {
            papplet.unregisterMethod("draw", drawHandler);
            disableDraw = true;
        }
        drawHandler.draw();
    }
    
    public void get(final String methodName) {
        if(!MethodWrapper.contains(methodName)) {
            System.err.println("There is no " + methodName + " added in the list");
            return;
        }
        
        methodWrapperMap.values().forEach(m -> {
            if(m.getName().equals(methodName)){
                m.isSelect = true;
                
                if(m.isActive) {
                    m.invokeMethod();
                }
            }
        });
    }
    
    protected class KeyEventHandler {
        public void keyEvent(KeyEvent e) {
            if(e.getAction() == KeyEvent.PRESS) handle(e.getKey());
        }
    }
    
    private void handle(char key) {
        if (!methodWrapperMap.containsKey(key)) return;
        
        MethodWrapper wrapper = methodWrapperMap.get(key);
        wrapper.isActive = !wrapper.isActive;
    }
    
    private static final class MethodWrapper {
        static final HashSet<String> addedMethods = new HashSet();
        static final Map<Class<?>, Class<?>> WRAPPER_TYPES = new HashMap<>();
        Object[] values;
        Object parent;
        Method method;
        boolean isActive, isSelect;
        
        static {
            WRAPPER_TYPES.put(Boolean.class, boolean.class);
            WRAPPER_TYPES.put(Byte.class, byte.class);
            WRAPPER_TYPES.put(Character.class, char.class);
            WRAPPER_TYPES.put(Double.class, double.class);
            WRAPPER_TYPES.put(Float.class, float.class);
            WRAPPER_TYPES.put(Integer.class, int.class);
            WRAPPER_TYPES.put(Long.class, long.class);
            WRAPPER_TYPES.put(Short.class, short.class);
            WRAPPER_TYPES.put(Void.class, void.class);
        }
        
        MethodWrapper(Object parent, String methodName, boolean isActive, Object...values) {
            this.parent = parent;
            this.isActive = isActive;
            this.values = values;
            
            try {
                Class<?>[] args = new Class[values.length];
                
                for(int i = 0; i < values.length; i++){
                    Class<?> c = values[i].getClass();
                    if(isWrapperType(c)) c = convertToPrimitive(c);
                    args[i] = c;
                }
                
                
                if(parent instanceof PApplet) {
                    method = parent.getClass().getMethod(methodName, args);
                }
                else{
                    method = parent.getClass().getDeclaredMethod(methodName, args);
                    method.setAccessible(true);
                }
                addedMethods.add(methodName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException("The method \"" + methodName + "\" must be void and receive no parameters.", e);
            }
        }
        
        void invokeMethod() {
            try {
                method.invoke(parent, values);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.err.println("An error occurred while trying to invoke " + getName() + "() method from the " + parent.getClass().getName() + " class.");
            }
        }
        
        boolean isWrapperType(Class<?> clazz) {
            return WRAPPER_TYPES.containsKey(clazz);
        }
        
        Class<?> convertToPrimitive(Class<?> clazz){
            return WRAPPER_TYPES.get(clazz);
        }
        
        String getName(){
            return method.getName();
        }
        
        static boolean contains(String method){
            return addedMethods.contains(method);
        }
    }
    
    @Override
    public String toString() {
        String header, results = "";
        String[] methods = new String[methodWrapperMap.size()];
        boolean[] state = new boolean[methodWrapperMap.size()];
        int i = 0;
        
        int maxLength = "METHOD".length(); // the minimum it can be is "METHOD".length()
        
        for(MethodWrapper m : methodWrapperMap.values()){
            String methodName = m.method.getName() + "() ";
            
            if(!(m.parent instanceof PApplet)) { // for any method that belongs to a class other than the main sketch class
                String c = m.parent.getClass().getName();
                c = c.substring(c.lastIndexOf('.') + 1);
                if(c.contains("$")) c = c.substring(c.indexOf('$') + 1); // in case m.parent is a PApplet's inner class
                methodName = c + "." + methodName;
            }
            methods[i] = methodName;
            state[i++] = m.isActive;
            
            final int len = methodName.length();
            if(len > maxLength) maxLength = len;
        }
        
        header = String.format("%-5s%-"+maxLength+"s%s\n", "KEY", "METHOD", "STATE");
        
        i = 0;
        for(char k : methodWrapperMap.keySet()) {
            String key = "'" + k + "'";
            results += String.format("%-5s%-"+maxLength+"s%s\n", key, methods[i], state[i++]);
        }
        
        return header + results;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + methodWrapperMap.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        return getClass() == obj.getClass();
    }
}