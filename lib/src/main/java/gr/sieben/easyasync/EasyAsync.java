package gr.sieben.easyasync;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import gr.sieben.easyasync.exceptions.EasyAsyncRuntimeException;

/**
 * Base class for the EasyAsync library.
 * EasyAsync library is a library that makes asynchronous calls very easy and compact, minimizing the boilerplate
 * code that a developer has to write. All the asynchronous calls are invoked inside an {@link android.os.AsyncTask} in a
 * retained {@link android.app.Fragment} or a {@link android.support.v4.app.Fragment}
 * <p>
 * Acquire the object as a singleton
 * <code>
 *     {@literal @}EasyAsync.getInstance();
 * </code></p>
 * The {@link gr.sieben.easyasync.EasyAsync} interaction must be made inside a {@link android.support.v4.app.FragmentActivity} or
 * a {@link android.app.Activity}.
 * <p>
 * Firstly you have to initialise the object by using one of the following:
 * <li>
 * {@link #init(android.support.v4.app.FragmentManager)}
 * </li>
 * <li>
 * {@link #init(android.app.FragmentManager)}
 * </li>
 * </p>
 *<p>
 * Quick Example 1:
 * <pre><code>
 * {@literal @}Override
 * protected void onCreate(Bundle savedInstanceState) {
 *      super.onCreate(savedInstanceState);
 *      setContentView(R.layout.activity_main);
 *
 *      EasyAsync.getInstance().init(getSupportFragmentManager(), null);
 *
 *      //...other stuff goes here
 * }
 * </code></pre>
 * </p>
 *
 * <p>
 * Quick Example 2:
 * <pre><code>
 * {@literal @}Override
 * protected void onCreate(Bundle savedInstanceState) {
 *      super.onCreate(savedInstanceState);
 *      setContentView(R.layout.activity_main);
 *
 *      EasyAsync.getInstance().init(getFragmentManager(), new OnEasyAsyncFinished() {
 *      //you can now start asynchronous operations here
 *
 *      //other stuff goes here...
 * }
 * </code></pre>
 * </p>
 * <p>
 * In the {@link android.app.Activity} or {@link android.support.v4.app.FragmentActivity} class you have to create public methods
 * that are annotated with {@link gr.sieben.easyasync.BackgroundJob} annotation and you must specify an id. This method will be triggered
 * in the different states of the {@link android.os.AsyncTask}. Read the {@link android.os.AsyncTask} documentation
 * <a href="http://developer.android.com/reference/android/os/AsyncTask.html">here</a>.
 * An {@link gr.sieben.easyasync.BackgroundJob} annotated method must have <u>zero</u> parameters,
 * <u>one</u> parameter of type {@link gr.sieben.easyasync.EasyAsyncCallbacks} or
 * <u>two</u> parameters of types ({@link gr.sieben.easyasync.EasyAsyncCallbacks}, {@link gr.sieben.easyasync.EasyAsyncResult}).
 * <li>
 * {@link gr.sieben.easyasync.EasyAsyncCallbacks} indicate the AsyncTask states
 * </li>
 * <li>
 * {@link gr.sieben.easyasync.EasyAsyncResult} is an object that is used across AsyncTask to maintain the parameters and the results of the asynchronous calls
 * </li>
 *
 * Full Quick Example:
 * <pre><code>
 * {@literal @}BackgroundJob(id = "demoid")
 * public void demoAsync(EasyAsyncCallbacks callbacks, EasyAsyncResult<String, String> args) {
 *      if(callbacks == EasyAsyncCallbacks.ON_BACKGROUND) {
 *          //do stuff in the background and set the result in the args object
 *          args.setResult(result);
 *      } else if(callbacks == EasyAsyncCallbacks.AFTER_EXECUTE) {
 *          //retrieve the result object
 *          String onPostResult = args.getResult();
 *          //do stuff on the foreground
 *      }
 * }
 * </code></pre>
 * </p>
 * <p>
 * To start the background job you must use the {@linkplain #start(String)} or {@linkplain #forceStart(String)} method with the desired
 * background job id that is used in the annotated parameter. Using {@linkplain #start(String)} method the background job is executed once and then
 * is cached in memory. If you start a specific background job again then the annotated method will be invoked like it has been executed.
 * If a background job needs to be re-executed then call forceStart, it will re-schedule the async task.
 * <b>NOTE: If the background job has already finished during an orientation change the annotated method will be invoked again
 * as though it has just finished, for convenience. You can change this behavior by using {@link gr.sieben.easyasync.EasyAsyncResult#setCallbackInConfigurationChange(boolean)} method.</b>
 * </p>
 * <p>
 * To avoid memory leaks you should invoke {@linkplain #destroy(android.app.Activity)} or {@linkplain #destroy(android.support.v4.app.FragmentActivity)}
 * in the {@link android.app.Activity#onDestroy()} method of your activity respectively.
 * </p>
 */
