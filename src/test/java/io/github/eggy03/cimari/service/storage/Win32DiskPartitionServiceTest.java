/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;


import io.github.eggy03.cimari.entity.storage.Win32DiskPartition;
import io.github.eggy03.cimari.mapping.storage.Win32DiskPartitionMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;

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
class Win32DiskPartitionServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32DiskPartition expectedSystemPartition = Win32DiskPartition.builder()
            .deviceId("Disk0\\Partition1")
            .name("System Reserved")
            .description("EFI System Partition")
            .blockSize(BigInteger.valueOf(512L))
            .numberOfBlocks(BigInteger.valueOf(131072L))
            .bootable(true)
            .primaryPartition(true)
            .bootPartition(true)
            .diskIndex(0L)
            .size(BigInteger.valueOf(67108864L))
            .type("EFI")
            .build();
    private final Win32DiskPartition expectedDataPartition = Win32DiskPartition.builder()
            .deviceId("Disk0\\Partition2")
            .name("Local Disk (C:)")
            .description("Primary OS Partition")
            .blockSize(BigInteger.valueOf(4096L))
            .numberOfBlocks(BigInteger.valueOf(244190000L))
            .bootable(false)
            .primaryPartition(true)
            .bootPartition(false)
            .diskIndex(0L)
            .size(BigInteger.valueOf(1000204886016L))
            .type("NTFS")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32DiskPartitionMapper mapper;

    @InjectMocks
    private Win32DiskPartitionService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedDataPartition, expectedSystemPartition));

        List<Win32DiskPartition> response = service.get(5L);
        assertThat(response).contains(expectedDataPartition, expectedSystemPartition); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_PARTITION, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32DiskPartition.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_PARTITION, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32DiskPartition.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32DiskPartition> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_DISK_PARTITION, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32DiskPartition.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
