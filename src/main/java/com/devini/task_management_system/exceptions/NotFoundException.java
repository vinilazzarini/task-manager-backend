package com.devini.task_management_system.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException() { super("User already exists!");}

    public NotFoundException(String message) {super(message);}
}
