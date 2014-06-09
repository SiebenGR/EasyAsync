package gr.sieben.easyasync;

import android.os.AsyncTask;

/**
 * Generic type AsyncTask that is used for the asynchronous calls.
 * Each instance is used for each background job
 */
class EasyAsyncTask extends AsyncTask<Void, Void, Void>{

    private boolean completed;

    /**
     * Whether the async task has completed its task
     * @return True if completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    private boolean started;
    /**
     * Whether the async task has started its task
     * @return True if started, false otherwise
     */
    public boolean hasStarted() {
        return started;
    }

    private String id; //the background job id

    /**
     * Setter for the id of the background job in the annotated method
     * @param id The id of the annotated method
     */
    public void setId(String id) {
        this.id = id;
    }

    private AsyncTaskCallbacks callbacks;

    /**
     * Setters for the asynchronous callbacks.
     * @param callbacks The interface related callbacks
     */
    public void setCallbacks(AsyncTaskCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    /*
    Callbacks generated from the async task
     */
    @Override
    protected void onPreExecute() {
        started = true;
        completed = false;
        callbacks.onResponse(id, EasyAsyncCallbacks.BEFORE_EXECUTE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        callbacks.onResponse(id, EasyAsyncCallbacks.ON_BACKGROUND);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callbacks.onResponse(id, EasyAsyncCallbacks.AFTER_EXECUTE);
        completed = true;
    }

    @Override
    protected void onCancelled() {
        callbacks.onResponse(id, EasyAsyncCallbacks.CANCELLED);
    }
}
