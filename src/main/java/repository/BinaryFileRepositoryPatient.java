package repository;


import domain.Entity;
import domain.Patient;
import exceptions.RepositoryException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class BinaryFileRepositoryPatient <T extends Entity> extends Repository<Patient> {
    private final String filePath;

    public BinaryFileRepositoryPatient(String filePath) throws RepositoryException {
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

    private void loadFromFile() throws RepositoryException {
        ObjectInputStream in = null;
        List<Patient> list = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            items = (ArrayList<Patient>) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("Eroare la citirea din fisier: " + e.getMessage());
        }
    }

    private void saveToFile() throws RepositoryException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(items);
            out.close();
        } catch (IOException e) {
            throw new RepositoryException("Eroare la incarcarea fisierului:" + e.getMessage());
        }
    }
}