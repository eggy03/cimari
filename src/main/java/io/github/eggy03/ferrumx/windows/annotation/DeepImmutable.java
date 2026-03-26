package io.github.eggy03.ferrumx.windows.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class is considered {@code DeepImmutable} when:
 * <ul>
 *   <li>All fields are {@code final} and cannot be reassigned</li>
 *   <li>All objects reachable from the instance are immutable (the entire object graph is immutable)</li>
 *   <li>No mutable internal state is exposed</li>
 * </ul>
 *
 * <p>
 *     Classes annotated with {@code DeepImmutable} are thread-safe,
 *     provided they are safely constructed (the {@code this} reference does not escape during construction)
 *     and do not expose mutable state.
 * </p>
 *
 * <p>
 *     This annotation is for documentation purposes only and does not enforce or verify immutability.
 * </p>
 *
 * @since 4.1.0
 * @see ShallowImmutable
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Documented
public @interface DeepImmutable {
}