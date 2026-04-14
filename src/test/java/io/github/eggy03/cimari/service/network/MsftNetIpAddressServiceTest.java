/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.network;

import com.google.gson.JsonSyntaxException;
import io.github.eggy03.cimari.entity.network.MsftNetIpAddress;
import io.github.eggy03.cimari.mapping.network.MsftNetIpAddressMapper;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class MsftNetIpAddressServiceTest {

    static MsftNetIpAddress.Datetime lifetime = MsftNetIpAddress.Datetime.builder()
            .days(9999L)
            .hours(0L)
            .minutes(0L)
            .seconds(0L)
            .build();
    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");
    private final MsftNetIpAddress expectedIPv4Address = MsftNetIpAddress.builder()
            .interfaceIndex(1L)
            .interfaceAlias("Ethernet")
            .addressFamily(2L) // IPv4
            .ipAddress("192.168.1.10")
            .ipv4Address("192.168.1.10")
            .ipv6Address(null)
            .type(1)
            .prefixOrigin(1L)
            .suffixOrigin(2L)
            .prefixLength(24L)
            .preferredLifetime(lifetime)
            .validLifeTime(lifetime)
            .build();

    private final MsftNetIpAddress expectedIPv6Address = MsftNetIpAddress.builder()
            .interfaceIndex(2L)
            .interfaceAlias("Wi-Fi")
            .addressFamily(23L) // IPv6
            .ipAddress("fe80::1a2b:3c4d:5e6f:7a8b")
            .ipv4Address(null)
            .ipv6Address("fe80::1a2b:3c4d:5e6f:7a8b")
            .type(2)
            .prefixOrigin(3L)
            .suffixOrigin(3L)
            .prefixLength(64L)
            .preferredLifetime(lifetime)
            .validLifeTime(lifetime)
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private MsftNetIpAddressMapper mapper;

    @InjectMocks
    private MsftNetIpAddressService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedIPv4Address, expectedIPv6Address));

        List<MsftNetIpAddress> response = service.get(5L);
        assertThat(response).contains(expectedIPv4Address, expectedIPv6Address); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_IP_ADDRESS, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), MsftNetIpAddress.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_mapperThrows_servicePropagatesException() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(invalidTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenThrow(JsonSyntaxException.class);

        assertThrows(JsonSyntaxException.class, () -> service.get(5L));

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_IP_ADDRESS, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), MsftNetIpAddress.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(StandardCimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<MsftNetIpAddress> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(StandardCimv2.MSFT_NET_IP_ADDRESS, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), MsftNetIpAddress.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
