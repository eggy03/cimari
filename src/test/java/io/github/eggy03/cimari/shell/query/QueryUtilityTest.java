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
        String actualString = QueryUtility.getClassNameFromWmiClass(MockWmiAnnotatedClass.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getWmiClassNameFromWmiClassAnnotation_nonAnnotatedClass_throwsException() {

        assertThrows(
                AnnotationNotFoundException.class,
                () -> QueryUtility.getClassNameFromWmiClass(MockNonWmiAnnotatedClass.class)
        );
    }

    @Test
    void getPropertiesFromJsonProperty_withAnnotatedMethods_success() {

        String expectedString = "method_one, method_three, method_two";
        String actualString = QueryUtility.getPropertiesFromJsonProperty(MockWithAnnotatedMethods.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getPropertiesFromJsonProperty_withoutAnnotatedMethods_returnsEmptyString() {

        String expectedString = "";
        String actualString = QueryUtility.getPropertiesFromJsonProperty(MockWithoutAnnotatedMethods.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getPropertiesFromJsonProperty_withAbstractClass_success_emptyString() {

        String actualString = QueryUtility.getPropertiesFromJsonProperty(MockEmptyClass.class);
        assertThat(actualString).isNotNull().isEmpty();
    }

    @Test
    void getFromJsonProperties_withAnnotatedMethods_inheritedPropertiesFromAnotherClass_success() {

        String expectedString = "method_four"; // inherited methods are not included
        String actualString = QueryUtility.getPropertiesFromJsonProperty(ExtensionOfMockWithAnnotatedMethods.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    @SuppressWarnings("all")
    void testBothMethodsForNullInputs_throwsException() {
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> QueryUtility.getClassNameFromWmiClass(null));
        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> QueryUtility.getPropertiesFromJsonProperty(null));
        assertThat(ex1.getMessage()).isEqualTo("tClass cannot be null");
        assertThat(ex2.getMessage()).isEqualTo("tClass cannot be null");
    }

    @SuppressWarnings("unused")
    abstract static class MockWithAnnotatedMethods { // inner test class where methods are annotated with Jackson's @JsonProperty

        @JsonProperty("method_one")
        public abstract String methodOne();

        @JsonProperty("method_two")
        public abstract String methodTwo();

        @JsonProperty("method_three")
        public abstract String methodThree();
    }

    @SuppressWarnings("unused")
    abstract static class MockWithoutAnnotatedMethods {

        public abstract String methodOne();

        public abstract String methodTwo();

        public abstract String methodThree();
    }

    abstract static class MockEmptyClass {

    }

    @SuppressWarnings("unused")
    abstract static class ExtensionOfMockWithAnnotatedMethods extends MockWithAnnotatedMethods {

        @JsonProperty("method_four")
        public abstract String methodFour();
    }

    @SuppressWarnings("unused")
    @WmiClass(className = "testClass")
    static class MockWmiAnnotatedClass {

    }

    @SuppressWarnings("unused")
    static class MockNonWmiAnnotatedClass {

    }
}
