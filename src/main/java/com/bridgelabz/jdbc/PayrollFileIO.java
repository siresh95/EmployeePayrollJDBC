package com.bridgelabz.jdbc;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class PayrollFileIO {
    public static String PAYROLL_FILE_NAME = "EmployeePayrollServiceMySql.txt";

    /**
     * Method to write data to file
     *
     * @param employeePayrollList
     */
    public void writeData(List<PayrollData> employeePayrollList) {
        StringBuffer buffer = new StringBuffer();
        employeePayrollList.forEach(employee -> {
            String employeeDataString = employee.toString().concat("\n");
            buffer.append(employeeDataString);
        });

        try {
            Files.write(Paths.get(PAYROLL_FILE_NAME), buffer.toString().getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Print the data
     */
    public void printData() {
        try {
            Files.lines(new File("EmployeePayrollServiceMySql.txt").toPath()).forEach(System.out::println);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Count the entries
     *
     * @return - total entries
     */
    public long countEntries() {
        long entries = 0;
        try {
            entries = Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return entries;
    }

    /**
     * Read the data
     *
     * @return - employee payroll list
     */
    public List<PayrollData> readData() {
        List<PayrollData> employeePayrollList = new ArrayList<>();
        try {
            Files.lines(new File(PAYROLL_FILE_NAME).toPath()).map(line -> line.trim())
                    .forEach(line -> System.out.println(line));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return employeePayrollList;
    }
}