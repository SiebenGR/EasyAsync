package gr.sieben.easyasync;

/**
 * Interface callback that is used by the AsyncTask to communicate with the related {@link gr.sieben.easyasync.FragmentController}
 */
interface AsyncTaskCallbacks  {
    public void onResponse(String id, EasyAsyncCallbacks easyAsyncCallbacks);
}
