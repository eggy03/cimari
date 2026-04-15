/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;


import io.github.eggy03.cimari.entity.compounded.HardwareId;
import io.github.eggy03.cimari.mapping.compounded.HardwareIdMapper;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HardwareIdServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    /**
     * Expected domain object returned by the mapper.
     */
    private final HardwareId deserializedObject = HardwareId.builder()
            .rawHWID("ABC123")
            .hashHWID("123XYZ")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private HardwareIdMapper mapper;

    @InjectMocks
    private HardwareIdService service;


    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToObject(anyString(), any()))
                .thenReturn(Optional.of(deserializedObject));

        Optional<HardwareId> response = service.get(5L);
        assertThat(response).contains(deserializedObject); // Service should return mapper result unchanged

        verify(terminalService).executeScript(ScriptEnum.HWID, 5L);
        verify(mapper).mapToObject(validTerminalResult.getResult(), HardwareId.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToObject(anyString(), any()))
                .thenThrow(JacksonException.class);

        assertThrows(JacksonException.class, () -> service.get(5L));

        verify(terminalService).executeScript(ScriptEnum.HWID, 5L);
        verify(mapper).mapToObject(invalidTerminalResult.getResult(), HardwareId.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToObject(anyString(), any()))
                .thenReturn(Optional.empty());

        Optional<HardwareId> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeScript(ScriptEnum.HWID, 5L);
        verify(mapper).mapToObject(emptyTerminalResult.getResult(), HardwareId.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}