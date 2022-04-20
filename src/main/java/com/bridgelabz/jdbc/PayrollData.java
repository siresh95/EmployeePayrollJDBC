package com.bridgelabz.jdbc;

import java.time.LocalDate;
import java.util.Objects;

public class PayrollData {

    int id;
    String name;
    double salary;
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
        this(id, name, salary);
        this.start = start;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, id, name, salary);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;

        PayrollData other = (PayrollData) obj;
        return Objects.equals(start, other.start) && id == other.id && Objects.equals(name, other.name)
                && Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary);
    }
    /**
     * toString method
     */
    @Override
    public String toString() {
        return "PayrollData [id=" + id + ", name=" + name + ", salary=" + salary + ", start=" + start + "]";
    }

}