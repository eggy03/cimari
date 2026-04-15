/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.processor;


import io.github.eggy03.cimari.entity.processor.Win32CacheMemory;
import io.github.eggy03.cimari.mapping.processor.Win32CacheMemoryMapper;
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
class Win32CacheMemoryServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32CacheMemory expectedL1Cache = Win32CacheMemory.builder()
            .deviceId("CPU0_L1")
            .purpose("Instruction")
            .cacheType(3)
            .level(3)
            .installedSize(256L)
            .associativity(5)
            .location(0)
            .errorCorrectType(5)
            .availability(3)
            .status("OK")
            .statusInfo(3)
            .build();
    private final Win32CacheMemory expectedL2Cache = Win32CacheMemory.builder()
            .deviceId("CPU0_L2")
            .purpose("Unified")
            .cacheType(5)
            .level(4)
            .installedSize(2048L)
            .associativity(7)
            .location(0)
            .errorCorrectType(5)
            .availability(3)
            .status("OK")
            .statusInfo(3)
            .build();
    private final Win32CacheMemory expectedL3Cache = Win32CacheMemory.builder()
            .deviceId("CPU0_L3")
            .purpose("Unified")
            .cacheType(5)
            .level(5)
            .installedSize(16384L)
            .associativity(8)
            .location(0)
            .errorCorrectType(5)
            .availability(3)
            .status("OK")
            .statusInfo(3)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32CacheMemoryMapper mapper;

    @InjectMocks
    private Win32CacheMemoryService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedL1Cache, expectedL2Cache, expectedL3Cache));

        List<Win32CacheMemory> response = service.get(5L);
        assertThat(response).contains(expectedL1Cache, expectedL2Cache, expectedL3Cache); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_CACHE_MEMORY, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32CacheMemory.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_CACHE_MEMORY, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32CacheMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32CacheMemory> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_CACHE_MEMORY, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32CacheMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}