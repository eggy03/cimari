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
    private final ObjectMapper objectMapper = mapper.objectMapper();

    @Test
    void testMapToObject_success() {

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
    void testMapToObject_invalidJson_throwsException() {

        String json = "invalid json";
        assertThrows(JacksonException.class, () -> mapper.mapToObject(json, MockEntity.class));

    }

    @Test
    void testMapToObject_validJson_jsonMismatchSchema_returnsObjectWithNullableFields() {

        String json = "{}";
        Optional<MockEntity> deserializedMockEntity = mapper.mapToObject(json, MockEntity.class);

        assertThat(deserializedMockEntity).isPresent();
        assertThat(deserializedMockEntity.get()).isNotNull();
        assertThat(deserializedMockEntity.get().id).isNull();
    }

    @Test
    void testMapToList_validJson_butJsonIsAnArray_throwsException() {
        String json = "[{}]";
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToObject(json, MockEntity.class));
    }

    @Test
    void testMapToObject_emptyJson_optionalNotPresent() {
        String json = "";
        Optional<MockEntity> deserializedMockEntity = mapper.mapToObject(json, MockEntity.class);
        assertThat(deserializedMockEntity).isNotPresent();
    }

    @Test
    void testMapToObject_nullParameters_throwsException() {
        assertThrows(NullPointerException.class, () -> mapper.mapToObject(null, MockEntity.class));
        assertThrows(NullPointerException.class, () -> mapper.mapToObject("", null));
    }

    @Test
    void testMapToList_success() {

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
    void testMapToList_whenSingleObject_returnsSingletonList() {

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
    void testMapToList_invalidJson_throwsException() {
        String json = "invalid json";
        assertThrows(JacksonException.class, () -> mapper.mapToList(json, MockEntity.class));
    }

    @Test
    void testMapToList_validJson_jsonMismatchSchema_returnsASingletonListWithNullFieldObject() {
        String json = "[{}]";
        List<MockEntity> deserializedEntityList = mapper.mapToList(json, MockEntity.class);

        assertThat(deserializedEntityList).hasSize(1);
        assertThat(deserializedEntityList.get(0)).isNotNull();
        assertThat(deserializedEntityList.get(0).id).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void testMapToList_emptyOrWhiteSpaceOrLiteralNullStringJson_emptyList(String json) {
        List<MockEntity> deserializedEntityList = mapper.mapToList(json, MockEntity.class);
        assertThat(deserializedEntityList).isEmpty();

    }

    @Test
    void testMapToList_nullArrayString_returnsListWithNullElement() {
        String json = "[null]";
        List<MockEntity> deserializedEntityList = mapper.mapToList(json, MockEntity.class);

        assertThat(deserializedEntityList).hasSize(1);
        assertThat(deserializedEntityList.get(0)).isNull();
    }

    @Test
    void testMapToList_nullParameters_throwsException() {
        assertThrows(NullPointerException.class, () -> mapper.mapToList(null, MockEntity.class));
        assertThrows(NullPointerException.class, () -> mapper.mapToList("", null));
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