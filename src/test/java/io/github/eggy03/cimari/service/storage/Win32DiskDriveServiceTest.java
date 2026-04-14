/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.storage.Win32DiskDrive;
import io.github.eggy03.cimari.mapping.storage.Win32DiskDriveMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Win32DiskDriveServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32DiskDrive expectedDiskDrive1 = Win32DiskDrive.builder()
            .deviceId("\\\\.\\PHYSICALDRIVE0")
            .caption("Samsung SSD 970 EVO")
            .model("MZ-V7E1T0")
            .size(BigInteger.valueOf(1000204886016L))
            .firmwareRevision("2B2QEXM7")
            .serialNumber("S4EVNX0M123456")
            .partitions(3L)
            .status("OK")
            .interfaceType("NVMe")
            .pnpDeviceId("PCI\\VEN_144D&DEV_A808&SUBSYS_0A0E144D&REV_01\\4&1A2B3C4D&0&000000")
            .capabilities(Arrays.asList(3, 4))
            .capabilityDescriptions(Arrays.asList("desc1", "desc2"))
            .build();
    private final Win32DiskDrive expectedDiskDrive2 = Win32DiskDrive.builder()
            .deviceId("\\\\.\\PHYSICALDRIVE1")
            .caption("Seagate BarraCuda 2TB")
            .model("ST2000DM008")
            .size(BigInteger.valueOf(2000398934016L))
            .firmwareRevision("CC26")
            .serialNumber("ZFL123ABC456")
            .partitions(2L)
            .status("OK")
            .interfaceType("SATA")
            .pnpDeviceId("PCI\\VEN_8086&DEV_A102&SUBSYS_85C41043&REV_31\\3&11583659&0&FA")
            .capabilities(Arrays.asList(3, 4))
            .capabilityDescriptions(Arrays.asList("desc1", "desc2"))
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32DiskDriveMapper mapper;

    @InjectMocks
    private Win32DiskDriveService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedDiskDrive1, expectedDiskDrive2));

        List<Win32DiskDrive> response = service.get(5L);
        assertThat(response).contains(expectedDiskDrive1, expectedDiskDrive2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_DRIVE, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32DiskDrive.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JsonSyntaxException.class);

        assertThrows(JsonSyntaxException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_DRIVE, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32DiskDrive.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32DiskDrive> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_DRIVE, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32DiskDrive.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}