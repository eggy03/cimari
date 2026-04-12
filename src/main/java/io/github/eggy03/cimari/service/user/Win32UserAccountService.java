/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.user;

import io.github.eggy03.cimari.entity.user.Win32UserAccount;
import io.github.eggy03.cimari.mapping.user.Win32UserAccountMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.utility.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Service class for fetching information about User Accounts in a Windows System.
 * <p>
 * This class executes the {@link Cimv2#WIN32_USER_ACCOUNT} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32UserAccount} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * Win32UserAccountService service = new Win32UserAccountService();
 * List<Win32UserAccount> ua = service.get(10);
 * }</pre>
 *
 * @since 1.0.0
 */
@Slf4j
public class Win32UserAccountService implements CommonServiceInterface<Win32UserAccount> {

    /**
     * Retrieves an immutable list of user accounts
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32UserAccount} objects representing the user accounts.
     * Returns an empty list if no user accounts are detected.
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Win32UserAccount> get(long timeout) {
        String command = Cimv2.WIN32_USER_ACCOUNT.getQuery();
        String response = TerminalService.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32UserAccountMapper().mapToList(response, Win32UserAccount.class);
    }
}