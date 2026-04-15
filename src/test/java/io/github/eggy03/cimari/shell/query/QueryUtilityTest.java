/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2026 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.shell.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.WmiClass;
import io.github.eggy03.cimari.exception.AnnotationNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueryUtilityTest {

    @Test
    void getWmiClassNameFromWmiClassAnnotation_success() {
        String expectedString = "testClass";
        String actualString = QueryUtility.getPropertiesFromWmiClass(MockWmiAnnotatedClass.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getWmiClassNameFromWmiClassAnnotation_nonAnnotatedClass_throwsException() {

        assertThrows(
                AnnotationNotFoundException.class,
                () -> QueryUtility.getPropertiesFromWmiClass(MockNonWmiAnnotatedClass.class)
        );
    }

    @Test
    void getPropertiesFromJsonProperty_withAnnotatedFields_success() {

        String expectedString = "field_one, field_three, field_two";
        String actualString = QueryUtility.getPropertiesFromJsonProperty(MockWithAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getPropertiesFromJsonProperty_withoutAnnotatedFields_success() {

        String expectedString = "fieldOne, fieldThree, fieldTwo";
        String actualString = QueryUtility.getPropertiesFromJsonProperty(MockWithoutAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getPropertiesFromJsonProperty_withAbstractClass_success_emptyString() {

        String actualString = QueryUtility.getPropertiesFromJsonProperty(MockAbstractClass.class);
        assertThat(actualString).isNotNull().isEmpty();
    }

    @Test
    void getFromJsonProperties_withAnnotatedFields_inheritedPropertiesFromAnotherClass_success() {

        String expectedString = "field_four"; // inherited fields are not included
        String actualString = QueryUtility.getPropertiesFromJsonProperty(ExtensionOfMockWithAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @SuppressWarnings("unused")
    static class MockWithAnnotatedFields { // inner test class where fields are annotated with gson @JsonProperty

        @JsonProperty("field_one")
        String fieldOne;

        @JsonProperty("field_two")
        String fieldTwo;

        @JsonProperty("field_three")
        String fieldThree;
    }

    @SuppressWarnings("unused")
    static class MockWithoutAnnotatedFields {

        String fieldOne;
        String fieldTwo;
        String fieldThree;
    }

    static class MockAbstractClass {

    }

    @SuppressWarnings("unused")
    static class ExtensionOfMockWithAnnotatedFields extends MockWithAnnotatedFields {

        @JsonProperty("field_four")
        String fieldFour;
    }

    @SuppressWarnings("unused")
    @WmiClass(className = "testClass")
    static class MockWmiAnnotatedClass {

    }

    @SuppressWarnings("unused")
    static class MockNonWmiAnnotatedClass {

    }
}
