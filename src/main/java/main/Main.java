package main;

import domain.Appointment;
import domain.Patient;
import repository.BinaryFileRepositoryAppointment;
import repository.Repository;
import exceptions.RepositoryException;
import repository.BinaryFileRepositoryPatient;
import repository.*;
import service.Service;
import ui.Ui;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        try {

            FileReader fr = new FileReader("settings.properties");
            Properties properties = new Properties();
            properties.load(fr);

            String repoType = properties.getProperty("Repository");
            String patientFile = properties.getProperty("Patients");
            String appointmentFile = properties.getProperty("Appointments");


            Repository<Patient> patientRepository;
            Repository<Appointment> appointmentRepository;

            if ("text".equals(repoType)) {
                patientRepository = new TextFileRepositoryPatient<>(patientFile);
                appointmentRepository = new TextFileRepositoryAppointment<>(appointmentFile, patientRepository);
            } else if ("binary".equals(repoType)) {
                    patientRepository = new BinaryFileRepositoryPatient<Patient>(patientFile);
                    appointmentRepository = new BinaryFileRepositoryAppointment<>(appointmentFile, patientRepository);

            }else if ("db".equals(repoType)) {
                patientRepository = new DBRepositoryPatient();
                appointmentRepository = new DBRepositoryAppointment((DBRepositoryPatient) patientRepository);

            } else {
                throw new IllegalArgumentException("Invalid repository type: " + repoType);
            }

            Service service = new Service(patientRepository, appointmentRepository);
            Ui ui = new Ui(service);

            ui.menu();
        } catch (IOException e) {
            System.out.println("Error reading settings file: " + e.getMessage());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}