package gr.sieben.easyasync;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BackgroundJob Annotation.
 * Use in methods only. Specify an <b>id</b> for the BackgoundJob.
 * See example of use in {@link gr.sieben.easyasync.EasyAsync} class or in the official documentation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BackgroundJob {
    public String id();
}
