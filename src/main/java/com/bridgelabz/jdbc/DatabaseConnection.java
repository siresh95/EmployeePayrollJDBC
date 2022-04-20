package com.bridgelabz.jdbc;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
 DatabaseFunction
        String url = "jdbc:mysql://localhost:3306/employeepayroll?useSSL=false";


        String url = "jdbc:mysql://localhost:3306/employeepayroll?useSSL=false";;

        String url = "jdbc:mysql://localhost:3306/employeepayroll?useSSL=false";";

master
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

    /**
     * Method to get employee from date range
     *
     * @param startDate start date
     * @param endDate   - end date
     * @return - employee from date range
     * @throws PayrollException
     */
    public List<PayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) throws PayrollException {
        String sql = String.format("select * from employee_payroll where start between '%s' and '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    /**
     * Method to get average salary by gender
     *
     * @return - salary by gender
     * @throws PayrollException
     */
    public Map<String, Double> getAverageSalaryByGender() throws PayrollException {
        String sql = "select gender,avg(salary) as avg_salary from employee_payroll group by gender";
        return getAggregateByGender("gender", "avg_salary", sql);
    }

    /**
     *
     * @param gender    -gender
     * @param aggregate -aggregate
     * @param sql       - sql query
     * @return
     */
    public Map<String, Double> getAggregateByGender(String gender, String aggregate, String sql) {
        Map<String, Double> genderCountMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String getGender = result.getString(gender);
                Double count = result.getDouble(aggregate);
                genderCountMap.put(getGender, count);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return genderCountMap;
    }

    /**
     * Method to count employee by gender
     *
     * @return - count by gender
     */
    public Map<String, Double> getCountByGender() {
        String sql = "select gender,count(salary) as count_gender from employee_payroll group by gender";
        return getAggregateByGender("gender", "count_gender", sql);
    }

    /**
     * Method to get minimum salary of employee by gender
     *
     * @return
     */
    public Map<String, Double> getMinimumByGender() {
        String sql = "select gender,min(salary) as minSalary_gender from employee_payroll group by gender";
        return getAggregateByGender("gender", "minSalary_gender", sql);
    }

    /**
     * Method to get maximum salary of employee by gender
     *
     * @return
     */
    public Map<String, Double> getMaximumByGender() {
        String sql = "select gender,max(salary) as maxSalary_gender from employee_payroll group by gender";
        return getAggregateByGender("gender", "maxSalary_gender", sql);
    }

    /**
     * Method to get sum of salary of employee by gender
     *
     * @return
     */
    public Map<String, Double> getSalarySumByGender() {
        String sql = "select gender,sum(salary) as sumSalary_gender from employee_payroll group by gender";
        return getAggregateByGender("gender", "sumSalary_gender", sql);
    }

    /**
     * get employee payroll using database
     *
     * @param sql
     * @return
     * @throws PayrollException
     */
    private List<PayrollData> getEmployeePayrollDataUsingDB(String sql) throws PayrollException {
        List<PayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                Double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollList.add(new PayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new PayrollException(e.getMessage(), PayrollException.ExceptionType.RETRIVAL_PROBLEM);
        }
        return employeePayrollList;

    }

    /*
     * get employee payroll data list
     */
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
     *
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
     * read data from table
     *
     * @return
     * @throws PayrollException
     */
    public List<PayrollData> readDate() throws PayrollException {
        String sql = "Select * from employee_payroll";
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    /**
     * Update Employee data using prepared statement
     *
     * @param name   - name of employee
     * @param salary - salary of employee
     * @return - salary
     * @throws PayrollException
     */
    public int updateEmployeeData(String name, double salary) throws PayrollException {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    /**
     * Update Employee data
     *
     * @param name   - name of employee
     * @param salary - salary of employee
     * @return - salary
     * @throws PayrollException
     */
    public int updateEmployeeDataUsingStatement(String name, double salary) throws PayrollException {
        String sql = String.format("update employee_payroll set salary = %.2f where name = %s;", salary, name);
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new PayrollException(ex.getMessage(), PayrollException.ExceptionType.UPDATE_PROBLEM);
        }
    }

    /**
     * update employee using PreparedStatement
     *
     * @param name   - name of employee
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
