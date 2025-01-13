package util;

import java.io.*;

public class IdGenerator {
    private static int currentId = 100; // Implicit începe de la 100
    private static final String filePath = "last_id.txt";

    static {
        currentId = loadLastId();
    }

    private static int loadLastId() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            return (line != null) ? Integer.parseInt(line) : 100;
        } catch (IOException | NumberFormatException e) {
            return 100;
        }
    }

    public static synchronized int generateId() {
        currentId++;
        saveLastId();
        return currentId;
    }

    private static void saveLastId() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(String.valueOf(currentId));
        } catch (IOException e) {
            System.out.println("Eroare la salvarea ID-ului: " + e.getMessage());
        }
    }
}
