package springbook.ch6.user.service;

public class TxUserServiceException extends RuntimeException{
    public TxUserServiceException(String message) {
        super(message);
    }
}
