/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.mapping;

import org.jspecify.annotations.NonNull;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.TypeFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A common mapping interface for mapping JSON strings to Java objects.
 * <p>
 * Provides default methods to deserialize JSON responses
 * into either a {@link List} of objects or a single {@link Optional} object.
 * The default methods in this interface use Jackson for JSON deserialization.
 * </p>
 *
 * @param <S> the entity type returned by the service implementation
 * @since 1.0.0
 */
public interface CommonMappingInterface<S> {

    /**
     * Configure the {@link ObjectMapper} to be used for JSON processing.
     *
     * <p>
     * The default implementation returns a new {@link ObjectMapper} instance with default configuration,
     * for each invocation.
     * </p>
     *
     * <p>
     * Custom implementations may override this method to provide a custom-configured
     * {@link ObjectMapper}.
     * </p>
     *
     * @return the {@link ObjectMapper} to use
     * @since 1.0.0
     */
    default @NonNull ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * Deserializes a JSON string into a list of objects of the specified type {@code <S>}.
     * <ul>
     *     <li>If the JSON represents a single object of type {@code <S>},
     *     it is deserialized into an unmodifiable list containing exactly one object of the given type.</li>
     *     <li>If the JSON represents an array of objects of type {@code <S>},
     *     it is deserialized into an unmodifiable list of objects of the given type.</li>
     *     <li>If the JSON is an empty string, returns an empty unmodifiable list.</li>
     * </ul>
     *
     * @param inputJson   the JSON string to parse
     * @param objectClass the class of the objects in the list
     * @return an immutable, non-null list of objects deserialized from JSON.
     * @throws NullPointerException     if {@code inputJson} or {@code objectClass} is null
     * @throws JacksonException         if {@code inputJson} is parsing fails
     * @throws IllegalArgumentException if {@code inputJson} deserialization to {@code objectClass} fails
     * due to incompatible type, or if {@code inputJson} is not a JSON Array or Object
     * @since 1.0.0
     */
    default @NonNull List<S> mapToList(@NonNull String inputJson, @NonNull Class<S> objectClass) {

        Objects.requireNonNull(objectClass, "objectClass cannot be null");
        Objects.requireNonNull(inputJson, "inputJson cannot be null");

        String trimmedInputJson = inputJson.trim();
        if (trimmedInputJson.isEmpty())
            return Collections.emptyList();

        ObjectMapper mapper = createObjectMapper();
        JsonNode inputJsonNode = mapper.readTree(trimmedInputJson);

        if (inputJsonNode.isArray()) {

            TypeFactory typeFactory = mapper.getTypeFactory();
            JavaType listType = typeFactory.constructCollectionType(List.class, objectClass);

            List<S> result = mapper.convertValue(inputJsonNode, listType);
            return result == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(result));

        } else if (inputJsonNode.isObject()) {

            S result = mapper.convertValue(inputJsonNode, objectClass);
            return result == null ? Collections.emptyList() : Collections.singletonList(result);

        } else
            throw new IllegalArgumentException("Expected JSON Array or Object but got: " + inputJsonNode.getNodeType());
    }

    /**
     * Deserializes a JSON string into an {@link Optional} of the specified type {@code <S>}.
     *
     * @param inputJson   the JSON string to parse
     * @param objectClass the class of the object to which {@code inputJson} will be deserialized to
     * @return an {@link Optional} of type {@code <S>}
     * @throws NullPointerException     if {@code inputJson} or {@code objectClass} is null
     * @throws JacksonException         if {@code inputJson} parsing fails
     * @throws IllegalArgumentException if {@code inputJson} deserialization to {@code objectClass} fails
     * due to incompatible type, or if {@code inputJson} is not a JSON Object
     * @since 1.0.0
     */
    default @NonNull Optional<S> mapToObject(@NonNull String inputJson, @NonNull Class<S> objectClass) {

        Objects.requireNonNull(objectClass, "objectClass cannot be null");
        Objects.requireNonNull(inputJson, "inputJson cannot be null");

        String trimmedInputJson = inputJson.trim();
        if (trimmedInputJson.isEmpty())
            return Optional.empty();

        ObjectMapper mapper = createObjectMapper();
        JsonNode inputJsonNode = mapper.readTree(trimmedInputJson);

        if (inputJsonNode.isObject()) {
            S result = mapper.convertValue(inputJsonNode, objectClass);
            return Optional.ofNullable(result);
        } else
            throw new IllegalArgumentException("Expected JSON Object but got: " + inputJsonNode.getNodeType());
    }
}