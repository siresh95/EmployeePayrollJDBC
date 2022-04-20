package com.bridgelabz.jdbc;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 1.Create database connection using JDBC
 * 2.Retrieve Data from database
 * 3.Update employee salary in database
 * 4.Update employee salary in database using prepared statement
 * 5.Cache the PreparedStatement at the Driver and DB Level
 *   Make Payroll DB Service Object as Singleton so PreparedStatement is catch the program
 *    Reuse the ResultSet to populate EmployeePayrollData Object
 * @author Asus
 *
 */
public class PayrollService {
    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    private DatabaseConnection databaseConnection;
    /*
     * Welcome Message
     */
    public void printWelcomeMessage() {
        System.out.println("Welcome to the Employee PayRoll Service Program");
    }

    /*
     * created on list to store
     */
    private List<PayrollData> employeePayrollList;

    public PayrollService(List<PayrollData> employeePayrollList) {
        this.employeePayrollList = employeePayrollList;
    }

    public PayrollService() {
        databaseConnection = DatabaseConnection.getInstance();
    }

    public static void main(String[] args) {
        ArrayList<PayrollData> employeePayrollList = new ArrayList<>();
        PayrollService employeePayrollService = new PayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);

    }

    /*
     * Read Employee Payroll data from console
     */
    public void readEmployeePayrollData(Scanner consoleInputReader) {
        System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Name ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary ");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new PayrollData(id, name, salary));
    }

    /* Write Employee Payroll data to console */
    public void writeEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO)) {
            new PayrollFileIO().writeData(employeePayrollList);
        }
    }

    /* Print Employee Payroll */
    public void printData(IOService fileIo) {
        if (fileIo.equals(IOService.FILE_IO)) {
            new PayrollFileIO().printData();
        }

    }

    /**
     * Count employee Entries
     * @param fileIo
     * @return
     */
    public long countEntries(IOService fileIo) {
        if (fileIo.equals(IOService.FILE_IO)) {
            return new PayrollFileIO().countEntries();
        }
        return 0;
    }

    public List<PayrollData> readPayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            this.employeePayrollList = new PayrollFileIO().readData();
        return this.employeePayrollList;
    }

    public List<PayrollData> readEmployeePayrollData(IOService ioService) throws PayrollException {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = databaseConnection.readData();
        return employeePayrollList;
    }

    public void updateEmployeeSalary(String name, double salary) throws PayrollException{
        int result = databaseConnection.updateEmployeeData(name, salary);
        if (result == 0)
            return;
        PayrollData payrollData = this.getEmployeePayrollData(name);
        if(payrollData != null)
            payrollData.salary = salary;
    }

    private PayrollData getEmployeePayrollData(String name) {
        PayrollData employeePayrollData;
        employeePayrollData = this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst().orElse(null);
        return employeePayrollData;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<PayrollData> employeePayrollDataList = databaseConnection.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }
}