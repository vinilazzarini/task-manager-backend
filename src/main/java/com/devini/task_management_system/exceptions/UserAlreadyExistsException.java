package com.devini.task_management_system.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException() { super("User already exists!");}

    public UserAlreadyExistsException(String message) {super(message);}
}