public class EasyAsync {
    //easyAsync instance
    private static EasyAsync easyAsync;

    //private constructor for the singleton protocol
    private EasyAsync() {}

    /**
     * Retrieves the EasyAsync object as a singleton. Read the documentation for more details.
     * @return The EasyAsync object as a singleton
     */
    public static EasyAsync getInstance() {
        if(easyAsync == null) {
            easyAsync = new EasyAsync();
        }
        return easyAsync;
    }

    /**
     * ConcurrentHashMap to avoid deadlocks and crashes when simultaneous access to the object.
     */
    ConcurrentHashMap<String, ObjectHolder> mAnnotatedMethods = new ConcurrentHashMap<String, ObjectHolder>();

    /*
    Holders of the retained Fragments. Each time one of each would be null.
     */
    private RetainedSupportFragment retainedSupportFragment;
    private RetainedFragment retainedFragment;

    /**
     * Basic method for initialisation. For pre Honeycomb devices.
     * <b>This must be called only one time and not during configuration changes</b>
     * See documentation for more details.
     * @param fragmentManager The fragment manager of the current Activity. FragmentManager must not have pending transactions.
     */
    public void init(FragmentManager fragmentManager) {
        Fragment tagFragment = fragmentManager.findFragmentByTag(FragmentController.FRAGMENT_TAG);
        if(tagFragment != null)
            retainedSupportFragment = (RetainedSupportFragment) tagFragment;
        else {
            retainedSupportFragment = RetainedSupportFragment.newInstance();
            fragmentManager.beginTransaction().add(retainedSupportFragment,
                    FragmentController.FRAGMENT_TAG).commit();
            fragmentManager.executePendingTransactions();
        }
    }

    /**
     * Basic method for initialisation. For after Honeycomb devices.
     * <b>This must be called only one time and not during configuration changes</b>
     * See documentation for more details.
     * @param fragmentManager The fragment manager of the current Activity. FragmentManager must not have pending transactions.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void init(android.app.FragmentManager fragmentManager) {
        android.app.Fragment tagFragment = fragmentManager.findFragmentByTag(FragmentController.FRAGMENT_TAG);
        if(tagFragment != null)
            retainedFragment = (RetainedFragment) tagFragment;
        else {
            retainedFragment = RetainedFragment.newInstance();
            fragmentManager.beginTransaction().add(retainedFragment,
                    FragmentController.FRAGMENT_TAG).commit();
            fragmentManager.executePendingTransactions();
        }
    }

    /**
     * Start an asynchronous background job for the current id. The id is defined in the @BackgroundJob annotation
     * @param id The asynchronous method id to start
     */
    public void start(String id) {
        //if the annotation method does not exist
        if(!mAnnotatedMethods.containsKey(id)) {
            throw new EasyAsyncRuntimeException("Method with id="+id+" is not declared in any of the target Activity");
        }
        //starts the async task through the available fragment
        if(retainedSupportFragment == null) {
            retainedFragment.startAsync(id, false);
        } else {
            retainedSupportFragment.startAsync(id, false);
        }
    }

