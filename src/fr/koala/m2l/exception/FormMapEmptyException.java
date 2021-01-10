package fr.koala.m2l.exception;

public class FormMapEmptyException extends Exception {

    public FormMapEmptyException() {
        super("Try to load form but map Form is Null");
    }

    public FormMapEmptyException(String message) {
        super(message);
    }
}
