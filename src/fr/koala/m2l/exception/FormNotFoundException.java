package fr.koala.m2l.exception;

public class FormNotFoundException extends Exception {

    private FormNotFoundException() {
        super("Try to load form but doesnt exist!");
    }

    public FormNotFoundException(String formKey) {
        super("Try to load form :" + formKey + " but doesnt exist!");
    }
}
