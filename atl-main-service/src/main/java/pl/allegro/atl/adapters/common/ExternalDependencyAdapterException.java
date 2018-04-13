package pl.allegro.atl.adapters.common;

public class ExternalDependencyAdapterException extends RuntimeException {

    public ExternalDependencyAdapterException(String message) {
        super(message);
    }

    public ExternalDependencyAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
