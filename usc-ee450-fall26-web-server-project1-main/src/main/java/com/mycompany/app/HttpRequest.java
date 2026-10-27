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
	}

	// Implement the run() method of the Runnable interface.
	@Override 
	public void run() {
        try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	private void processRequest() throws Exception {
		// Access the socket streams.
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());

		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		// Read the HTTP request line.
		String requestLine = br.readLine();
		System.out.println();
		System.out.println(requestLine);

		// Read and print the header lines.
		String headerLine = null;
		while ((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		}

		// Extract the filename from the request line.
		StringTokenizer tokens = new StringTokenizer(requestLine);

		tokens.nextToken(); // skip over the method, which should be "GET"

		String fileName = tokens.nextToken();
		// Prepend a "." so that file request is within the current directory.
		fileName = "." + fileName;

		// Open the requested file.
		FileInputStream fis = null;
		boolean fileExists = true;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			fileExists = false;
		}

		// Construct the response message.
		String statusLine;
        String contentTypeLine;
        String entityBody = null;

        if (fileExists) {
            statusLine = "HTTP/1.0 200 OK" + CRLF;
            contentTypeLine =
                    "Content-type: "
                    + contentType(fileName)
                    + CRLF;
        } else {
            statusLine = "HTTP/1.0 404 Not Found" + CRLF;
            contentTypeLine =
                    "Content-type: text/html" + CRLF;
            entityBody =
                    "<HTML>"
                    + "<HEAD><TITLE>Not Found</TITLE></HEAD>"
                    + "<BODY>Not Found</BODY>"
                    + "</HTML>";
        }

		// Send the response status.
        os.writeBytes(statusLine);

        // Send the Content-Type header.
        os.writeBytes(contentTypeLine);

        // Blank line marks the end of the headers.
        os.writeBytes(CRLF);

        // Send either the file or the 404 page.
        if (fileExists) {
            sendBytes(fis, os);
            fis.close();
        } else {
            os.writeBytes(entityBody);
        }

		os.close();
		br.close();
		socket.close();
	}

	private static void sendBytes(
            FileInputStream fileInputStream,
            OutputStream outputStream) throws Exception {

        byte[] buffer = new byte[1024];
        int bytes;

        while ((bytes = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String fileName) {
        String lowerName = fileName.toLowerCase();

        if (lowerName.endsWith(".htm")
                || lowerName.endsWith(".html")) {
            return "text/html";
        }

        if (lowerName.endsWith(".gif")) {
            return "image/gif";
        }

        if (lowerName.endsWith(".jpeg")
                || lowerName.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (lowerName.endsWith(".png")) {
            return "image/png";
        }

        if (lowerName.endsWith(".pdf")) {
            return "application/pdf";
        }

        if (lowerName.endsWith(".zip")) {
            return "application/zip";
        }

        return "application/octet-stream";
    }
}
