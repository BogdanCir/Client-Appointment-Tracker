package repository;

import domain.Appointment;
import domain.Patient;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBRepositoryAppointment extends Repository<Appointment> {
    private String JDBC_URL = "jdbc:sqlite:patients";
    private Connection connection;
    private DBRepositoryPatient patientRepository;

    public DBRepositoryAppointment(DBRepositoryPatient patientRepository) throws Exception {
        this.patientRepository = patientRepository;
        openConnection();
        createTable();
//        clearPatientsTable();
//        generateRandomAppointments(100); // Generate 100 pseudo-random appointments
        loadDataInMemory();
    }

    public void clearPatientsTable() {
        String query = "DELETE FROM appointments";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            System.out.println("appointments been deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openConnection() {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);

        try {
            if (connection == null || connection.isClosed()) {
                connection = ds.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try (final Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS appointments(id INT, patientId INT, date TEXT, appointmentHour TEXT, appointmentPurpose TEXT);");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDataInMemory() throws Exception {
        for (Appointment e : getAll()) {
            super.add(e);
        }
    }

    private void generateRandomAppointments(int count) {
        String[] purposes = {"Consultatie", "Control de rutina", "Vaccinare", "Tratament", "Investigatie"};
        Random random = new Random();
        List<Patient> patients = patientRepository.getAll();

        if (patients.isEmpty()) {
            System.out.println("Nu există pacienți pentru a genera programări.");
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO appointments VALUES (?,?,?,?,?)")) {
            for (int i = 1; i <= count; i++) {
                Patient patient = patients.get(random.nextInt(patients.size()));
                LocalDate date = LocalDate.now().plusDays(random.nextInt(30));
                LocalTime hour = LocalTime.of(random.nextInt(9) + 8, random.nextInt(60)); // Ore între 08:00 și 16:59
                String purpose = purposes[random.nextInt(purposes.length)];

                statement.setInt(1, i);
                statement.setInt(2, patient.getId());
                statement.setString(3, date.toString());
                statement.setString(4, hour.toString());
                statement.setString(5, purpose);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public List<Appointment> getAll() {
//        List<Appointment> appointments = new ArrayList<>();
//        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointments")) {
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                int patientId = rs.getInt("patientId");
//                String lastName = rs.getString("lastName");
//                String firstName = rs.getString("firstName");
//                int age = rs.getInt("age");
//                LocalDate date = LocalDate.parse(rs.getString("date"));
//                LocalTime appointmentHour = LocalTime.parse(rs.getString("appointmentHour"));
//                String appointmentPurpose = rs.getString("appointmentPurpose");
//
//                Patient patient = findPatientById(patientId);
//                if (patient != null) {
//                    appointments.add(new Appointment(id, patient, date, appointmentHour, appointmentPurpose));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return appointments;
//    }
    public List<Appointment> getAll() {
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointments")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int patientId = rs.getInt("patientId");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                LocalTime hour = LocalTime.parse(rs.getString("appointmentHour"));
                String purpose = rs.getString("appointmentPurpose");

                Patient patient = patientRepository.getAll().stream().filter(p -> p.getId() == patientId).findFirst().orElse(null);

                if (patient != null) {
                    appointments.add(new Appointment(id, patient, date, hour, purpose));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public void add(Appointment appointment) throws Exception {
        super.add(appointment);
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO appointments VALUES (?,?,?,?,?)")) {
            statement.setInt(1, appointment.getId());
            statement.setInt(2, appointment.getPatient().getId());
            statement.setString(3, appointment.getPatient().getLastName());
            statement.setString(4, appointment.getPatient().getFirstName());
            statement.setInt(5, appointment.getPatient().getAge());
            statement.setString(6, appointment.getDate().toString());
            statement.setString(7, appointment.getAppointmentHour().toString());
            statement.setString(8, appointment.getAppointmentPurpose());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) throws Exception {
        try {
            super.delete(id);
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM appointments WHERE id=?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Appointment appointment) throws Exception {
        try {
            super.update(appointment);
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE appointments SET patientId=?, lastName=?, firstName=?, age=?, date=?, appointmentHour=?, appointmentPurpose=? WHERE id=?")) {
                statement.setInt(1, appointment.getPatient().getId());
                statement.setString(2, appointment.getPatient().getLastName());
                statement.setString(3, appointment.getPatient().getFirstName());
                statement.setInt(4, appointment.getPatient().getAge());
                statement.setString(5, appointment.getDate().toString());
                statement.setString(6, appointment.getAppointmentHour().toString());
                statement.setString(7, appointment.getAppointmentPurpose());
                statement.setInt(8, appointment.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Patient findPatientById(int patientId) {
        Patient patient = null;
        String query = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String lastName = rs.getString("lastName");
                String firstName = rs.getString("firstName");
                int age = rs.getInt("age");
                patient = new Patient(id, lastName, firstName, age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }
}
