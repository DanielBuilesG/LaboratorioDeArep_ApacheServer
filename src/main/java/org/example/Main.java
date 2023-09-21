package org.example;


import org.example.HttpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        HttpServer server = HttpServer.getInstance();
        server.run(args);
    }
}
