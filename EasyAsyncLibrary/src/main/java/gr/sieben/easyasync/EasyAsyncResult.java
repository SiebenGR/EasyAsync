package gr.sieben.easyasync;

/**
 * Holder Object that is used in the annotation method to preserve variables among the different
 * asynchronous states.
 * It has to be used as a parameter in the annotated method.
 * <li>Generic P object can be set as a parameter object that is preserved in the asynchronous states</li>
 * <li>Generic R object can be set as a result object that is preserver in the asynchronous states</li>
 * <p>
 * Full Quick Example:
 * <pre><code>
 * {@literal @}BackgroundJob(id="demoid")
 * public void asyncMethod(EasyAsyncCallbacks callbacks, EasyAsyncResult<Object, String> args) {
 *      if(callbacks == EasyAsyncCallbacks.BEFORE_EXECUTE) {
 *          args.setParameter(parameter);
 *      } if(callbacks == EasyAsyncCallbacks.ON_BACKGROUND) {
 *          args.setResult(parameter.toString());
 *      } else if(callbacks == EasyAsyncCallbacks.AFTER_EXECUTE) {
 *          show(args.getResult());
 *      }
 * }
 * </code></pre>
 * </p>
 */
public class EasyAsyncResult<P, R>{

    private boolean callbackOnConfigurationChange;

    /**
     * Sets whether the callback should be invoked when a configuration change has occurred in the activity.
     * The callback is invoked if the background job has completed and an orientation change has occurred.
     * <b><i>Default value is set to </i></b><code>true</code>
     * @param callbackOnConfigurationChange The argument to set
     */
    public void setCallbackInConfigurationChange(boolean callbackOnConfigurationChange) {
        this.callbackOnConfigurationChange = callbackOnConfigurationChange;
    }

    /**
     * Gets whether the callback should be called in a configuration change in the activity.
     * @return The current value
     */
    public boolean isCallbackOnConfigurationChangeEnabled() {
        return callbackOnConfigurationChange;
    }

    /**
     * This is used internally in the project. DO NOT MANUALLY CREATE, USE THE PARAMETER IN THE ANNOTATED METHOD.
     * See documentation for more details.
     */
    public EasyAsyncResult() {
        callbackOnConfigurationChange = true;
    }

    private P parameter;

    /**
     * Getter for the parameter object
     * @return Type P object
     */
    @SuppressWarnings("unused") //this is used only within the annotated method
    public P getParameter() {
        return parameter;
    }

    /**
     * Setter for the parameter object
     * @param parameter The parameter that needs to be retained across asynchronous states
     */
    @SuppressWarnings("unused") //this is used only within the annotated method
    public void setParameter(P parameter) {
        this.parameter = parameter;
    }

    private R result;

    /**
     * Getter for the result object
     * @return Type R object
     */
    @SuppressWarnings("unused") //this is used only within the annotated method
    public R getResult() {
        return result;
    }

    /**
     * Setter for the result object
     * @param result The parameter that needs to be retained across asynchronous states
     */
    @SuppressWarnings("unused") //this is used only within the annotated method
    public void setResult(R result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "parameter of type"+parameter.getClass().toString()+" and result of "+result.getClass().toString();
    }
}
