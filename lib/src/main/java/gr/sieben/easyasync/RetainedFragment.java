package gr.sieben.easyasync;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;

/**
 * A {@link android.app.Fragment} fragment that will attach to the {@link android.app.FragmentManager} in order
 * to retain its data into configuration changes. Every fragment state are passed to the FragmentController object
 * for scalability
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class RetainedFragment extends Fragment {

    /* we are using a fragment controller to maintain scalability among the
        support.Fragment and the app.Fragment
         */
    private FragmentController mController = new FragmentController();

    public static RetainedFragment newInstance() {
        return new RetainedFragment();
    }

    public RetainedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retain fragment across coniguration changes
        setRetainInstance(true);

        //the fragment is ready for use
        EasyAsync.getInstance().invokeOnFinishedCallback();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mController.onActivityCreated();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mController.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mController.onDetach();
    }

    public void startAsync(String id, boolean isForced) {
        mController.startAsync(id, isForced);
    }

    public void cancelAsync(String id) {
        mController.cancelAsync(id);
    }
}
