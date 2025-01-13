package ui;

import domain.Appointment;
import domain.Patient;
import service.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import util.IdGenerator;

public class Ui {
    private Service service;
    Scanner scanner = new Scanner(System.in);
    String option = null;

    public Ui(Service service) {
        this.service = service;
    }

    public int menu() {
        try {
//            Patient p1 = new Patient( IdGenerator.generateId(), "Marcel", "Don", 21);
//            Patient p2 = new Patient(IdGenerator.generateId(), "Misu", "Aurel", 19);
//            Patient p3 = new Patient(IdGenerator.generateId(), "Popa", "Dorian", 27);
//
//            service.patientsRepository.add(p1);
//            service.patientsRepository.add(p2);
//            service.patientsRepository.add(p3);
//
//            service.addAppointment(1, p1, LocalDate.now(), LocalTime.parse("10:02"), "purpose1");
//            service.addAppointment(2, p2, LocalDate.parse("2024-12-12"), LocalTime.parse("14:30"), "purpose2");
//            service.addAppointment(3, p3, LocalDate.now(), LocalTime.parse("18:00"), "purpose3");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        while(true){
            printMenu();
            option = scanner.next();
            if (Objects.equals(option, "1"))  addPatientUi();
            else if (Objects.equals(option, "2")) {
                try {
                    updatePatientUi();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (Objects.equals(option, "3")) {
                try {
                    deletePatientUi();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (Objects.equals(option, "4")) getAllPatientsUi();
            else if (Objects.equals(option, "a")) {
                try {
                    addAppointmentUi();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (Objects.equals(option, "b")) {
                try {
                    updateAppointmentUi();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (Objects.equals(option, "c")) deleteAppointmentUi();
            else if (Objects.equals(option, "d")) getAllAppointmentsUi();
            else {
                System.out.println("Try again!");
            }
        }
    }

    private void deleteAppointmentUi() {
        int id;
        System.out.println("\nid: ");
        try {
            id = scanner.nextInt();
            service.deleteAppointment(id);
            System.out.println("Appointment deleted!");
        } catch (Exception e) {
            System.out.println("Error while deleting the appointment: " + e.getMessage());
        }
    }

    private void updateAppointmentUi() throws Exception {
        int id, patientId, newAppointmentHour;
        Patient thePatient = null;
        String newDate, newAppointmentPurpose;

        try {
            System.out.println("\nid of the appointment you want to update: ");
            id = scanner.nextInt();

            System.out.println("\nnew patient id: ");
            patientId = scanner.nextInt();
            ArrayList<Patient> patients = this.service.getAllPatients();
            for (Patient patient : patients) {
                if (patient.getId() == patientId)
                    thePatient = new Patient(patient.getId(), patient.getLastName(), patient.getFirstName(), patient.getAge());
            }

            System.out.println("new date (YYYY-MM-DD): ");
            newDate = scanner.next();
            LocalDate date = LocalDate.parse(newDate);


            System.out.println("\nnew appointment hour: ");
            newAppointmentHour = scanner.nextInt();
            LocalTime time = LocalTime.of(newAppointmentHour, 0);


            System.out.println("\nnew appointment purpose: ");
            newAppointmentPurpose = scanner.next();
            ;

            service.updateAppointment(id, thePatient, date, time, newAppointmentPurpose);
            System.out.println("Appointment updated!");
        } catch (Exception e) {
            System.out.println("Error while updating the appointment: " + e.getMessage());
        }
    }

    private void addAppointmentUi() {
        int id, idPatient;
        String appointmentHour, appointmentPurpose, date;
        Patient thePatient = null;

        try {
            System.out.println("id: ");
            id = scanner.nextInt();
            System.out.println("id patient: ");
            idPatient = scanner.nextInt();

            ArrayList<Patient> patients = this.service.getAllPatients();
            for (Patient patient : patients) {
                if (patient.getId() == idPatient) {
                    thePatient = new Patient(patient.getId(), patient.getLastName(), patient.getFirstName(), patient.getAge());
                    break;
                }
            }

            if (thePatient == null) {
                System.out.println("No patient found with the given ID!");
                return;
            }

            System.out.println("date (YYYY-MM-DD): ");
            date = scanner.next();
            LocalDate date1 = LocalDate.parse(date);


            System.out.println("appointment purpose: ");
            appointmentPurpose = scanner.next();

            System.out.println("appointment hour (hh:mm): ");
            appointmentHour = scanner.next();
            LocalTime appointmentHour1 = LocalTime.parse(appointmentHour);

            service.addAppointment(id, thePatient, date1, appointmentHour1, appointmentPurpose);
            System.out.println("Appointment added successfully!");
        } catch (Exception e) {
            System.out.println("Error while adding the appointment: " + e.getMessage());
        }
    }


    private void addPatientUi(){
        int id, age;
        String lastName, firstName;

        try {
            System.out.println("last name: ");
            lastName = scanner.next();
            System.out.println("first name: ");
            firstName = scanner.next();
            System.out.println("age: ");
            age = scanner.nextInt();

            service.addPatient(lastName, firstName, age);
            System.out.println("Patient added!");
        } catch (Exception e) {
            System.out.println("Error while adding the patient: " + e.getMessage());
        }
    }

    private void deletePatientUi() {
        int id;
        System.out.println("id: ");
        try {
            id = scanner.nextInt();
            service.deletePatient(id);
            System.out.println("Patient deleted!");
        } catch (Exception e) {
            System.out.println("Error while deleting the patient " + e.getMessage());
        }
    }

    private void updatePatientUi() {
        int id, age;
        String lastName, firstName;

        try {
            System.out.println("\nid of the patient you want to update: ");
            id = scanner.nextInt();

            System.out.println("\nnew last name: ");
            lastName = scanner.next();

            System.out.println("\nnew first name: ");
            firstName = scanner.next();

            System.out.println("\nnew age: ");
            age = scanner.nextInt();

            service.updatePatient(id, lastName, firstName, age);
            System.out.println("Patient updated!");
        } catch (Exception e) {
            System.out.println("Error while updating the patient: " + e.getMessage());
        }
    }





    private void getAllPatientsUi() {
        ArrayList<Patient> patients = this.service.getAllPatients();
        if (patients != null) {
            for (Patient patient : patients) {
                System.out.println("ID: " + patient.getId());
                System.out.println("Last Name: " + patient.getLastName());
                System.out.println("First Name: " + patient.getFirstName());
                System.out.println("Age: " + patient.getAge());
                System.out.println();
            }
        } else System.out.println("No patients found.");
    }


    private void getAllAppointmentsUi() {
        ArrayList<Appointment> appointments = this.service.getAllAppointments();
        if (appointments != null) {
            for (Appointment appointment : appointments) {
                Patient p = appointment.getPatient();
                System.out.println("ID: " + appointment.getId());
                System.out.println("patient: " + p.getId() + " " + p.getLastName() + " " + p.getFirstName() + " " + p.getAge());
                System.out.println("Date: " + appointment.getDate());
                System.out.println("Appointment Hour: " + appointment.getAppointmentHour());
                System.out.println("Appointment Purpose: " + appointment.getAppointmentPurpose());
                System.out.println();
            }
        } else System.out.println("No appointments found.");
    }

    void printMenu(){
        System.out.println("1.Add patient");
        System.out.println("2.Update patient");
        System.out.println("3.Delete patient");
        System.out.println("4.Display all patients");

        System.out.println("a.Add appointment");
        System.out.println("b.Update appointment");
        System.out.println("c.Delete appointment");
        System.out.println("d.Display all appointments");

        System.out.println("Choose the option");
    }
}
