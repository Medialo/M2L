package fr.koala.m2l.exception;

public class ObjectArgsCounterNullException extends Exception {


    public ObjectArgsCounterNullException() {
        super("Error: Un object contable na pas redéfinit sa méthode argsCount() (implements ObjectArgsCountable)");
    }

    public ObjectArgsCounterNullException(String message) {
        super(message);
    }

//    public ObjectArgsCounterNullException(String message, Throwable cause) {
//        super(message, cause);
//    }
}
