package repository;

import domain.Appointment;
import domain.Entity;
import domain.Patient;
import exceptions.RepositoryException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TextFileRepositoryAppointment <T extends Entity> extends Repository<Appointment> {
    private final String filePath;
    private final Repository<Patient> patientRepository;

    public TextFileRepositoryAppointment(String filePath, Repository<Patient> patientRepository) {
        this.filePath = filePath;
        this.patientRepository = patientRepository;
        loadFromFile();
    }

    @Override
    public void add(Appointment appointment) throws Exception {
        super.add(appointment);
        saveToFile();
    }

    @Override
    public void delete(int id) throws Exception {
        super.delete(id);
        saveToFile();
    }

    @Override
    public void update(Appointment appointment) throws Exception {
        super.update(appointment);
        saveToFile();
    }

    private void loadFromFile() {
        items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Appointment appointment = deserialize(line);
                if (appointment != null) items.add(appointment);
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Appointment appointment : items) {
                bw.write(serialize(appointment));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private String serialize(Appointment appointment) {
        return appointment.getId() + " " +
                appointment.getPatient().getId() + " " +
                appointment.getDate() + " " +
                appointment.getAppointmentHour() + " " +
                appointment.getAppointmentPurpose();
    }

    private Appointment deserialize(String line) {
        try {
            String[] parts = line.split(" ");
            int id = Integer.parseInt(parts[0]);
            int patientId = Integer.parseInt(parts[1]);
            LocalDate date = LocalDate.parse(parts[2]);
            LocalTime time = LocalTime.parse(parts[3]);
            String purpose = parts[4];

            Patient patient = patientRepository.getItemByID(patientId);
            return new Appointment(id, patient, date, time, purpose);
        } catch (Exception e) {
            System.out.println("Error deserializing line: " + e.getMessage());
            return null;
        }
    }
}
