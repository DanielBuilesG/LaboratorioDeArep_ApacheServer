package org.example.componentes;


import org.example.springBoot.Component;
import org.example.springBoot.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class ControladorDeRecursos {

    @RequestMapping("/html")
    public static String readFileHTML() {
        return  getHeaderHTML() + getResponseFileHTML();
    }

    public static String getHeader() {
        return "HTTP/1.1 200 \r\n" +
                "Content-Type: application/json \r\n" +
                "\r\n";
    }

    public static String getHeaderHTML() {
        return "HTTP/1.1 200 \r\n" +
                "Content-Type: text/html \r\n" +
                "\r\n";
    }

    @RequestMapping("/image")
    public static String readFileImage() {
        return getHeaderImage() + getResponseImg();
    }

    public static String getHeaderImage() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: image/jpeg\r\n" + // Cambia el tipo de contenido a image/jpeg
                "\r\n";
    }

    public static String getResponseImg() {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/public/imagen.jpg")); // Cambia la ruta y el nombre del archivo de imagen .jpg
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return base64Image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResponseFileHTML() {
        byte[] file;
        try{
            file = Files.readAllBytes(Paths.get("src/main/resources/public/index.html"));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return new String(file);
    }

}
