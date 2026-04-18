/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.storage;


import io.github.eggy03.cimari.entity.storage.Win32LogicalDisk;
import io.github.eggy03.cimari.mapping.storage.Win32LogicalDiskMapper;
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
class Win32LogicalDiskServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32LogicalDisk expectedSystemVolume = new Win32LogicalDisk.Builder()
            .deviceId("C:")
            .description("System Volume")
            .driveType(3L)
            .mediaType(12L)
            .fileSystem("NTFS")
            .size(BigInteger.valueOf(1000204886016L))
            .freeSpace(BigInteger.valueOf(532147200000L))
            .compressed(false)
            .supportsFileBasedCompression(true)
            .supportsDiskQuotas(false)
            .volumeName("Windows")
            .volumeSerialNumber("1A2B-3C4D")
            .build();
    private final Win32LogicalDisk expectedDataVolume = new Win32LogicalDisk.Builder()
            .deviceId("D:")
            .description("Data Volume")
            .driveType(3L)
            .mediaType(12L)
            .fileSystem("NTFS")
            .size(BigInteger.valueOf(2000409772032L))
            .freeSpace(BigInteger.valueOf(1240152000000L))
            .compressed(false)
            .supportsFileBasedCompression(true)
            .supportsDiskQuotas(false)
            .volumeName("Data")
            .volumeSerialNumber("5E6F-7G8H")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32LogicalDiskMapper mapper;

    @InjectMocks
    private Win32LogicalDiskService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedDataVolume, expectedSystemVolume));

        List<Win32LogicalDisk> response = service.get(5L);
        assertThat(response).contains(expectedDataVolume, expectedSystemVolume); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_LOGICAL_DISK, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32LogicalDisk.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_LOGICAL_DISK, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32LogicalDisk.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32LogicalDisk> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_LOGICAL_DISK, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32LogicalDisk.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
