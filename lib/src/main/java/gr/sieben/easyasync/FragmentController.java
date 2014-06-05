package gr.sieben.easyasync;

import java.util.Map;

/**
 * A controller class that controls the data in fragments in order to maintain scalability
 */
class FragmentController implements AsyncTaskCallbacks {

    public static String FRAGMENT_TAG = "asyncfragmenttag";

    private EasyAsyncResult args; //argument object that needs to be retained and passed through different async states

    public FragmentController() {
        args = new EasyAsyncResult();
    }

    /**
     * Method that starts the respective AsyncTask for the background job specified
     * @param id The id to be started
     * @param isForced True if it has to be restarted, false otherwise
     */
    public void startAsync(String id, boolean isForced) {
        EasyAsyncTask asyncTask = EasyAsync.getInstance().mAnnotatedMethods.get(id).getAsyncTask();
        if(!isForced && asyncTask != null) {
            return;
        }
        asyncTask = new EasyAsyncTask();
        asyncTask.setCallbacks(this);
        asyncTask.setId(id);
        EasyAsync.getInstance().mAnnotatedMethods.get(id).setAsyncTask(asyncTask);
        asyncTask.execute();
    }

    /**
     * Cancels the async task
     * @param id The id of the AsyncTask to cancel
     */
    public void cancelAsync(String id) {
        EasyAsync.getInstance().mAnnotatedMethods.get(id).getAsyncTask().cancel(true);
    }

    /**
     * The callback that is called from the AsyncTask that indicates a change in its state.
     * @param id The id of the background job
     * @param easyAsyncCallbacks The callbackMethod that indicates the current state
     */
    @Override
    public synchronized void onResponse(String id, EasyAsyncCallbacks easyAsyncCallbacks) {
        EasyAsync.getInstance().invokeMethod(id, easyAsyncCallbacks, args);
    }

    /**
     * Method that is called after a configuration change in the activity. Invokes methods with a
     * EasyAsyncCallbacks.AFTER_EXECUTE when the AsyncTask has already been completed and the arguments
     * has set the callbackConfigurationChange to true.
     */
    public void onActivityCreated() {
        for(Map.Entry<String, ObjectHolder> entry : EasyAsync.getInstance().mAnnotatedMethods.entrySet()) {
            EasyAsyncTask asyncTask = entry.getValue().getAsyncTask();
            if(asyncTask != null && asyncTask.hasStarted() && asyncTask.isCompleted()
                    && args.isCallbackOnConfigurationChangeEnabled()) {
                onResponse(entry.getKey(), EasyAsyncCallbacks.AFTER_EXECUTE);
            }
        }
    }

    /**
     * During a configuration change we need to re-find the annotations
     * @param target The activity as a target
     */
    public void onAttach(Object target) {
        EasyAsync.getInstance().findAnnotations(target);
    }

    /**
     * During a configuration change we need to clear all available references to avoid memory leaks
     */
    public void onDetach() {
        EasyAsync.getInstance().clearAnnotations();
    }
}
