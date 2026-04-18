/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.mapping.CommonMappingInterface;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommonMappingInterfaceDefaultMethodsTest {

    private final CommonMappingInterface<MockEntity> mapper = new CommonMappingInterface<MockEntity>() {
    };
    private final ObjectMapper objectMapper = mapper.configureObjectMapper();

    // MAP TO OBJECT TESTS

    @Test
    void testMapToObject_validJsonObject_matchesSchema_returnsMockEntityWithNonNullFields() {

        // construct a MockEntity object
        MockEntity constructedMockEntity = new MockEntity(1L, "SomeValue");

        // manually construct json
        ObjectNode mockEntityJsonObject = objectMapper.createObjectNode();
        mockEntityJsonObject.put("ID", 1L);
        mockEntityJsonObject.put("Value", "SomeValue");

        // get it as string
        String mockEntityJson = objectMapper.writeValueAsString(mockEntityJsonObject);

        // deserialize it
        Optional<MockEntity> deserializedMockEntity = mapper.mapToObject(mockEntityJson, MockEntity.class);

        // assert that the deserialization was a success
        assertThat(deserializedMockEntity).contains(constructedMockEntity);
    }

    @Test
    void testMapToObject_validJsonObject_doesNotMatchSchema_returnsMockEntityWithNullFields() {

        String json = "{\"a\" : \"1\" , \"b\" : \"2\"}";
        Optional<MockEntity> deserializedMockEntity = mapper.mapToObject(json, MockEntity.class);

        assertThat(deserializedMockEntity).isPresent();
        assertThat(deserializedMockEntity.get()).isNotNull();
        assertThat(deserializedMockEntity.get().id).isNull();
    }

    @Test
    void testMapToObject_validJsonType_butIsNotObject_throwsIllegalArgumentException() {
        String stringToken = "\"json string\"";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToObject(stringToken, MockEntity.class));

        String nullToken = "null";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToObject(nullToken, MockEntity.class));

        String trueToken = "true";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToObject(trueToken, MockEntity.class));

        String falseToken = "false";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToObject(falseToken, MockEntity.class));

        String jsonArray = "[{\"a\" : \"1\" , \"b\" : \"2\"}, {\"c\" : \"3\" , \"d\" : \"4\"}]";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToObject(jsonArray, MockEntity.class));
    }

    @Test
    void testMapToObject_invalidJsonType_throwsJacksonException() {
        String invalidJson = "invalid json string";
        assertThrows(JacksonException.class, () -> mapper.mapToObject(invalidJson, MockEntity.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void testMapToObject_emptyOrWhiteSpaceJsonString_optionalNotPresent(String json) {
        Optional<MockEntity> deserializedMockEntity = mapper.mapToObject(json, MockEntity.class);
        assertThat(deserializedMockEntity).isNotPresent();
    }

    @Test
    @SuppressWarnings("all")
    void testMapToObject_nullParameters_throwsException() {
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> mapper.mapToObject(null, MockEntity.class));
        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> mapper.mapToObject("", null));
        assertThat(ex1.getMessage()).isEqualTo("inputJson cannot be null");
        assertThat(ex2.getMessage()).isEqualTo("objectClass cannot be null");
    }


    // MAP TO LIST TESTS

    @Test
    void testMapToList_validJsonArray_returnsListOfMockEntitiesWithNonNullFields() {

        // construct two MockEntity objects
        MockEntity constructedMockEntityOne = new MockEntity(1L, "SomeValue");
        MockEntity constructedMockEntityTwo = new MockEntity(2L, "AnotherValue");

        // construct json
        ArrayNode rootArray = objectMapper.createArrayNode();

        ObjectNode mockEntityJsonObjectOne = objectMapper.createObjectNode();
        mockEntityJsonObjectOne.put("ID", 1L);
        mockEntityJsonObjectOne.put("Value", "SomeValue");

        ObjectNode mockEntityJsonObjectTwo = objectMapper.createObjectNode();
        mockEntityJsonObjectTwo.put("ID", 2L);
        mockEntityJsonObjectTwo.put("Value", "AnotherValue");

        rootArray.add(mockEntityJsonObjectOne);
        rootArray.add(mockEntityJsonObjectTwo);

        String mockEntityJson = objectMapper.writeValueAsString(rootArray);

        List<MockEntity> deserializedEntityList = mapper.mapToList(mockEntityJson, MockEntity.class);
        assertThat(deserializedEntityList).contains(constructedMockEntityOne, constructedMockEntityTwo);

        // test immutability
        assertThrows(UnsupportedOperationException.class, () -> deserializedEntityList.add(null));
    }

    @Test
    void testMapToList_validJsonObject_returnsSingletonListOfMockEntityWithNonNullFields() {

        // construct two MockEntity objects
        MockEntity constructedMockEntity = new MockEntity(1L, "SomeValue");

        // manually construct json
        ObjectNode mockEntityJsonObject = objectMapper.createObjectNode();
        mockEntityJsonObject.put("ID", 1L);
        mockEntityJsonObject.put("Value", "SomeValue");

        // get it as string
        String mockEntityJson = objectMapper.writeValueAsString(mockEntityJsonObject);

        List<MockEntity> deserializedEntityList = mapper.mapToList(mockEntityJson, MockEntity.class);
        assertThat(deserializedEntityList)
                .hasSize(1)
                .contains(constructedMockEntity);
    }

    @Test
    void testMapToList_validJsonArrayOrObject_doesNotMatchSchema_returnsListOfMockEntitiesWithNullFields() {
        String jsonArray = "[{\"a\" : \"1\" , \"b\" : \"2\"}, {\"c\" : \"3\" , \"d\" : \"4\"}]";
        String jsonObject = "{\"a\" : \"1\" , \"b\" : \"2\"}";

        List<MockEntity> deserializedEntityListForArray = mapper.mapToList(jsonArray, MockEntity.class);
        List<MockEntity> deserializedEntityListForObject = mapper.mapToList(jsonObject, MockEntity.class);

        assertThat(deserializedEntityListForArray).hasSize(2);

        assertThat(deserializedEntityListForArray.get(0)).isNotNull();
        assertThat(deserializedEntityListForArray.get(1)).isNotNull();

        assertThat(deserializedEntityListForArray.get(0).id).isNull();
        assertThat(deserializedEntityListForArray.get(1).id).isNull();

        assertThat(deserializedEntityListForObject).hasSize(1);

        assertThat(deserializedEntityListForObject.get(0)).isNotNull();
        assertThat(deserializedEntityListForObject.get(0).id).isNull();
    }

    @Test
    void testMapToList_validJsonType_butIsNotArrayOrObject_throwsIllegalArgumentException() {
        String jsonString = "\"json string\"";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToList(jsonString, MockEntity.class));

        String nullToken = "null";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToList(nullToken, MockEntity.class));

        String trueToken = "true";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToList(trueToken, MockEntity.class));

        String falseToken = "false";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToList(falseToken, MockEntity.class));
    }

    @Test
    void testMapToList_invalidJsonType_throwsJacksonException() {
        String invalidJson = "invalid json string";
        assertThrows(JacksonException.class, () -> mapper.mapToList(invalidJson, MockEntity.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void testMapToList_emptyOrWhiteSpaceStringJson_emptyList(String json) {
        List<MockEntity> deserializedEntityList = mapper.mapToList(json, MockEntity.class);
        assertThat(deserializedEntityList).isEmpty();

    }

    @Test
    @SuppressWarnings("all")
    void testMapToList_nullParameters_throwsException() {
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> mapper.mapToList(null, MockEntity.class));
        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> mapper.mapToList("", null));
        assertThat(ex1.getMessage()).isEqualTo("inputJson cannot be null");
        assertThat(ex2.getMessage()).isEqualTo("objectClass cannot be null");
    }

    static class MockEntity {

        @Nullable
        @JsonProperty("ID")
        Long id;

        @Nullable
        @JsonProperty("Value")
        String value;

        // default constructor is necessary for jackson to deserialize
        public MockEntity() {

        }

        public MockEntity(@Nullable Long id, @Nullable String value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{\n\t" + this.id + ",\n\t" + this.value + "\n}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MockEntity)) return false;
            MockEntity that = (MockEntity) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, value);
        }

    }
}