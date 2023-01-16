package org.example.grpchelloworld;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final GreetingServer server = new GreetingServer();
        server.start();
        server.blockUntilShutdown();
    }
}