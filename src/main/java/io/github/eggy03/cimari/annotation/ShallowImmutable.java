package io.github.eggy03.cimari.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class is considered {@code ShallowImmutable} when:
 * <ul>
 *   <li>All fields are {@code final} and cannot be reassigned</li>
 *   <li>The class does not mutate its internal state after construction
 *   (e.g., it does not modify collections or mutable objects held in its fields)</li>
 *
 *   <li>Objects referenced by the fields may be mutable and can be modified externally</li>
 * </ul>
 *
 * <p>
 *     <b>Warning:</b> Shallow immutability does not guarantee thread safety.
 *     If referenced objects are mutable, external modification can lead to
 *     unpredictable behavior in concurrent environments.
 * </p>
 *
 * <p>
 *     This annotation is for documentation purposes only and does not enforce or verify immutability.
 * </p>
 *
 * @since 4.1.0
 * @see DeepImmutable
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Documented
public @interface ShallowImmutable {
}
