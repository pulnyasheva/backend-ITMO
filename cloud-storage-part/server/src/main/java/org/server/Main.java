package org.server;

import static org.common.Constants.PORT;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(PORT);
        try {
            server.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
