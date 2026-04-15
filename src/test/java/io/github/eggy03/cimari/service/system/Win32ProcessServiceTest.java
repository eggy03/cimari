/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.system;


import io.github.eggy03.cimari.entity.system.Win32Process;
import io.github.eggy03.cimari.mapping.system.Win32ProcessMapper;
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
class Win32ProcessServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32Process expectedProcess1 = Win32Process.builder()
            .processId(1234L)
            .sessionId(1L)
            .name("explorer.exe")
            .caption("Windows Explorer")
            .description("Manages the Windows shell")
            .executablePath("C:\\Windows\\explorer.exe")
            .executionState(1)
            .handle("0xABC")
            .handleCount(500L)
            .priority(8L)
            .threadCount(35L)
            .kernelModeTime(new BigInteger("120000000"))
            .userModeTime(new BigInteger("80000000"))
            .workingSetSize(new BigInteger("52428800"))
            .peakWorkingSetSize(new BigInteger("67108864"))
            .privatePageCount(new BigInteger("33554432"))
            .pageFileUsage(30000L)
            .peakPageFileUsage(40000L)
            .virtualSize(new BigInteger("268435456"))
            .peakVirtualSize(new BigInteger("536870912"))
            .creationDate("20251103101530.000000+330")
            .terminationDate(null)
            .build();

    private final Win32Process expectedProcess2 = Win32Process.builder()
            .processId(5678L)
            .sessionId(1L)
            .name("svchost.exe")
            .caption("Service Host")
            .description("Hosts Windows services")
            .executablePath("C:\\Windows\\System32\\svchost.exe")
            .executionState(1)
            .handle("0xDEF")
            .handleCount(200L)
            .priority(8L)
            .threadCount(10L)
            .kernelModeTime(new BigInteger("60000000"))
            .userModeTime(new BigInteger("40000000"))
            .workingSetSize(new BigInteger("26214400"))
            .peakWorkingSetSize(new BigInteger("31457280"))
            .privatePageCount(new BigInteger("16777216"))
            .pageFileUsage(15000L)
            .peakPageFileUsage(20000L)
            .virtualSize(new BigInteger("134217728"))
            .peakVirtualSize(new BigInteger("268435456"))
            .creationDate("20251103102000.000000+330")
            .terminationDate(null)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32ProcessMapper mapper;

    @InjectMocks
    private Win32ProcessService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedProcess1, expectedProcess2));

        List<Win32Process> response = service.get(5L);
        assertThat(response).contains(expectedProcess1, expectedProcess2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_PROCESS, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32Process.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_PROCESS, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32Process.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32Process> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_PROCESS, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32Process.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
