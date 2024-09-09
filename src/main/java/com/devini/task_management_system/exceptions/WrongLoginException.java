package com.devini.task_management_system.exceptions;

public class WrongLoginException extends RuntimeException{
    public WrongLoginException() { super("Wrong username or password.");}

    public WrongLoginException(String message) {super(message);}
}
