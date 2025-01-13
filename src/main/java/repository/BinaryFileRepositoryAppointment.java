package repository;

import domain.Appointment;
import domain.Entity;
import domain.Patient;
import exceptions.RepositoryException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileRepositoryAppointment <T extends Entity> extends Repository<Appointment> {
    String filename;
    private Repository<Patient> patientRepository;

    public BinaryFileRepositoryAppointment(String filename, Repository<Patient> patientRepository) throws RepositoryException {
        this.filename = filename;
        this.patientRepository = patientRepository;
        loadFromBinaryFile();
    }

    @Override
    public void add(Appointment entity) throws Exception {
        super.add(entity);
        saveToBinaryFile();
    }

    @Override
    public void delete(int id) throws Exception {
        super.delete(id);
        saveToBinaryFile();
    }

    @Override
    public void update(Appointment appointment) throws Exception {
        super.update(appointment);
        saveToBinaryFile();
    }

    private void saveToBinaryFile() throws RepositoryException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(items);
            out.close();
        } catch (IOException e) {
            throw new RepositoryException("Eroare la incarcarea fisierului:" + e.getMessage());
        }
    }

    private void loadFromBinaryFile() throws RepositoryException {
        ObjectInputStream in = null;
        List<Patient> list = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filename));
            items = (ArrayList<Appointment>) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("Eroare la citirea din fisier: " + e.getMessage());
        }
    }
}