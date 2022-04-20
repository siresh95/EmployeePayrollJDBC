package com.bridgelabz.jdbc;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bridgelabz.jdbc.PayrollService.IOService;

import static com.bridgelabz.jdbc.PayrollService.IOService.FILE_IO;

/**
 * Unit test for employee payroll service.
 */
public class PayrollServiceTest {

    static PayrollService employeePayrollService;

    public static void initializeConstructor() {
        employeePayrollService = new PayrollService();
    }

    @BeforeClass
    public void printWelcomeMessage() {
        employeePayrollService.printWelcomeMessage();
    }

    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
        PayrollData[] arrayOfEmps = { new PayrollData(1, "Bill", 100000.0), new PayrollData(2, "Terisa", 200000.0),
                new PayrollData(3, "Charlie", 300000.0) };
        PayrollService payrollService;
        payrollService = new PayrollService(Arrays.asList(arrayOfEmps));
        payrollService.writeEmployeePayrollData(FILE_IO);
        payrollService.printData(FILE_IO);
        long entries = payrollService.countEntries(FILE_IO);
        Assert.assertEquals(3, entries);
    }

    @Test
    public void givenFileOnReadingFileShouldMatchEmployeeCount() {
        PayrollService employeePayrollService = new PayrollService();
        List<PayrollData> entries = employeePayrollService.readPayrollData(FILE_IO);
        Assert.assertEquals(3, entries.size());
    }

    @Test
    public void givenEmployeePayrollinDB_whenRetrieved_ShouldMatch_Employee_Count() throws PayrollException {
        List<PayrollData> employeePayrollData = employeePayrollService
                .readEmployeePayrollData(PayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_shouldSynchronizewithDataBase() throws PayrollException {
        List<PayrollData> employeePayrollData = employeePayrollService
                .readEmployeePayrollData(PayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Deven", 000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Deven");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() throws PayrollException {
        PayrollService employeePayrollService = new PayrollService();
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<PayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollForDateRange(IOService.DB_IO,
                startDate, endDate);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperValue() throws PayrollException {
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(IOService.DB_IO);
        Assert.assertTrue(
                averageSalaryByGender.get("M").equals(2000000.00) && averageSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperCountValue()
            throws PayrollException {
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readCountByGender(IOService.DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(2.0) && countByGender.get("F").equals(1.0));
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperMinimumValue()
            throws PayrollException {
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readMinumumSalaryByGender(IOService.DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(1000000.00) && countByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperMaximumValue()
            throws PayrollException {
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readMaximumSalaryByGender(IOService.DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(3000000.00) && countByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperSumValue()
            throws PayrollException {
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        Map<String, Double> sumSalaryByGender = employeePayrollService.readSumSalaryByGender(IOService.DB_IO);
        Assert.assertTrue(
                sumSalaryByGender.get("M").equals(4000000.00) && sumSalaryByGender.get("F").equals(3000000.00));
    }
}