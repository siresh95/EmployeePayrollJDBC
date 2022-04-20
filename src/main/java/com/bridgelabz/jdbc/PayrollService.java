package com.bridgelabz.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class PayrollService {
    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

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
            this.employeePayrollList = new DatabaseConnection().readData();
        return employeePayrollList;
    }
}