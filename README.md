EasyAsync
=========

A library made to make asynchronous calls in android very easy

EasyAsync library is a library that makes asynchronous calls very easy and compact, minimizing the boilerplate
code that a developer has to write. All the asynchronous calls are invoked inside an {@link android.os.AsyncTask} in a
retained {@link android.app.Fragment} or a {@link android.support.v4.app.Fragment}
<p>
Acquire the object as a singleton
<code>
    {@literal @}EasyAsync.getInstance();
</code></p>
The {@link gr.sieben.easyasync.EasyAsync} interaction must be made inside a {@link android.support.v4.app.FragmentActivity} or
a {@link android.app.Activity}.
<p>
Firstly you have to initialise the object by using one of the following:
<li>
{@link #init(android.support.v4.app.FragmentManager, OnEasyAsyncFinished)}
</li>
<li>
{@link #init(android.app.FragmentManager, OnEasyAsyncFinished)}
</li>
</p>

<b>NOTE: During configuration changes you have to avoid reinitialising the object</b>
You have to implement the {@link gr.sieben.easyasync.OnEasyAsyncFinished} when you want to start an asynchronous call during initialization
and not after a particular event
<p>
Quick Example 1:
<pre><code>
{@literal @}Override
protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     if(savedInstanceState == null) //if it is called after a configuration change then do not initialise it
         EasyAsync.getInstance().init(getSupportFragmentManager(), null);

     //...other stuff goes here
}
</code></pre>
</p>

<p>
Quick Example 2:
<pre><code>
{@literal @}Override
protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     if(savedInstanceState == null) //if it is called after a configuration change then do not initialise it
         EasyAsync.getInstance().init(getFragmentManager(), new OnEasyAsyncFinished() {
             {@literal @)Override
             public void onFinished() {
                 //you should start an asynchronous operation here
             }
         });

     //...other stuff goes here
}
</code></pre>
</p>
<p>
In the {@link android.app.Activity} or {@link android.support.v4.app.FragmentActivity} class you have to create public methods
that are annotated with {@link gr.sieben.easyasync.BackgroundJob} annotation and you must specify an id. This method will be triggered
in the different states of the {@link android.os.AsyncTask}. Read the {@link android.os.AsyncTask} documentation
<a href="http://developer.android.com/reference/android/os/AsyncTask.html">here</a>.
An {@link gr.sieben.easyasync.BackgroundJob} annotated method must have <u>zero</u> parameters,
<u>one</u> parameter of type {@link gr.sieben.easyasync.EasyAsyncCallbacks} or
<u>two</u> parameters of types ({@link gr.sieben.easyasync.EasyAsyncCallbacks}, {@link gr.sieben.easyasync.EasyAsyncResult}).
<li>
{@link gr.sieben.easyasync.EasyAsyncCallbacks} indicate the AsyncTask states
</li>
<li>
{@link gr.sieben.easyasync.EasyAsyncResult} is an object that is used across AsyncTask to maintain the parameters and the results of the asynchronous calls
</li>

Full Quick Example:
<pre><code>
{@literal @}BackgroundJob(id = "demoid")
public void demoAsync(EasyAsyncCallbacks callbacks, EasyAsyncResult<String, String> args) {
     if(callbacks == EasyAsyncCallbacks.ON_BACKGROUND) {
         //do stuff in the background and set the result in the args object
         args.setResult(result);
     } else if(callbacks == EasyAsyncCallbacks.AFTER_EXECUTE) {
         //retrieve the result object
         String onPostResult = args.getResult();
         //do stuff on the foreground
     }
}
</code></pre>
</p>
<p>
To start the background job you must use the {@linkplain #start(String)} or {@linkplain #forceStart(String)} method with the desired
background job id that is used in the annotated parameter. Using {@linkplain #start(String)} method the background job is executed once and then
is cached in memory. If you start a specific background job again then the annotated method will be invoked like it has been executed.
If a background job needs to be re-executed then call forceStart, it will re-schedule the async task.
<b>NOTE: If the background job has already finished during an orientation change the annotated method will be invoked again
as though it has just finished, for convenience. You can change this behavior by using {@link gr.sieben.easyasync.EasyAsyncResult#setCallbackInConfigurationChange(boolean)} method.</b>
</p>
<p>
To avoid memory leaks you should invoke {@linkplain #destroy(android.app.Activity)} or {@linkplain #destroy(android.support.v4.app.FragmentActivity)}
in the {@link android.app.Activity#onDestroy()} method of your activity respectively.
</p>