    /**
     * Force start an asynchronous background job for the current id. Force start must be used when the annotated @BackgroundJob must be restarted if not yet started.
     * @param id The asynchronous method id to start
     */
    public void forceStart(String id) {
        //if the annotation method does not exist
        if(!mAnnotatedMethods.containsKey(id)) {
            throw new EasyAsyncRuntimeException("Method with id="+id+" is not declared in any of the target Activity");
        }
        //starts the async task through the available fragment
        if(retainedSupportFragment == null) {
            retainedFragment.startAsync(id, true);
        } else {
            retainedSupportFragment.startAsync(id, true);
        }
    }

    /**
     * Cancels an asynchronous background job for the current id. This should trigger a callback of type {@link gr.sieben.easyasync.EasyAsyncCallbacks#CANCELLED}
     * in the annotated method
     * @param id The asynchronous method id to start
     */
    public void cancel(String id) {
        //cancels the async task of the respective fragment
        if(retainedSupportFragment == null) {
            retainedFragment.cancelAsync(id);
        } else {
            retainedSupportFragment.cancelAsync(id);
        }
    }

    /**
     * Must be called in the onDestroy method of the activity
     * @param activity the activity as a parameter
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void destroy(Activity activity) {
        if(activity.isFinishing()) {
            destroy();
        }
    }

    /**
     * Must be called in the onDestroy method of the FragmentActivity
     * @param activity the activity as a parameter
     */
    public void destroy(FragmentActivity activity) {
        if(activity.isFinishing()) {
            destroy();
        }
    }

    //overloaded method to clear the references
    private void destroy() {
        mAnnotatedMethods.clear();
        retainedFragment = null;
        retainedSupportFragment = null;
    }

    /**
     * Method that is used to find annotations of the @BackgroundJob in target class
     * @param target The target object
     */
    void findAnnotations(Object target) {
        Class<?> targetClass = target.getClass();
        //if we reach an android component or a java component we stop searching
        if (targetClass.getName().startsWith("android.") || targetClass.getName().startsWith("java.")) {
            return;
        }
        BackgroundJob annotation;
        for(Method method : targetClass.getMethods()) {
            if(method.isAnnotationPresent(BackgroundJob.class)) {
                annotation = method.getAnnotation(BackgroundJob.class);

                if(annotation.id() == null)  //annotation needs a unique id to work, throw an exception otherwise
                    throw new EasyAsyncRuntimeException("You have to specify an id in class "+
                            targetClass.getName()+" and in annotated method "+method.getName()+
                            ". See documentation for more details");
                if(mAnnotatedMethods.containsKey(annotation.id())
                        && mAnnotatedMethods.get(annotation.id()).getTarget() != null) {
                        //checking for a unique key inside, we also check the target to be null
                        //because there might be an orientation change so we must reinitialise the targets of each async
                    ObjectHolder similarMethod = mAnnotatedMethods.get(annotation.id());
                    throw new EasyAsyncRuntimeException("You must specify a unique id in the annotated method. In class " +
                            targetClass.getName() + " method " + method.getName() + " has the same id with method " +
                            similarMethod.getAnnotatedMethod().getName() + " in class " +
                            similarMethod.getTarget().getClass().getName());
                }

                //this condition checks whether we should create the annotation or reset the targets
                if(!mAnnotatedMethods.containsKey(annotation.id()))
                    mAnnotatedMethods.put(annotation.id(), createObjectHolder(target, method));
                else
                    setObjectHolder(annotation.id(), target, method);
            }
        }
    }

    /**
     * Helper method for clearing the target Activity and annotated Method
     * This has to be done in an orientation change in order to avoid any activity leaks
     */
    void clearAnnotations() {
        for(Map.Entry<String, ObjectHolder> holder : mAnnotatedMethods.entrySet()) {
            holder.getValue().setTarget(null);
            holder.getValue().setMethodType(null);
            holder.getValue().setAnnotatedMethod(null);
        }
    }

