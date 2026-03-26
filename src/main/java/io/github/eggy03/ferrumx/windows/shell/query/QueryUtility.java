/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.shell.query;

import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.annotation.WmiClass;
import io.github.eggy03.ferrumx.windows.exception.AnnotationNotFoundException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;

/**
 * A utility class that provides helper methods that use Java reflection
 * <p>
 * <b>For internal use only</b>
 *
 * @since 4.1.0
 */
@UtilityClass
public class QueryUtility {

    /**
     * <p>
     * Retrieves a {@link WmiClass} value declared on the specified class, at class level.
     * </p>
     *
     * @param tClass the class having the annotation
     * @param <T>    the type of the class
     * @return the annotation value
     * @throws AnnotationNotFoundException if the class to be inspected does not have the {@link WmiClass} annotation
     */
    @NotNull
    public static <T> String getClassNameFromWmiClassAnnotation(@NonNull Class<T> tClass) {
        WmiClass wmiClass = tClass.getAnnotation(WmiClass.class);

        if (wmiClass == null)
            throw new AnnotationNotFoundException("Missing @WmiClass annotation on: " + tClass.getName());

        return wmiClass.className();
    }

    /**
     * Retrieves all {@link SerializedName} values declared on the fields of the specified class
     * and returns them as a comma-separated string.
     *
     * <p>If a field does not declare a {@link SerializedName} annotation,
     * its actual field name is used instead.</p>
     *
     * <p>The method inspects only fields declared directly within the provided class;
     * inherited fields are not included.</p>
     *
     * @param tClass the class whose fields should be inspected
     * @param <T>    the type of the class
     * @return a comma-separated string containing either the "Value" of each
     * {@link SerializedName} annotation or the field name if the annotation is absent, in alphabetical order
     */
    @NotNull
    public static <T> String getPropertiesFromSerializedNameAnnotation(@NonNull Class<T> tClass) {

        StringBuilder properties = new StringBuilder();

        Arrays.stream(tClass.getDeclaredFields())
                .filter(field -> !field.isSynthetic()) // filter out synthetic fields since JaCoCo creates $jacocoData field during tests which fails the assertions. This behavior is not observed in scenarios where code coverage is not run
                .map(field -> {
                    SerializedName property = field.getAnnotation(SerializedName.class);
                    return property != null ? property.value() : field.getName();
                })
                .sorted()
                .forEach(property -> properties.append(property).append(", "));

        // remove trailing ", "
        if (properties.length() > 0) {
            properties.delete(properties.length() - 2, properties.length());
        }

        return properties.toString();
    }
}
