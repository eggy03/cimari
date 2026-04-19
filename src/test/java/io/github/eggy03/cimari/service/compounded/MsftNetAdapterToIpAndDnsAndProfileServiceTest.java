/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.compounded;


import io.github.eggy03.cimari.entity.compounded.ImmutableMsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.cimari.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.cimari.mapping.compounded.MsftNetAdapterToIpAndDnsAndProfileMapper;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;

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
class MsftNetAdapterToIpAndDnsAndProfileServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final MsftNetAdapterToIpAndDnsAndProfile expectedObject
            = new ImmutableMsftNetAdapterToIpAndDnsAndProfile.Builder()
            .interfaceIndex(1L)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private MsftNetAdapterToIpAndDnsAndProfileMapper mapper;

    @InjectMocks
    private MsftNetAdapterToIpAndDnsAndProfileService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.singletonList(expectedObject));

        List<MsftNetAdapterToIpAndDnsAndProfile> response = service.get(5L);
        assertThat(response).contains(expectedObject); // Service should return mapper result unchanged

        verify(terminalService).executeScript(ScriptEnum.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), MsftNetAdapterToIpAndDnsAndProfile.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JacksonException.class);

        assertThrows(JacksonException.class, () -> service.get(5L));

        verify(terminalService).executeScript(ScriptEnum.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), MsftNetAdapterToIpAndDnsAndProfile.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeScript(any(), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<MsftNetAdapterToIpAndDnsAndProfile> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeScript(ScriptEnum.MSFT_NET_ADAPTER_TO_IP_AND_DNS_AND_PROFILE, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), MsftNetAdapterToIpAndDnsAndProfile.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}