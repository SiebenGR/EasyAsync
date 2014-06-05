package gr.sieben.easyasync;

/**
 * The callbacks that can be used in the annotated method as a parameter in order to determine the states of the
 * background job that is invoked.
 * <li><b>BEFORE_EXECUTE</b> Before the asynchronous execution. This is currently in the <u>main</u> thread.</li>
 * <li><b>ON_BACKGROUND</b> This is invoked in the background. This is currently in the <u>background</u> thread.
 * Do not use any ui changes without the proper implementation.</li>
 * <li><b>AFTER_EXECUTE</b> After the asynchronous execution. This is currently in the <u>main</u> thread</li>
 * <li><b>CANCELLED</b> This is invoked when {@linkplain gr.sieben.easyasync.EasyAsync#cancel(String)} is invoked. Cancels the
 *  background job. {@linkplain #AFTER_EXECUTE} is never invoked.</li>
 */
public enum EasyAsyncCallbacks {
    BEFORE_EXECUTE, ON_BACKGROUND, AFTER_EXECUTE, CANCELLED;

    @Override
    public String toString() {
        if(this == BEFORE_EXECUTE)
            return "BEFORE_EXECUTE";
        if (this == ON_BACKGROUND)
            return "ON_BACKGROUND";
        if (this == AFTER_EXECUTE)
            return "AFTER_EXECUTE";
        if(this == CANCELLED)
            return "CANCELLED";
        return "";
    }
}
