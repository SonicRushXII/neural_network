package srx12.neuralnetwork.loader.image;

public class LoaderException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public LoaderException(String message, Throwable cause) {
        super(message, cause);
    }
    public LoaderException(String message) {
        super(message);
    }
}
