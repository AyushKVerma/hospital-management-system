package ManagmentSystem;

import javax.lang.model.element.Name;
import java.sql.*;
import java.util.Scanner;

public class Patient {
    private Connection conn;
    private Scanner sc;

    public Patient(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;

        createPatient();
    }

    public void createPatient() {
        String QUERY = "CREATE TABLE IF NOT EXISTS patient"+
                "(Patient_ID int PRIMARY KEY AUTO_INCREMENT, "+
                "Name varchar(255) NOT NULL, "+
                "Gender varchar(10) NOT NULL, "+
                "Age int NOT NULL);";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(QUERY);
            System.out.println("Patient has been created");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addPatient(){
        try{
            System.out.println("Enter Patient Name");
            String name = sc.next();
            System.out.println("Enter Patient Gender");
            String gender = sc.next();
            System.out.println("Enter Patient Age");
            int age = sc.nextInt();

            String insertQuery = "INSERT INTO patient (Name, Gender, Age) VALUES (?,?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2,gender);
            preparedStatement.setInt(3,age);
            int affRow = preparedStatement.executeUpdate();

            if(affRow>0){
                System.out.println("Patient Added Successfully");
            }else{
                System.out.println("Patient Not Added Successfully");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatient(){
        String selectQuery = "select * from patient;";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery(); // this interface holds the data coming from the database. we csn use it print or put value in a variable

            System.out.println("Patients Table");
            System.out.println("+------------+--------------+-----------+--------------+");
            System.out.println("|Patient_ID  |Name          |Age        |Gender        |");
            System.out.println("+------------+--------------+-----------+--------------+");
            while(resultSet.next()){
                int patient_ID = resultSet.getInt("Patient_ID");
                String patient_Name = resultSet.getString("Name");
                int age = resultSet.getInt("Age");
                String gender = resultSet.getString("Gender");
                System.out.printf("|%-12s|%-14s|%-11s|%-14s|\n", patient_ID, patient_Name, age, gender);
                System.out.println("+------------+--------------+-----------+--------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatient(int id){
        String selectQuery = "select * from patient where Patient_ID=?;";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);

            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}