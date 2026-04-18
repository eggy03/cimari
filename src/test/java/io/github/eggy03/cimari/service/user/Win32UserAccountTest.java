/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.user;


import io.github.eggy03.cimari.entity.user.Win32UserAccount;
import io.github.eggy03.cimari.mapping.user.Win32UserAccountMapper;
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
class Win32UserAccountTest {

    private final TerminalResult validTerminalResult = new TerminalResult("{}", "");
    private final TerminalResult invalidTerminalResult = new TerminalResult("invalid json", "");
    private final TerminalResult emptyTerminalResult = new TerminalResult("", "");

    private final Win32UserAccount expectedUser1 = new Win32UserAccount.Builder()
            .sid("S-1-5-21-1234567890-1001")
            .sidType(1)
            .accountType(512L)
            .caption("User1")
            .description("Local user account")
            .domain("WORKGROUP")
            .name("Egg-03")
            .disabled(false)
            .localAccount(true)
            .lockout(false)
            .passwordRequired(true)
            .passwordExpires(false)
            .passwordChangeable(true)
            .status("OK")
            .build();

    private final Win32UserAccount expectedUser2 = new Win32UserAccount.Builder()
            .sid("S-1-5-21-0987654321-1002")
            .sidType(1)
            .accountType(512L)
            .caption("User2")
            .description("Administrator account")
            .domain("WORKGROUP")
            .name("Admin")
            .disabled(false)
            .localAccount(true)
            .lockout(false)
            .passwordRequired(true)
            .passwordExpires(true)
            .passwordChangeable(true)
            .status("OK")
            .build();

    @Mock
    private TerminalService terminalService;

    @Mock
    private Win32UserAccountMapper mapper;

    @InjectMocks
    private Win32UserAccountService service;

    @Test
    void test_get_serviceReturnsMapperResult() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(validTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Arrays.asList(expectedUser1, expectedUser2));

        List<Win32UserAccount> response = service.get(5L);
        assertThat(response).contains(expectedUser1, expectedUser2); // Service should return mapper result unchanged

        verify(terminalService).executeQuery(Cimv2.WIN32_USER_ACCOUNT, 5L);
        verify(mapper).mapToList(validTerminalResult.getResult(), Win32UserAccount.class);
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

        verify(terminalService).executeQuery(Cimv2.WIN32_USER_ACCOUNT, 5L);
        verify(mapper).mapToList(invalidTerminalResult.getResult(), Win32UserAccount.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void test_get_serviceReturnsEmpty_whenMapperReturnsEmpty() {

        when(terminalService.executeQuery(any(Cimv2.class), anyLong()))
                .thenReturn(emptyTerminalResult);

        when(mapper.mapToList(anyString(), any()))
                .thenReturn(Collections.emptyList());

        List<Win32UserAccount> response = service.get(5L);
        assertThat(response).isEmpty();

        verify(terminalService).executeQuery(Cimv2.WIN32_USER_ACCOUNT, 5L);
        verify(mapper).mapToList(emptyTerminalResult.getResult(), Win32UserAccount.class);
        verifyNoMoreInteractions(terminalService);
        verifyNoMoreInteractions(mapper);
    }
}
