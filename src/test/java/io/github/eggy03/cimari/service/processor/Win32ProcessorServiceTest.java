/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.processor;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.mapping.processor.Win32ProcessorMapper;
import io.github.eggy03.cimari.shell.query.Cimv2;
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
class Win32ProcessorServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32Processor expectedProcessor = Win32Processor.builder()
            .deviceId("CPU0")
            .name("Intel(R) Core(TM) i9-13900K")
            .numberOfCores(24)
            .numberOfEnabledCores(24)
            .threadCount(32)
            .numberOfLogicalProcessors(32)
            .manufacturer("GenuineIntel")
            .addressWidth(64)
            .l2CacheSize(2048)
            .l3CacheSize(36864)
            .maxClockSpeed(5300)
            .extClock(100)
            .socketDesignation("LGA1700")
            .version("Model 151 Stepping 2")
            .caption("Intel64 Family 6 Model 151 Stepping 2")
            .family(6)
            .stepping("2")
            .virtualizationFirmwareEnabled(true)
            .processorId("BFEBFBFF000B0671")
            .architecture(9)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32ProcessorMapper mapper;

    @InjectMocks
    private Win32ProcessorService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.singletonList(expectedProcessor));

        List<Win32Processor> response = service.get(5L);
        assertThat(response).contains(expectedProcessor); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_PROCESSOR, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32Processor.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_PROCESSOR, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32Processor.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32Processor> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_PROCESSOR, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32Processor.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
