package springbook.ch5.user.service.testservice;

public class TxUserServiceException extends RuntimeException{
    public TxUserServiceException(String message) {
        super(message);
    }
}
