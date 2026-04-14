/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.compounded.Win32ProcessorToCacheMemory;
import io.github.eggy03.cimari.mapping.compounded.Win32ProcessorToCacheMemoryMapper;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class Win32ProcessorToCacheMemoryServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32ProcessorToCacheMemory expectedObject
            = Win32ProcessorToCacheMemory.builder()
            .deviceId("1L")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32ProcessorToCacheMemoryMapper mapper;

    @InjectMocks
    private Win32ProcessorToCacheMemoryService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.singletonList(expectedObject));

        List<Win32ProcessorToCacheMemory> response = service.get(5L);
        assertThat(response).contains(expectedObject); // Service should return mapper result unchanged

        verify(terminalService).executeScript(ScriptEnum.WIN32_PROCESSOR_TO_CACHE_MEMORY, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32ProcessorToCacheMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JsonSyntaxException.class);

        assertThrows(JsonSyntaxException.class, () -> service.get(5L));

        verify(terminalService).executeScript(ScriptEnum.WIN32_PROCESSOR_TO_CACHE_MEMORY, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32ProcessorToCacheMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32ProcessorToCacheMemory> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeScript(ScriptEnum.WIN32_PROCESSOR_TO_CACHE_MEMORY, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32ProcessorToCacheMemory.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
