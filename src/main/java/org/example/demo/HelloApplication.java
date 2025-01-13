//package org.example.demo;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class HelloApplication extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}


package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.DBRepositoryAppointment;
import repository.DBRepositoryPatient;
import service.Service;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Inițializează repository-urile și serviciul
        DBRepositoryPatient patientRepository = new DBRepositoryPatient();
        DBRepositoryAppointment appointmentRepository = new DBRepositoryAppointment(patientRepository);
        Service service = new Service(patientRepository, appointmentRepository);

        // Încarcă interfața principală
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        fxmlLoader.setController(new HelloController(service));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setTitle("Patient and Appointment Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