    //setting targets
    private void setObjectHolder(String id, Object target, Method method) {
        ObjectHolder holder = mAnnotatedMethods.get(id);
        holder.setAnnotatedMethod(method);
        holder.setTarget(target);
        holder.setMethodType(getParameterMethodType(target, method));
    }

    //creating during init
    private ObjectHolder createObjectHolder(Object target, Method method) {
        ObjectHolder holder = new ObjectHolder();
        holder.setAnnotatedMethod(method);
        holder.setTarget(target);
        holder.setMethodType(getParameterMethodType(target, method));
        return holder;
    }

    //returns the Method type according to the AnnotatedMethodType for easier use
    private AnnotatedMethodType getParameterMethodType(Object target, Method method) {
        Class[] types = method.getParameterTypes();
        AnnotatedMethodType methodType = null;
        if(types == null || types.length == 0)
            methodType = AnnotatedMethodType.NO_PARAM;
        else {
            int typesSize = types.length;
            switch (typesSize) {
                case 1:
                    if (types[0] == EasyAsyncCallbacks.class)
                        methodType = AnnotatedMethodType.TYPE_CALLBACKS;
                    else
                        throw new EasyAsyncRuntimeException("Method " + method.getName() + " in class " + target.getClass().getName() +
                                " has one parameter and it is not of type AnnotatedMethodType. " +
                                "See documentation for more details");
                    break;
                case 2:
                    if(types[0] == EasyAsyncCallbacks.class && types[1] == EasyAsyncResult.class)
                        methodType = AnnotatedMethodType.TYPE_RESULT;
                    else
                        throw new EasyAsyncRuntimeException("Method "+method.getName()+" in class "+target.getClass().getName()+
                                " has two parameters and they are not of type (AnnotatedMethodType, EasyAsyncResult). " +
                                "See documentation for more details");
                    break;
            }
        }

        return methodType;
    }

    /**
     * This method is called when we want to invoke any annotated method in the target Activity
     * @param id The id of the method to be invoked
     * @param callbacks The callbacks that will be passed in the method
     * @param args The arguments that will be passed
     */
    synchronized void invokeMethod(String id, EasyAsyncCallbacks callbacks, EasyAsyncResult args) {
        if(mAnnotatedMethods.size() == 0) {
            return;
        }
        ObjectHolder holder = mAnnotatedMethods.get(id);
        AnnotatedMethodType parameterType = holder.getMethodType();
        //we categorize the invokation depending on the number of the parameters specified
        if(parameterType == AnnotatedMethodType.NO_PARAM)
            try {
                holder.getAnnotatedMethod().invoke(holder.getTarget());
            } catch (Exception e) {
                throw new EasyAsyncRuntimeException("Problem when invoking method "+holder.getAnnotatedMethod().getName()+" with " +
                        "no parameter", e);
            }
        else if(parameterType == AnnotatedMethodType.TYPE_CALLBACKS)
            try {
                holder.getAnnotatedMethod().invoke(holder.getTarget(), callbacks);
            } catch (Exception e) {
                throw new EasyAsyncRuntimeException("Problem when invoking method "+holder.getAnnotatedMethod().getName()+" with " +
                        "one parameter of type EasyAsyncCallbacks "+callbacks.toString(), e);
            }
        else if(parameterType == AnnotatedMethodType.TYPE_RESULT)
            try {
                holder.getAnnotatedMethod().invoke(holder.getTarget(), callbacks, args);
            } catch (Exception e) {
                throw new EasyAsyncRuntimeException("Problem when invoking method "+holder.getAnnotatedMethod().getName()+" with " +
                        "two parameter of type EasyAsyncCallbacks "+callbacks.toString()+
                        " and EasyAsyncResult"+args.toString(), e);
            }
    }
}
