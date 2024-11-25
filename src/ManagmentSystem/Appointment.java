package ManagmentSystem;

import java.sql.*;
import java.util.Scanner;

public class Appointment {
    private Connection connection;
    public Appointment(Connection connection) {
        this.connection = connection;

        createTable();
    }
    public void createTable() {
        String QUERY = "CREATE TABLE IF NOT EXISTS appointment ("+
                "Id int PRIMARY KEY AUTO_INCREMENT, "+
                "Patient_Id int NOT NULL, "+
                "Doctor_Id int NOT NULL, "+
                "Appointment_Date DATE NOT NULL, "+
                "FOREIGN KEY(Patient_Id) REFERENCES patient (Patient_ID), "+
                "FOREIGN KEY(Doctor_Id) REFERENCES doctors (Doctor_Id));";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(QUERY);

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter Patient ID");
        int Patient_id = scanner.nextInt();
        System.out.println("Enter Doctor ID");
        int Doctor_id = scanner.nextInt();
        scanner.nextLine();
        if(patient.getPatient(Patient_id) && doctor.getDoctor(Doctor_id)){
            System.out.println("Enter Appointment Date (yyyy-MM-dd)");
            String date = scanner.nextLine();
            if(isAvailable(Doctor_id, date)){
                String QUERY = "INSERT INTO appointment(Patient_Id, Doctor_Id, Appointment_Date) VALUES (?,?,?); ";
                try{
                    PreparedStatement statement = connection.prepareStatement(QUERY);
                    statement.setInt(1,Patient_id);
                    statement.setInt(2,Doctor_id);
                    statement.setDate(3,Date.valueOf(date));
                    int affRow = statement.executeUpdate();

                    if(affRow > 0){
                        System.out.println("Appointment Booked");
                    }else{
                        System.out.println("Appointment Booking Failed");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Please choose another Date");
            }
        }else{
            System.out.println("Patient ID or Doctor ID does not exist");
        }
    }

    public void viewAppointment(){
        String QUERY = "SELECT * FROM appointment;";
        try{
            PreparedStatement statement = connection.prepareStatement(QUERY);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Appointment Details");
            System.out.println("+----+------------+--------------+----------------------+");
            System.out.println("|ID  |Patient_Id  |Doctor_Id     |Appointment_Date      |");
            System.out.println("+----+------------+--------------+----------------------+");
            while(resultSet.next()){
                int Appointment_Id = resultSet.getInt("Id");
                int Patient_Id = resultSet.getInt("Patient_Id");
                int Doctor_Id = resultSet.getInt("Doctor_Id");
                String Appointment_Date = resultSet.getString("Appointment_Date");

                System.out.printf("|%-4s|%-12s|%-14s|%-22s|\n", Appointment_Id, Patient_Id, Doctor_Id, Appointment_Date);
                System.out.println("+----+------------+--------------+----------------------+");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean isAvailable(int Doctor_id, String Date){
        String QUERY = "SELECT * FROM appointment WHERE Doctor_Id = ? AND Appointment_Date = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY);
            statement.setInt(1,Doctor_id);
            statement.setString(2,Date);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("inside is Available");
        return true;
    }

}
