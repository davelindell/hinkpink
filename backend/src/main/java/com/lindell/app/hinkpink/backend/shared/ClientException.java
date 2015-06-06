package com.lindell.app.hinkpink.backend.shared;

/**
 * Created by lindell on 6/1/15.
 */
@SuppressWarnings("serial")
public class ClientException extends Exception {

    public ClientException() {
        return;
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(Throwable throwable) {
        super(throwable);
    }

    public ClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

}