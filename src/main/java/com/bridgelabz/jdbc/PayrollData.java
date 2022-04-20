package com.bridgelabz.jdbc;

import java.time.LocalDate;

public class PayrollData {

    private int id;
    private String name;
    private double salary;
    private LocalDate start;

    /**
     * parameterized constructor
     * @param id - id of employee
     * @param name -name of employee
     * @param salary - salary of employee
     */
    public PayrollData(int id, String name, double salary) {
        super();
        this.id = id;
        this.name = name;
        this.salary = salary;

    }

    /*
     * Constructor
     */
    public PayrollData(int id, String name, double salary, LocalDate start) {
        super();
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.start = start;
    }

    /**
     * toString method
     */
    @Override
    public String toString() {
        return "PayrollData [id=" + id + ", name=" + name + ", salary=" + salary + ", start=" + start + "]";
    }

}