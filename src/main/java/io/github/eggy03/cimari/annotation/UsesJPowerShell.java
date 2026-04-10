package io.github.eggy03.cimari.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated component relies on the
 * {@link com.profesorfalken.jpowershell.PowerShell jPowerShell} library
 * for executing PowerShell commands.
 *
 * <p>
 * The underlying {@code jPowerShell} implementation maintains shared internal state
 * and global configuration, which may lead to unexpected behavior when used
 * concurrently across multiple threads or executors.
 * </p>
 *
 * <h2>Implications</h2>
 * <ul>
 *   <li>Not safe for concurrent use</li>
 *   <li>May throw runtime exceptions when accessed by multiple threads</li>
 * </ul>
 *
 * <p>
 * For concurrent or multithreaded use cases, prefer methods that are annotated with {@link IsolatedPowerShell}
 * </p>
 *
 * <p>
 * This annotation is for documentation purposes only and does not enforce or verify any behavior.
 * </p>
 *
 * @see IsolatedPowerShell
 * @since 4.1.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@Documented
public @interface UsesJPowerShell {
}
