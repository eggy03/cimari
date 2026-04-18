/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.processor;


import io.github.eggy03.cimari.entity.processor.Win32AssociatedProcessorMemory;
import io.github.eggy03.cimari.mapping.processor.Win32AssociatedProcessorMemoryMapper;
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
class Win32AssociatedProcessorMemoryTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32AssociatedProcessorMemory expectedAssoc1 = new Win32AssociatedProcessorMemory.Builder()
            .cacheMemoryDeviceId("CacheMemory0")
            .processorDeviceId("CPU0")
            .build();

    private final Win32AssociatedProcessorMemory expectedAssoc2 = new Win32AssociatedProcessorMemory.Builder()
            .cacheMemoryDeviceId("CacheMemory1")
            .processorDeviceId("CPU1")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32AssociatedProcessorMemoryMapper mapper;

    @InjectMocks
    private Win32AssociatedProcessorMemoryService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedAssoc1, expectedAssoc2));

        List<Win32AssociatedProcessorMemory> response = service.get(5L);
        assertThat(response).contains(expectedAssoc1, expectedAssoc2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_ASSOCIATED_PROCESSOR_MEMORY, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32AssociatedProcessorMemory.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_ASSOCIATED_PROCESSOR_MEMORY, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32AssociatedProcessorMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32AssociatedProcessorMemory> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_ASSOCIATED_PROCESSOR_MEMORY, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32AssociatedProcessorMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
