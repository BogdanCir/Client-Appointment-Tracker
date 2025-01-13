package service;

import domain.Appointment;
import domain.Patient;
import repository.Repository;
import util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Service {
    public Repository <Patient> patientsRepository = new Repository<>();
    public Repository <Appointment> appointmentsRepository = new Repository<>();

    public Service(Repository patientsRepository, Repository appointmentsRepository) {
        this.patientsRepository = patientsRepository;
        this.appointmentsRepository = appointmentsRepository;
    }

    public void addPatient(String firstName, String lastName, int age) throws Exception {
        int id = IdGenerator.generateId();
        Patient patient = new Patient(id, firstName, lastName, age);
        this.patientsRepository.add(patient);
    }

    public void deletePatient (int id) throws Exception {
        patientsRepository.delete(id);
    }

    public void updatePatient(int id, String newFirstName, String newLastName, int newAge) throws Exception {
        Patient patient = new Patient(id, newLastName, newFirstName, newAge);
        patientsRepository.update(patient);
    }

    public ArrayList<Patient> getAllPatients() {
        return (ArrayList<Patient>) this.patientsRepository.getAll();
    }


    public void addAppointment(int id, Patient patient, LocalDate date, LocalTime appointmentHour, String appointmentPurpose) throws Exception{
        Appointment appointment = new Appointment(id, patient, date, appointmentHour, appointmentPurpose);
        ArrayList<Appointment> allAppointments = (ArrayList<Appointment>)appointmentsRepository.getAll();
        for (Appointment currentAppointment : allAppointments) {
            if (
                    currentAppointment.getDate().equals(date) &&
                    currentAppointment.getAppointmentHour() == appointmentHour)
                throw new Exception ("Patient already has an appointment on that time!");
        }
        appointmentsRepository.add(appointment);
    }

    public void deleteAppointment(int id) throws Exception{
        appointmentsRepository.delete(id);
    }

    public void updateAppointment(int id, Patient newPatient, LocalDate newDate, LocalTime newAppointmentHour, String newAppointmentPurpose) throws Exception{
        for(Appointment currentAppointment : appointmentsRepository.getAll())
            if(currentAppointment.getPatient().getId() == newPatient.getId() &&
            currentAppointment.getDate().equals(newDate) &&
            currentAppointment.getAppointmentHour() == newAppointmentHour)
                throw new Exception("Patient already has an appointment on that time!");

        Appointment newAppointment = new Appointment(id, newPatient, newDate, newAppointmentHour, newAppointmentPurpose);
        appointmentsRepository.update(newAppointment);
    }
    
    public ArrayList<Appointment> getAllAppointments() {
        return (ArrayList<Appointment>) appointmentsRepository.getAll();
    }

    // Rapoarte

    // 1. Numărul de programări pentru fiecare pacient, sortat descrescător

    public List<Map.Entry<Patient, Long>> getAppointmentsPerPatient() {
        return appointmentsRepository.getAll().stream()
                .collect(Collectors.groupingBy(Appointment::getPatient, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    // 2. Numărul total de programări pentru fiecare lună, sortat descrescător
    public List<Map.Entry<Month, Long>> getAppointmentsPerMonth() {
        return appointmentsRepository.getAll().stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getDate().getMonth(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    // 3. Numărul de zile trecute de la ultima programare pentru fiecare pacient, sortat descrescător
    public List<Map.Entry<Patient, Map.Entry<LocalDate, Long>>> getDaysSinceLastAppointment() {
        return appointmentsRepository.getAll().stream()
                .collect(Collectors.groupingBy(Appointment::getPatient,
                        Collectors.maxBy(Comparator.comparing(Appointment::getDate))))
                .entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), Map.entry(entry.getValue().get().getDate(),
                        ChronoUnit.DAYS.between(entry.getValue().get().getDate(), LocalDate.now()))))
                .sorted((e1, e2) -> Long.compare(e2.getValue().getValue(), e1.getValue().getValue()))
                .collect(Collectors.toList());
    }

    // 4. Cele mai aglomerate luni ale anului, sortate descrescător după numărul de programări
    public List<Map.Entry<Month, Long>> getBusiestMonths() {
        return getAppointmentsPerMonth(); // Reutilizăm raportul generat anterior
    }





}
