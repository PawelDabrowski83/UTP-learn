package org.example.TPO.TPO2.clientServerPractice.serverClient9;

import java.io.PrintWriter;

public class FileUtils {
    public static void logToFile(PrintWriter writerToFile, String message, int lineCounter, String name) {
        writerToFile.println(
                String.format("%d || %s || %s", lineCounter, name, message)
        );
    }

    public static void sendResponse(PrintWriter writer, PrintWriter writerToFile, String message, int lineCounter, String name) {
        writer.println(message);
        logToFile(writerToFile, message, lineCounter, name);
    }
}
