package org.example;

import org.example.springBoot.MySpringBoot;
import org.example.springBoot.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HttpServer {

    private static  HttpServer instancia = new HttpServer();

    private HashMap<String, Method> metodos = new HashMap<>();

    private OutputStream outputStream;

    public void run(String[] args) throws IOException, ClassNotFoundException {

        MySpringBoot spring = new MySpringBoot();
        ArrayList<String> classes = spring.getClassComponent(new ArrayList<String>(), ".");
        for (String className: classes) {
            Class c = Class.forName(className);
            for (Method m: c.getMethods()) {
                if (m.isAnnotationPresent(RequestMapping.class)){
                    metodos.put(m.getAnnotation(RequestMapping.class).value(), m);
                }
            }
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while(running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
            String inputLine, outputLine, request = "/simple", typeMethod = "GET";
            Boolean boolFirstLine = true;
            outputStream = clientSocket.getOutputStream();

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (boolFirstLine) {
                    request = inputLine.split(" ")[1];
                    typeMethod = inputLine.split(" ")[0];
                    boolFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
            if (Objects.equals(typeMethod, "GET")) {
                try {
                    if (metodos.containsKey(request)) {
                        outputLine = (String) metodos.get(request).invoke(null);
                    } else {
                        outputLine = (String) metodos.get("404").invoke(null);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else {
                outputLine = "HTTP/1.1 200 OK\r\n" +
                        "Content-type: text/html\r\n" +
                        "\r\n" +
                        "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>404</title>\n"
                        + "</head>"
                        + "<body>"
                        + "Use metodos GET"
                        + "</body>"
                        + "</html>";
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }


    public static HttpServer getInstance() {
        return instancia;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

}