/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
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

    ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    /**
     * Configure the {@link ObjectMapper} to be used for JSON processing.
     * <p>
     * The default implementation returns a shared {@link #DEFAULT_MAPPER} instance.
     * Implementations may override this method to provide a custom-configured
     * {@link ObjectMapper}
     * </p>
     *
     * @return the {@link ObjectMapper} to use
     * @since 1.0.0
     */
    default @NotNull ObjectMapper objectMapper() {
        return DEFAULT_MAPPER;
    }

    /**
     * Deserializes a JSON string into a list of objects of the specified type {@code <S>}.
     * <ul>
     *     <li>If the JSON represents a single object of type {@code <S>},
     *     it is deserialized into an unmodifiable list containing exactly one object of the given type.</li>
     *     <li>If the JSON represents an array of objects of type {@code <S>},
     *     it is deserialized into an unmodifiable list of objects of the given type.</li>
     *     <li>If the JSON is null or empty, returns an empty unmodifiable list.</li>
     * </ul>
     *
     * @param inputJson   the JSON string to parse
     * @param objectClass the class of the objects in the list
     * @return an immutable, non-null list of objects deserialized from JSON.
     * @throws NullPointerException     if {@code inputJson} or {@code objectClass} is null
     * @throws JacksonException         if {@code inputJson} parsing fails
     * @throws IllegalArgumentException if deserialization of {@code inputJson} to {@code objectClass} fails due to incompatible type
     * @since 1.0.0
     */
    @NotNull
    @Unmodifiable
    default List<S> mapToList(String inputJson, Class<S> objectClass) { //todo add jspecify not null annotations

        Objects.requireNonNull(objectClass, "objectClass cannot be null");
        Objects.requireNonNull(inputJson, "inputJson cannot be null");

        String trimmedInputJson = inputJson.trim();
        if (trimmedInputJson.isEmpty())
            return Collections.emptyList();

        JsonNode inputJsonNode = objectMapper().readTree(trimmedInputJson);

        if (inputJsonNode.isArray()) {

            TypeFactory typeFactory = objectMapper().getTypeFactory();
            JavaType listType = typeFactory.constructCollectionType(List.class, objectClass);

            List<S> result = objectMapper().convertValue(inputJsonNode, listType);
            return result == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(result));

        } else if (inputJsonNode.isObject()) {

            S result = objectMapper().convertValue(inputJsonNode, objectClass);
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
     * @throws IllegalArgumentException if deserialization of {@code inputJson} to {@code objectClass} fails due to incompatible type
     * @since 1.0.0
     */
    @NotNull
    default Optional<S> mapToObject(String inputJson, Class<S> objectClass) { //todo add jspecify not null annotations

        Objects.requireNonNull(objectClass, "objectClass cannot be null");
        Objects.requireNonNull(inputJson, "inputJson cannot be null");

        String trimmedInputJson = inputJson.trim();
        if (trimmedInputJson.isEmpty())
            return Optional.empty();

        JsonNode inputJsonNode = objectMapper().readTree(trimmedInputJson);

        if (inputJsonNode.isObject()) {
            S result = objectMapper().convertValue(inputJsonNode, objectClass);
            return Optional.ofNullable(result);
        } else
            throw new IllegalArgumentException("Expected JSON Object but got: " + inputJsonNode.getNodeType());
    }
}