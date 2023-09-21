package org.example.springBoot;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MySpringBoot {

    private ArrayList<String> classes;


    public ArrayList<String> getClassComponent(ArrayList<String> files, String path) {
        Path filePath = Paths.get(path);
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(filePath);
            for (Path file : directoryStream) {
                if (Files.isDirectory(file)) {
                    getClassComponent(files, file.toString());
                } else if (Files.isRegularFile(file)){
                    if (file.toString().split("\\.")[2].equals("java")) {;
                        String className = file.toString().replace("\\", ".").replace(".java", "").split("main.")[1];
                        if(Class.forName(className).isAnnotationPresent(Component.class)){
                            files.add(className);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return files;
    }


}
