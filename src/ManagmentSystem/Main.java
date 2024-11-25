package ManagmentSystem;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static final Dotenv dotenv = Dotenv.load();

    public static Connection getConnection() throws SQLException {
        String URL = dotenv.get("DB_URL");
        String USER = dotenv.get("DB_USER");
        String PASSWORD = dotenv.get("DB_PASSWORD");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println("Connected to database");

            Patient patient = new Patient(conn, scanner);
            Doctor doctor = new Doctor(conn, scanner);
            Appointment appointment = new Appointment(conn);

            while(true) {
                System.out.println("1. Enter 1 to add a new patient");
                System.out.println("2. Enter 2 to view patients");
                System.out.println("3. Enter 3 to add a new doctor");
                System.out.println("4. Enter 4 to view doctors");
                System.out.println("5. Enter 5 to Book Appointments");
                System.out.println("6. Enter 6 t0 view appointments");
                System.out.println("7. Exit");
                System.out.println("Enter your choice:");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewPatient();
                        break;
                    case 3:
                        doctor.addDoctor();
                        break;
                    case 4:
                        doctor.viewDoctor();
                        break;
                    case 5:
                        appointment.bookAppointment(patient, doctor, conn, scanner);
                        break;
                    case 6:
                        appointment.viewAppointment();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
