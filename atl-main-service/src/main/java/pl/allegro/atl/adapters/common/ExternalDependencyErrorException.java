package pl.allegro.atl.adapters.common;

public class ExternalDependencyErrorException extends RuntimeException {

    public ExternalDependencyErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
