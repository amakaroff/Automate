package org.makarov.util;

public class MessageUtils {

    public static String createMessage(String message, int position, String line) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < position; i++) {
            stringBuilder.append(" ");
        }

        return line + "\n" + stringBuilder.toString() + "^ " + message + "\n";
    }
}
