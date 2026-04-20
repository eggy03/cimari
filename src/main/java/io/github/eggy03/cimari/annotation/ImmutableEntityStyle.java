package io.github.eggy03.cimari.annotation;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * A custom annotation that is intended to be applied with {@link Value.Immutable},
 * on {@link io.github.eggy03.cimari.entity} package classes.
 * </p>
 * <p>
 * It modifies the naming and structural style of the generated immutable implementations via {@link Value.Style}
 * </p>
 *
 * @since 0.1.0
 */
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
@Value.Style(
        typeAbstract = {"*"}, // No prefix or suffix will be detected and trimmed
        typeImmutable = "Immutable*", // generated immutable types will have Immutable as prefix
        visibility = Value.Style.ImplementationVisibility.PUBLIC, // Generated class will be always public
        builder = "new", // construct builder using 'new' instead of factory method (required for Jackson).
        // Generated builders will have attributes annotated with @JsonProperty so deserialization will work properly.
        defaults = @Value.Immutable(copy = true), // Enable copy methods
        passAnnotations = WmiClass.class // this annotation is needed to build queries at runtime from @JsonProperty values
)
public @interface ImmutableEntityStyle {
}