package gr.sieben.easyasync;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Helper FragmentActivity that implements all the needed operations for the EasyAsync implementation.
 * It is highly recommended to read the {@link gr.sieben.easyasync.EasyAsync} documentation before use.
 * You have to implement {@link gr.sieben.easyasync.BackgroundJob} annotation in methods.
 * <p>
 * Available Methods:
 * <li>{@linkplain #startBackgroundJob(String)} Starts a new background job with the respective id</li>
 * <li>{@linkplain #forceStartBackgroundJob(String)} Force starts a new background job with the respective id</li>
 * <li>{@linkplain #cancelBackgroundJob(String)} Cancels a background job with the respective id</li>
 * </p>
 */
public abstract class EasyAsyncFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyAsync.getInstance().init(getSupportFragmentManager());
    }

    @Override
    protected void onDestroy() {
        EasyAsync.getInstance().destroy(this);
        super.onDestroy();
    }

    /**
     * Cancels a background job with the respective id
     * @param id The background job id
     */
    @SuppressWarnings("unused") //this is used only in the client code
    protected void cancelBackgroundJob(String id) {
        EasyAsync.getInstance().cancel(id);
    }

    /**
     * Starts a new background job with the respective id
     * @param id The background job id
     */
    @SuppressWarnings("unused") //this is used only in the client code
    protected void startBackgroundJob(String id) {
        EasyAsync.getInstance().start(id);
    }

    /**
     * Force starts a new background job with the respective id
     * @param id The background job id
     */
    @SuppressWarnings("unused") //this is used only in the client code
    protected void forceStartBackgroundJob(String id) {
        EasyAsync.getInstance().forceStart(id);
    }
}
