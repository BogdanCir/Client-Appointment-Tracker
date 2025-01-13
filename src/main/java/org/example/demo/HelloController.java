//package org.example.demo;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//
//public class HelloController {
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
//}

package org.example.demo;

import domain.Appointment;
import domain.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import service.Service;

import java.time.LocalDate;
import java.time.LocalTime;

public class HelloController {
    @FXML
    private ListView<Patient> patientListView;
    @FXML
    private ListView<Appointment> appointmentListView;
    @FXML
    private TextField patientIdTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField appointmentIdTextField;
    @FXML
    private TextField purposeTextField;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private TextField appointmentHourTextField;
    @FXML
    private Button addPatientButton;
    @FXML
    private Button updatePatientButton;
    @FXML
    private Button deletePatientButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;

    private final Service service;
    private final ObservableList<Patient> patients;
    private final ObservableList<Appointment> appointments;

    public HelloController(Service service) {
        this.service = service;
        this.patients = FXCollections.observableArrayList(service.getAllPatients());
        this.appointments = FXCollections.observableArrayList(service.getAllAppointments());
    }

    @FXML
    public void initialize() {
        // Setări pentru ListViews
        patientListView.setItems(patients);
        appointmentListView.setItems(appointments);

        patientListView.setOnMouseClicked(this::onPatientSelected);
        appointmentListView.setOnMouseClicked(this::onAppointmentSelected);
    }

    @FXML
    private void onPatientSelected(MouseEvent event) {
        Patient patient = patientListView.getSelectionModel().getSelectedItem();
        if (patient != null) {
            patientIdTextField.setText(String.valueOf(patient.getId()));
            firstNameTextField.setText(patient.getFirstName());
            lastNameTextField.setText(patient.getLastName());
            ageTextField.setText(String.valueOf(patient.getAge()));

            // Filtrare programări pentru pacientul selectat
//            ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList(
//                    service.getAppointmentsByPatient(patient.getId())
//            );
//            appointmentListView.setItems(filteredAppointments);
        }
    }

    @FXML
    private void onAppointmentSelected(MouseEvent event) {
        Appointment appointment = appointmentListView.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            appointmentIdTextField.setText(String.valueOf(appointment.getId()));
            purposeTextField.setText(appointment.getAppointmentPurpose());
            appointmentDatePicker.setValue(appointment.getDate());
            appointmentHourTextField.setText(appointment.getAppointmentHour().toString());
        }
    }

    @FXML
    private void onAddPatientClicked() {
        try {
            int id = Integer.parseInt(patientIdTextField.getText());
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            int age = Integer.parseInt(ageTextField.getText());

            service.addPatient(lastName, firstName, age);
            patients.setAll(service.getAllPatients());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdatePatientClicked() {
        try {
            int id = Integer.parseInt(patientIdTextField.getText());
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            int age = Integer.parseInt(ageTextField.getText());

            service.updatePatient(id, lastName, firstName, age);
            patients.setAll(service.getAllPatients());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDeletePatientClicked() {
        try {
            int id = Integer.parseInt(patientIdTextField.getText());
            service.deletePatient(id);
            patients.setAll(service.getAllPatients());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onAddAppointmentClicked() {
        try {
            int id = Integer.parseInt(appointmentIdTextField.getText());
            int patientId = Integer.parseInt(patientIdTextField.getText());
            String purpose = purposeTextField.getText();
            LocalDate date = appointmentDatePicker.getValue();
            LocalTime hour = LocalTime.parse(appointmentHourTextField.getText());

            // Caută pacientul cu ID-ul specificat
            Patient patient = null;
            for (Patient p : service.getAllPatients()) {
                if (p.getId() == patientId) {
                    patient = p;
                    break;
                }
            }

            if (patient == null) {
                throw new Exception("Pacientul cu ID-ul " + patientId + " nu există.");
            }

            service.addAppointment(id, patient, date, hour, purpose);
            appointments.setAll(service.getAllAppointments());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdateAppointmentClicked() {
        try {
            int id = Integer.parseInt(appointmentIdTextField.getText());
            int patientId = Integer.parseInt(patientIdTextField.getText());
            String purpose = purposeTextField.getText();
            LocalDate date = appointmentDatePicker.getValue();
            LocalTime hour = LocalTime.parse(appointmentHourTextField.getText());

            // Caută pacientul cu ID-ul specificat
            Patient patient = null;
            for (Patient p : service.getAllPatients()) {
                if (p.getId() == patientId) {
                    patient = p;
                    break;
                }
            }

            if (patient == null) {
                throw new Exception("Pacientul cu ID-ul " + patientId + " nu există.");
            }

            service.updateAppointment(id, patient, date, hour, purpose);
            appointments.setAll(service.getAllAppointments());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDeleteAppointmentClicked() {
        try {
            int id = Integer.parseInt(appointmentIdTextField.getText());
            service.deleteAppointment(id);
            appointments.setAll(service.getAllAppointments());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void onShowAppointmentsPerPatient() {
        StringBuilder report = new StringBuilder("Numărul de programări pentru fiecare pacient:\n");
        service.getAppointmentsPerPatient().forEach(entry ->
                report.append(entry.getKey()).append(" - Programări: ").append(entry.getValue()).append("\n")
        );
        showInformation("Raport Programări Pacienți", report.toString());
    }

    @FXML
    private void onShowAppointmentsPerMonth() {
        StringBuilder report = new StringBuilder("Numărul de programări pentru fiecare lună:\n");
        service.getAppointmentsPerMonth().forEach(entry ->
                report.append(entry.getKey()).append(" - Programări: ").append(entry.getValue()).append("\n")
        );
        showInformation("Raport Programări Lunar", report.toString());
    }

    @FXML
    private void onShowDaysSinceLastAppointment() {
        StringBuilder report = new StringBuilder("Zile trecute de la ultima programare:\n");
        service.getDaysSinceLastAppointment().forEach(entry ->
                report.append(entry.getKey()).append(" - Ultima programare: ").append(entry.getValue().getKey())
                        .append(" - Zile trecute: ").append(entry.getValue().getValue()).append("\n")
        );
        showInformation("Raport Zile Ultima Programare", report.toString());
    }

    @FXML
    private void onShowBusiestMonths() {
        StringBuilder report = new StringBuilder("Cele mai aglomerate luni:\n");
        service.getBusiestMonths().forEach(entry ->
                report.append(entry.getKey()).append(" - Programări: ").append(entry.getValue()).append("\n")
        );
        showInformation("Raport Luni Aglomerate", report.toString());
    }

    private void showInformation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
