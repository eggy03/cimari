/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;


import io.github.eggy03.cimari.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.cimari.mapping.network.MsftDnsClientServerAddressMapper;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
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
class MsftDnsClientServerAddressServiceTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final MsftDnsClientServerAddress expectedDns1 = new MsftDnsClientServerAddress.Builder()
            .interfaceIndex(1L)
            .interfaceAlias("Ethernet")
            .addressFamily(2) // IPv4
            .dnsServerAddresses(Arrays.asList("8.8.8.8", "4.4.4.4"))
            .build();

    private final MsftDnsClientServerAddress expectedDns2 = new MsftDnsClientServerAddress.Builder()
            .interfaceIndex(2L)
            .interfaceAlias("Wi-Fi")
            .addressFamily(23) // IPv6
            .dnsServerAddresses(Arrays.asList("2001:4860:4860::8888", "2001:4860:4860::8844"))
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private MsftDnsClientServerAddressMapper mapper;

    @InjectMocks
    private MsftDnsClientServerAddressService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedDns1, expectedDns2));

        List<MsftDnsClientServerAddress> response = service.get(5L);
        assertThat(response).contains(expectedDns1, expectedDns2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), MsftDnsClientServerAddress.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JacksonException.class);

        assertThrows(JacksonException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), MsftDnsClientServerAddress.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<MsftDnsClientServerAddress> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), MsftDnsClientServerAddress.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
