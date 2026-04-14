/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.storage.Win32LogicalDiskToPartition;
import io.github.eggy03.cimari.mapping.storage.Win32LogicalDiskToPartitionMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class Win32LogicalDiskToPartitionServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32LogicalDiskToPartition expectedSystemLogicalDiskPartition = Win32LogicalDiskToPartition.builder()
            .diskPartitionDeviceId("Disk #0 Partition #1")
            .logicalDiskDeviceId("C:")
            .build();
    private final Win32LogicalDiskToPartition expectedDataLogicalDiskPartition = Win32LogicalDiskToPartition.builder()
            .diskPartitionDeviceId("Disk #0 Partition #2")
            .logicalDiskDeviceId("D:")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32LogicalDiskToPartitionMapper mapper;

    @InjectMocks
    private Win32LogicalDiskToPartitionService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedDataLogicalDiskPartition, expectedSystemLogicalDiskPartition));

        List<Win32LogicalDiskToPartition> response = service.get(5L);
        assertThat(response).contains(expectedDataLogicalDiskPartition, expectedSystemLogicalDiskPartition); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_LOGICAL_DISK_TO_PARTITION, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32LogicalDiskToPartition.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_LOGICAL_DISK_TO_PARTITION, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32LogicalDiskToPartition.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32LogicalDiskToPartition> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_LOGICAL_DISK_TO_PARTITION, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32LogicalDiskToPartition.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
