package repository;

import domain.Entity;
import domain.Patient;
import exceptions.RepositoryException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileRepositoryPatient <T extends Entity> extends Repository<Patient> {
    private final String filePath;

    public TextFileRepositoryPatient(String filePath) {
        this.filePath = filePath;
        loadFromFile();
    }

    @Override
    public void add(Patient patient) throws Exception {
        super.add(patient);
        saveToFile();
    }

    @Override
    public void delete(int id) throws Exception {
        super.delete(id);
        saveToFile();
    }

    @Override
    public void update(Patient patient) throws Exception {
        super.update(patient);
        saveToFile();
    }

    private void loadFromFile() {
        items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Patient patient = deserialize(line);
                if (patient != null) items.add(patient);
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Patient patient : items) {
                bw.write(serialize(patient));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private String serialize(Patient patient) {
        return patient.toString();
    }

    private Patient deserialize(String line) {
        try {
            String[] parts = line.split(" ");
            int id = Integer.parseInt(parts[0]);
            String lastName = parts[2];
            String firstName = parts[1];
            int age = Integer.parseInt(parts[3]);
            return new Patient(id, lastName, firstName, age);
        } catch (Exception e) {
            System.out.println("Error deserializing line: " + e.getMessage());
            return null;
        }
    }
}
