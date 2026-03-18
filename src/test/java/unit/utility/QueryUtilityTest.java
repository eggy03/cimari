package unit.utility;

import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.annotation.WmiClass;
import io.github.eggy03.ferrumx.windows.exception.AnnotationNotFoundException;
import io.github.eggy03.ferrumx.windows.query.utility.QueryUtility;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueryUtilityTest {

    @Test
    void getWmiClassNameFromWmiClassAnnotation_success() {
        String expectedString = "testClass";
        String actualString = QueryUtility.getClassNameFromWmiClassAnnotation(MockWmiAnnotatedClass.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getWmiClassNameFromWmiClassAnnotation_nonAnnotatedClass_throwsException() {

        assertThrows(
                AnnotationNotFoundException.class,
                () -> QueryUtility.getClassNameFromWmiClassAnnotation(MockNonWmiAnnotatedClass.class)
        );
    }

    @Test
    void getPropertiesFromSerializedNameAnnotation_withAnnotatedFields_success() {

        String expectedString = "field_one, field_two, field_three";
        String actualString = QueryUtility.getPropertiesFromSerializedNameAnnotation(MockWithAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getPropertiesFromSerializedNameAnnotation_withoutAnnotatedFields_success() {

        String expectedString = "fieldOne, fieldTwo, fieldThree";
        String actualString = QueryUtility.getPropertiesFromSerializedNameAnnotation(MockWithoutAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @Test
    void getPropertiesFromSerializedNameAnnotation_withAbstractClass_success_emptyString() {

        String actualString = QueryUtility.getPropertiesFromSerializedNameAnnotation(MockAbstractClass.class);
        assertThat(actualString).isNotNull().isEmpty();
    }

    @Test
    void getFromSerializedNames_withAnnotatedFields_inheritedPropertiesFromAnotherClass_success() {

        String expectedString = "field_four"; // inherited fields are not included
        String actualString = QueryUtility.getPropertiesFromSerializedNameAnnotation(ExtensionOfMockWithAnnotatedFields.class);

        assertThat(expectedString).isEqualTo(actualString);
    }

    @SuppressWarnings("unused")
    static class MockWithAnnotatedFields { // inner test class where fields are annotated with gson @SerializedName

        @SerializedName("field_one")
        String fieldOne;

        @SerializedName("field_two")
        String fieldTwo;

        @SerializedName("field_three")
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

        @SerializedName("field_four")
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
