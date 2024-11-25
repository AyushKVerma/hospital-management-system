package ManagmentSystem;

import java.sql.*;
import java.util.Scanner;

public class Doctor {
    private Connection conn;
    private Scanner sc;

    public Doctor(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;

        createTable();
    }

    public void createTable(){
        String QUERY = "CREATE TABLE IF NOT EXISTS doctors ("+
                "Doctor_Id int PRIMARY KEY AUTO_INCREMENT, "+
                "Name varchar(255) NOT NULL, "+
                "Age int NOT NULL, "+
                "Specialization varchar(255) NOT NULL);";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(QUERY);
            System.out.println("Doctors table created");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDoctor() {
        String QUERY = "INSERT INTO Doctors (Name, Age, Specialization) VALUES(?,?,?);";

        System.out.println("Enter Name of Doctor");
        String Name = sc.next();
        System.out.println("Enter Age of Doctor");
        int Age = sc.nextInt();
        System.out.println("Enter Specialization of Doctor");
        String Specialization = sc.next();

        try{
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
            preparedStatement.setString(1,Name);
            preparedStatement.setInt(2,Age);
            preparedStatement.setString(3,Specialization);

            int affRow = preparedStatement.executeUpdate();
            if(affRow>0){
                System.out.println("Doctor added successfully");
            }else {
                System.out.println("Doctor not added");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewDoctor() {
        String QUERY = "SELECT * FROM Doctors;";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors Table");
            System.out.println("+------------+--------------+----------+----------------------+");
            System.out.println("|Doctor_ID   |Name          |Age        |Specialization        |");
            System.out.println("+------------+-------------+-----------+----------------------+");

            while(resultSet.next()){
                int Doctor_ID = resultSet.getInt("Doctor_ID");
                String Name = resultSet.getString("Name");
                int Age = resultSet.getInt("Age");
                String Specialization = resultSet.getString("Specialization");

                System.out.printf("|%-12s|%-14s|%-11s|%-22s|\n",Doctor_ID,Name,Age,Specialization);
                System.out.println("+------------+-------------+-----------+----------------------+");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctor(int id){
        String QUERY = "SELECT * FROM Doctors WHERE Doctor_Id = ?";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
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
