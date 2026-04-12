/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package unit.service.compounded;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.cimari.entity.compounded.HardwareId;
import io.github.eggy03.cimari.service.TerminalService;
import io.github.eggy03.cimari.service.compounded.HardwareIdService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

class HardwareIdServiceTest {

    private static HardwareId expectedHwid;
    private static String json;
    private HardwareIdService service;

    @BeforeAll
    static void setJson() {

        JsonObject hwidObject = new JsonObject();
        hwidObject.addProperty("HWIDRaw", "ABC123");
        hwidObject.addProperty("HWIDHash", "123XYZ");

        json = new GsonBuilder().serializeNulls().create().toJson(hwidObject);

        expectedHwid = HardwareId.builder()
                .rawHWID("ABC123")
                .hashHWID("123XYZ")
                .build();
    }

    @BeforeEach
    void setService() {
        service = new HardwareIdService();
    }

    @Test
    void test_getWithTimeout_success() {

        try (MockedStatic<TerminalService> mockedTerminal = mockStatic(TerminalService.class)) {
            mockedTerminal
                    .when(() -> TerminalService.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            Optional<HardwareId> response = service.get(5L);
            assertThat(response).isPresent();
            assertThat(response.get()).usingRecursiveComparison().isEqualTo(expectedHwid);
        }
    }

    @Test
    void test_getWithTimeout_invalidJson_throwsException() {

        try (MockedStatic<TerminalService> mockedTerminal = mockStatic(TerminalService.class)) {
            mockedTerminal
                    .when(() -> TerminalService.executeCommand(anyString(), anyLong()))
                    .thenReturn("invalid json");

            assertThrows(JsonSyntaxException.class, () -> service.get(5L));
        }
    }

    /*
     * This test ensures that the test JSON has keys matching all @SerializedName
     * (or raw field names if not annotated) declared in the entity class.
     *
     * The test fails if:
     * - any field is added or removed in the entity without updating the test JSON
     * - any @SerializedName value changes without updating the test JSON
     */
    @Test
    void test_entityFieldParity_withTestJson() {

        // get the serialized name for each field, in a set
        // store the field name in case no serialized names are found
        Field[] declaredClassFields = HardwareId.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonObject.class).keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
