package com.bridgelabz.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Creating Database connection with database payroll_service
 * @author Asus
 *
 */
public class DatabaseConnection {

    private Connection getConnection() throws SQLException{
        String url = "jdbc:mysql://localhost:3306/payroll_service";
        String uname = "root";
        String pass = "Deven#7818";

        System.out.println("Connecting to database : " +url);
        Connection con = DriverManager.getConnection(url, uname, pass);
        System.out.println("Connection Successfull" +con);
        return con;
    }

    public List<PayrollData> readData() throws PayrollException{
        String sql = "select * from employee_payroll";
        List<PayrollData> employeePayrollList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                Double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollList.add(new PayrollData(id, name, salary,startDate));
            }
        }catch (SQLException ex) {
            throw new PayrollException(ex.getMessage(), PayrollException.ExceptionType.RETRIVAL_PROBLEM);
        }
        return employeePayrollList;
    }
}


