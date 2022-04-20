package com.bridgelabz.jdbc;


@SuppressWarnings("serial")
public class PayrollException extends Exception{
    enum ExceptionType {
        CONNECTION_PROBLEM, RETRIVAL_PROBLEM, UPDATE_PROBLEM;
    }

    ExceptionType type;
    public PayrollException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}