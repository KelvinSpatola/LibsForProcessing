package kelvinclark.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 *
 * @author Kelvin Spátola (Ov3rM1nD_)
 */
public abstract class FileHandler {
    
    public static void copyFileTo(File source, String destination) {
        if (destination == null) throw new IllegalArgumentException("Your destination is not valid");
        
        if (source.isFile()) {
            File target = new File(destination + File.separatorChar + source.getName());
            try {
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch(IOException io) {
                System.err.println("fodeu");
            }
        }
    }
    
    public static long getSize(File ... files){
        long size = 0;
        for(File f : files) size += f.length();
        return size / 1024;
    }
    
    public static String getName(File file){
        return file.getName();
    }
    
    public static String[] getName(File[] files){
        String[] list = new String[files.length];
        
        for(int i = 0; i < files.length; i++){
            list[i] = files[i].getName();
        }
        return list;
    }
    
    public static void copyFilesTo(File[] files, String dest) {
        int num = 0;
        
        for (File f : files) {
            System.out.println((num * 100) / files.length + "%");
            copyFileTo(f, dest);
            num++;
        }
    }
    
    /**
     * É preciso modificar este método pois ele não leva em conta a existencia de uma hierarquia de diretórios já
     * existente. Da forma como está ele força a criação de uma nova hierarquia, o que pode vir a apagar tudo
     * o que exista pelo meio.
     * Talvez seja melhor mudar o "target" para String, invés de File (ou talvez sobrecarregar o método)
     *
     * @param source
     * @param target
     */
    
    public static void copyDirectoryTo(File source, File target) {
        if (source.isDirectory()) {
            String destination = target.getAbsolutePath() + File.separatorChar + source.getName();
            new File(destination).mkdirs();
            File[] sourceFiles = source.listFiles();
            copyFilesTo(sourceFiles, destination);
        }
    }
    
    public static void moveFileTo(File f, String dest) {
        if (f.isFile() && dest != null) {
            File newFile = f.getAbsoluteFile();
            newFile.renameTo(new File(dest + File.separatorChar + f.getName()));
        }
    }
    
    
    public static File[] scanFiles(String path, String ... ext) {
        ArrayList<File> fileList = new ArrayList<>();
        recursiveSearch(fileList, path, ext);
        
        File[] result = new File[fileList.size()];
        fileList.toArray(result);
        return result;
    }
    
    private static void recursiveSearch(ArrayList<File> fileList, String path, String[] extension) {
        File file = new File(path);
        
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (File f : subfiles)
                recursiveSearch(fileList, f.getAbsolutePath(), extension);
        } else {
            
            boolean valid = true;
            for (String ext : extension) {
                if (file.getName().endsWith(ext.toLowerCase())) {
                    valid = false;
                }
            }
            
            if (valid) fileList.add(file);
        }
    }
    
}
