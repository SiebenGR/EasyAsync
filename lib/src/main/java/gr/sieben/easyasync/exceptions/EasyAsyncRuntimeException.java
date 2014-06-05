package gr.sieben.easyasync.exceptions;

/**
 * Runtime Exception related to the EasyAsync framework.
 */
public class EasyAsyncRuntimeException extends RuntimeException {

    public EasyAsyncRuntimeException(String message)
    {
        super (message);
    }

    public EasyAsyncRuntimeException (String message, Throwable cause)
    {
        super (message, cause);
    }
}
