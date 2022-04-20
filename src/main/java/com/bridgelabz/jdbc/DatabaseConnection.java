package com.bridgelabz.jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Creating Database connection with database payroll_service
 *
 * @author Asus
 *
 */
public class DatabaseConnection {
    private PreparedStatement employeePayrollDataStatement;
    private static DatabaseConnection databaseConnection;

    private DatabaseConnection() {
    }

    /**
     * Create connection with database
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/employeepayroll?useSSL=false";";
        String uname = "root";
        String pass = "Resh@308";

        System.out.println("Connecting to database : " + url);
        Connection con = DriverManager.getConnection(url, uname, pass);
        System.out.println("Connection Successfull" + con);
        return con;
    }

    public static DatabaseConnection getInstance() {
        if (databaseConnection == null)
            databaseConnection = new DatabaseConnection();
        return databaseConnection;
    }

    public List<PayrollData> getEmployeePayrollData(String name) {
        List<PayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    /**
     * Get employee payroll data
     * @param result
     * @return
     */
    private List<PayrollData> getEmployeePayrollData(ResultSet result) {
        List<PayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                Double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollList.add(new PayrollData(id, name, salary, startDate));
                return employeePayrollList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from employee_payroll where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to read data
     * @return - employee list
     * @throws PayrollException
     */
    public List<PayrollData> readData() throws PayrollException {
        String sql = "select * from employee_payroll";
        List<PayrollData> employeePayrollList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                Double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollList.add(new PayrollData(id, name, salary, startDate));
            }
        } catch (SQLException ex) {
            throw new PayrollException(ex.getMessage(), PayrollException.ExceptionType.RETRIVAL_PROBLEM);
        }
        return employeePayrollList;
    }

    /**
     * Update Employee data using prepared statement
     * @param name - name of employee
     * @param salary - salary of employee
     * @return - salary
     * @throws PayrollException
     */
    public int updateEmployeeData(String name, double salary) throws PayrollException {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    /**
     * Update Employee data
     * @param name - name of employee
     * @param salary - salary of employee
     * @return - salary
     * @throws PayrollException
     */
    public int updateEmployeeDataUsingStatement(String name, double salary) throws PayrollException{
        String sql = String.format("update employee_payroll set salary = %.2f where name = %s;", salary , name);
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        }catch (SQLException ex) {
            throw new PayrollException(ex.getMessage(), PayrollException.ExceptionType.UPDATE_PROBLEM);
        }
    }

    /**
     * update employee using PreparedStatement
     * @param name - name of employee
     * @param salary - salary of employee
     * @return
     */
    public int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
        try (Connection connection = this.getConnection();) {
            String sql = "update employee_payroll set salary=? where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, salary);
            preparedStatement.setString(2, name);
            int status = preparedStatement.executeUpdate();
            return status;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}


