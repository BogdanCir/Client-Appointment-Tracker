package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Patient patient;
    private LocalTime appointmentHour;
    private LocalDate date;
    private String appointmentPurpose;

    public Appointment(int id, Patient patient, LocalDate date, LocalTime appointmentHour, String appointmentPurpose) {
        super(id);
        this.patient = patient;
        this.date = date;
        this.appointmentHour = appointmentHour;
        this.appointmentPurpose = appointmentPurpose;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getAppointmentHour() {
        return appointmentHour;
    }

    public void setAppointmentHour(LocalTime appointmentHour) {
        this.appointmentHour = appointmentHour;
    }

    public String getAppointmentPurpose() {
        return appointmentPurpose;
    }

    public void setAppointmentPurpose(String appointmentPurpose) {
        this.appointmentPurpose = appointmentPurpose;
    }

//    @Override
//    public String toString() {
//        return id + " " + patient.getId() + " " + date + " " + appointmentHour + " " + appointmentPurpose;
//    }


    @Override
    public String toString() {
        return
                "id=" + id +
                ", patient=" + patient +
                ", date=" + date +
                ", appointmentHour=" + appointmentHour +
                ", appointmentPurpose='" + appointmentPurpose;
    }
}
