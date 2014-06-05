package gr.sieben.easyasync;

import java.lang.reflect.Method;

/**
 * Holder that is used and cached and holds all the necessary information
 */
class ObjectHolder {
    private Method annotatedMethod;

    /**
     * Gets the annotated method that will be invoked.
     * @return The annotated method as an object
     */
    public Method getAnnotatedMethod() {
        return annotatedMethod;
    }

    /**
     * Sets the annotated method that will be invoked
     * @param annotatedMethod The annotated method as an object
     */
    public void setAnnotatedMethod(Method annotatedMethod) {
        this.annotatedMethod = annotatedMethod;
    }

    private EasyAsyncTask asyncTask;

    /**
     * Getter for the AsyncTask that is connected to the specific background job.
     * @return The related EasyAsyncTask
     */
    public EasyAsyncTask getAsyncTask() {
        return asyncTask;
    }

    /**
     * Setter for the AsyncTask that is connected to the specific background job.
     * @param asyncTask The related EasyAsyncTask
     */
    public void setAsyncTask(EasyAsyncTask asyncTask) {
        this.asyncTask = asyncTask;
    }

    private Object target;

    /**
     * Getter for the target activity that the annotated methods reside
     * @return The Activity as an object
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Getter for the target activity that the annotated methods reside
     * @param target The Activity as an object
     */
    public void setTarget(Object target) {
        this.target = target;
    }

    private AnnotatedMethodType methodType;

    /**
     * Getter for the annotated method type as an enumerator
     * @return The method type
     */
    public AnnotatedMethodType getMethodType() {
        return methodType;
    }

    /**
     * Setter for the annotated method type as an enumerator
     * @param methodType The method type
     */
    public void setMethodType(AnnotatedMethodType methodType) {
        this.methodType = methodType;
    }
}
