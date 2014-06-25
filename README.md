EasyAsync
=========

A library made to make asynchronous calls in android very easy

EasyAsync library is a library that makes asynchronous calls very easy and compact, minimizing the boilerplate
code that a developer has to write. All the asynchronous calls are invoked inside an android.os.AsyncTask in a
retained android.app.Fragment or a android.support.v4.app.Fragment
<p>
Acquire the object as a singleton
<code>
    EasyAsync.getInstance();
</code></p>
The EasyAsync interaction must be made inside a FragmentActivity or a Activity.
<p>
Firstly you have to initialise the object by using one of the following:
<li>
<code>EasyAsync.getInstance().init(android.support.v4.app.FragmentManager);</code>
</li>
<li>
<code>EasyAsync.getInstance().init(android.app.FragmentManager);</code>
</li>
</p>

<p>
Quick Example 1:
<pre><code>
@Override
protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     EasyAsync.getInstance().init(getSupportFragmentManager());

     //...other stuff goes here
}
</code></pre>
</p>

<p>
Quick Example 2:
<pre><code>
@Override
protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     EasyAsync.getInstance().init(getFragmentManager());
     //you should start an asynchronous operation here

     //...other stuff goes here
}
</code></pre>
</p>
<p>
In the android.app.Activity or android.support.v4.app.FragmentActivity class you have to create public methods
that are annotated with the <code>@BackgroundJob</code> annotation and you must specify an id as a String. This method will be triggered in the different states of the AsyncTask. (Read the AsyncTask documentation <a href="http://developer.android.com/reference/android/os/AsyncTask.html">here</a>.)
A @BackgroundJob annotated method must have <u>zero</u> parameters, <u>one</u> parameter of type <code>EasyAsyncCallbacks</code> or <u>two</u> parameters of types <code>(EasyAsyncCallbacks, EasyAsyncResult)</code>.
<br/>
<b>EasyAsyncCallbacks:</b> indicate the AsyncTask states
<br/>
<b>EasyAsyncResult:</b> is an object that is used across AsyncTask to maintain the parameters and the results of the asynchronous calls
</p>
<p>
Full Quick Example:
<pre><code>
@BackgroundJob(id = "demoid")
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
To start the background job you must use the <code>EasyAsync.getInstance().start(String id)</code> or <code>EasyAsync.getInstance().forceStart(String id)</code> method with the desired background job id that is used in the annotated method. 
<br/>Using <b>EasyAsync.getInstance().start(String id)</b> method the background job is executed once and then
is cached in memory. If you start a specific background job again then the annotated method will be invoked like it has been executed.
<br/>If a background job needs to be re-executed then call <b>EasyAsync.getInstance().forceStart(String id)</b>, it will re-schedule the async task.
</p>
<p>
<b>NOTE: If the background job has already finished during an orientation change the annotated method will be invoked again as though it has just finished, for convenience. You can change this behavior by using <code>EasyAsyncResult.setCallbackInConfigurationChange(boolean)</code> method in the annotated method parameters.</b>
</p>

<p>
To avoid memory leaks you should invoke <code>EasyAsync.getInstance().destroy(android.app.Activity)</code> or <code>EasyAsync.getInstance().destroy(android.support.v4.app.FragmentActivity)</code>
in the <code>onDestroy()</code> method of your Activity respectively.
</p>

License
=========

<pre><code>
Copyright 2014 SiEBEN Innovative Solutions

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</code></pre>
