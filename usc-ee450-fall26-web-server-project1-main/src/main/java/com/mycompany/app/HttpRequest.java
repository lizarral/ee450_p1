package com.mycompany.app;
import java.io.* ;
import java.net.* ;
import java.util.* ;

public class HttpRequest implements Runnable {
    final static String CRLF = "\r\n";
	Socket socket;

	// Constructor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
        // Add your code below...
	}

	// Implement the run() method of the Runnable interface.
	public void run() {
        // Add your code below...
		
	}

	private void processRequest() throws Exception {
		// Add your code below...
	}
}
