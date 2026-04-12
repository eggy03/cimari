/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package unit.service.compounded;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.cimari.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.cimari.service.TerminalService;
import io.github.eggy03.cimari.service.compounded.MsftNetAdapterToIpAndDnsAndProfileService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

class MsftNetAdapterToIpAndDnsAndProfileServiceTest {

    private static MsftNetAdapterToIpAndDnsAndProfile expectedObject;
    private static String json;
    private MsftNetAdapterToIpAndDnsAndProfileService service;

    @BeforeAll
    static void setJson() {

        JsonArray array = new JsonArray();

        JsonObject object = new JsonObject();
        object.addProperty("InterfaceIndex", 1L);
        object.add("NetworkAdapter", null);
        object.add("IPAddresses", null);
        object.add("DNSServers", null);
        object.add("Profile", null);

        array.add(object);

        json = new GsonBuilder().serializeNulls().create().toJson(array);

        expectedObject = MsftNetAdapterToIpAndDnsAndProfile.builder()
                .interfaceIndex(1L)
                .build();
    }

    @BeforeEach
    void setService() {
        service = new MsftNetAdapterToIpAndDnsAndProfileService();
    }

    @Test
    void test_getWithTimeout_success() {

        try (MockedStatic<TerminalService> mockedTerminal = mockStatic(TerminalService.class)) {
            mockedTerminal
                    .when(() -> TerminalService.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<MsftNetAdapterToIpAndDnsAndProfile> objectList = service.get(5L);
            assertThat(objectList).hasSize(1);
            assertThat(objectList.get(0)).usingRecursiveComparison().isEqualTo(expectedObject);
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
        Field[] declaredClassFields = MsftNetAdapterToIpAndDnsAndProfile.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonArray.class).get(0).getAsJsonObject().keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
