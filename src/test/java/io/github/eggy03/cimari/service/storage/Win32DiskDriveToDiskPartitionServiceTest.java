/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;


import io.github.eggy03.cimari.entity.storage.ImmutableWin32DiskDriveToDiskPartition;
import io.github.eggy03.cimari.entity.storage.Win32DiskDriveToDiskPartition;
import io.github.eggy03.cimari.mapping.storage.Win32DiskDriveToDiskPartitionMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;

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
class Win32DiskDriveToDiskPartitionServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32DiskDriveToDiskPartition expectedMapping1 = new ImmutableWin32DiskDriveToDiskPartition.Builder()
            .diskDriveDeviceId("PHYSICALDRIVE0")
            .diskPartitionDeviceId("Disk #0 Partition #1")
            .build();
    private final Win32DiskDriveToDiskPartition expectedMapping2 = new ImmutableWin32DiskDriveToDiskPartition.Builder()
            .diskDriveDeviceId("PHYSICALDRIVE1")
            .diskPartitionDeviceId("Disk #1 Partition #1")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32DiskDriveToDiskPartitionMapper mapper;

    @InjectMocks
    private Win32DiskDriveToDiskPartitionService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedMapping1, expectedMapping2));

        List<Win32DiskDriveToDiskPartition> response = service.get(5L);
        assertThat(response).contains(expectedMapping1, expectedMapping2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_DRIVE_TO_DISK_PARTITION, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32DiskDriveToDiskPartition.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JacksonException.class);

        assertThrows(JacksonException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_DRIVE_TO_DISK_PARTITION, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32DiskDriveToDiskPartition.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32DiskDriveToDiskPartition> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_DRIVE_TO_DISK_PARTITION, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32DiskDriveToDiskPartition.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

}
