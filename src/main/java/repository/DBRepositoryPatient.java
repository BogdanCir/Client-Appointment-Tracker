package repository;

import domain.Patient;
import org.sqlite.SQLiteDataSource;
//import com.github.javafaker.Faker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBRepositoryPatient extends Repository<Patient>{
    private String JDBC_URL = "jdbc:sqlite:patients";
    Connection connection;

    public DBRepositoryPatient() throws Exception {
        openConnection();
        createTable();
//        clearPatientsTable();
//        generateRandomPatients(100); // Generează 100 de pacienți aleatori
        loadDataInMemory();
//        initData();
    }

    private void openConnection(){
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);

        try {
            if (connection == null || connection.isClosed())
                connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void clearPatientsTable() {
        String query = "DELETE FROM patients";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            System.out.println("Tabela patients a fost curățată.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e. printStackTrace();
        }
    }

    private void generateRandomPatients(int count) {
        String[] lastNames = {"Popescu", "Ionescu", "Vasilescu", "Georgescu", "Marinescu"};
        String[] firstNames = {"Andrei", "Maria", "Ion", "Alexandra", "Cristian"};
        Random random = new Random();

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO patients VALUES (?,?,?,?)")) {
            for (int i = 1; i <= count; i++) {
                String lastName = lastNames[random.nextInt(lastNames.length)];
                String firstName = firstNames[random.nextInt(firstNames.length)];
                int age = 18 + random.nextInt(60);

                statement.setInt(1, i);
                statement.setString(2, lastName);
                statement.setString(3, firstName);
                statement.setInt(4, age);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try (final Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS patients(id int, lastName varchar(400), firstName varchar(400), age int);");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void loadDataInMemory() throws Exception {
        for (Patient e : getAll())
        {
//            entitati.add(e);
            super.add(e);
        }
    }

    private void initData() {
        List<Patient> entitati = new ArrayList<>();
        entitati.add(new Patient(7777, "Ion", "Pop", 23));
        entitati.add(new Patient(7778, "Alexandra", "Ionescu", 22));
        entitati.add(new Patient( 7779, "Radu", "Oprea", 33));
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO patients VALUES (?,?,?,?)")) {
            for (Patient e : entitati)
            {
                statement.setInt(1, e.getId());
                statement.setString(2, e.getLastName());
                statement.setInt(4, e.getAge());
                statement.setString(3, e.getFirstName());
                statement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Patient> getAll()
    {
        List<Patient> entitati = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM patients")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                entitati.add(new Patient(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getInt(4)));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return entitati;
    }

    public void add(Patient e) throws Exception {
        super.add(e);
//        System.out.println("s-a afisat");
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO patients VALUES (?,?,?,?)")) {
            statement.setInt(1, e.getId());
            statement.setString(2, e.getLastName());
            statement.setInt(4, e.getAge());
            statement.setString(3, e.getFirstName());
            statement.executeUpdate();
        }
        catch (SQLException a)
        {
            a.printStackTrace();
        }
    }

    public void delete(int p1) throws Exception{
        try {
            super.delete(p1);
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM patients WHERE id=?")) {
                statement.setInt(1, p1);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Patient e) throws Exception{
        try {
            super.update(e);
            try (PreparedStatement statement = connection.prepareStatement("UPDATE patients SET lastName=?, firstName=?, age=? WHERE id=?")) {
                statement.setInt(1, e.getId());
                statement.setString(2, e.getLastName());
                statement.setString(3, e.getFirstName());
                statement.setInt(4, e.getAge());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
