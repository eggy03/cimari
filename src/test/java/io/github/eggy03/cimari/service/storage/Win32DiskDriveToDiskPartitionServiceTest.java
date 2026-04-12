/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.cimari.entity.storage.Win32DiskDriveToDiskPartition;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

class Win32DiskDriveToDiskPartitionServiceTest {

    private static Win32DiskDriveToDiskPartition expectedMapping1;
    private static Win32DiskDriveToDiskPartition expectedMapping2;
    private static String json;
    private Win32DiskDriveToDiskPartitionService service;

    @BeforeAll
    static void setDiskDriveToPartitionMappings() {
        expectedMapping1 = Win32DiskDriveToDiskPartition.builder()
                .diskDriveDeviceId("PHYSICALDRIVE0")
                .diskPartitionDeviceId("Disk #0 Partition #1")
                .build();

        expectedMapping2 = Win32DiskDriveToDiskPartition.builder()
                .diskDriveDeviceId("PHYSICALDRIVE1")
                .diskPartitionDeviceId("Disk #1 Partition #1")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject mapping1 = new JsonObject();
        mapping1.addProperty("DiskDriveDeviceID", "PHYSICALDRIVE0");
        mapping1.addProperty("DiskPartitionDeviceID", "Disk #0 Partition #1");

        JsonObject mapping2 = new JsonObject();
        mapping2.addProperty("DiskDriveDeviceID", "PHYSICALDRIVE1");
        mapping2.addProperty("DiskPartitionDeviceID", "Disk #1 Partition #1");

        JsonArray array = new JsonArray();
        array.add(mapping1);
        array.add(mapping2);

        json = new GsonBuilder().serializeNulls().create().toJson(array);
    }


    @BeforeEach
    void setService() {
        service = new Win32DiskDriveToDiskPartitionService();
    }

    @Test
    void test_getWithTimeout_success() {

        try (MockedStatic<TerminalService> mockedTerminal = mockStatic(TerminalService.class)) {
            mockedTerminal
                    .when(() -> TerminalService.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<Win32DiskDriveToDiskPartition> associationList = service.get(5L);
            assertEquals(2, associationList.size());

            assertThat(associationList.get(0)).usingRecursiveComparison().isEqualTo(expectedMapping1);
            assertThat(associationList.get(1)).usingRecursiveComparison().isEqualTo(expectedMapping2);
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
        Field[] declaredClassFields = Win32DiskDriveToDiskPartition.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonArray.class)
                .get(0).getAsJsonObject().keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
