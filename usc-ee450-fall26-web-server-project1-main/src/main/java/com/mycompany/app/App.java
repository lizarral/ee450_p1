/*----------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *---------------------------------------------------------------------------------------*/

package com.mycompany.app;
import java.io.* ;
import java.net.* ;
import java.util.* ;

public class App {
    public static void main(String[] args) throws Exception {
        // Set the port number.
	    int port = 6789;

        // Establish the listen socket. Hint, look at documentation for Socket
        // https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html#Socket(java.net.InetAddress,%20int)
        // Add your code below...
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);

        // Process HTTP service requests in an infinite loop.
        while (true) {
            // Listen for a TCP connection request.
            Socket socket = serverSocket.accept();

            // Construct an object to process the HTTP request message.
            HttpRequest request = new HttpRequest(socket);

            // Create a new thread to process the request.
            Thread thread = new Thread(request);

            // Start the thread.
            thread.start();
        }
    }
}
