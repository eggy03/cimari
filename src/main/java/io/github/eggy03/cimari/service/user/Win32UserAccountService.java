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
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service class for fetching information about User Accounts in a Windows System.
 * <p>
 * This class executes the {@link Cimv2#WIN32_USER_ACCOUNT} PowerShell command
 * and maps the resulting output into an unmodifiable {@link List} of {@link Win32UserAccount} objects.
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
public class Win32UserAccountService implements CommonServiceInterface<Win32UserAccount> {

    private final @NonNull TerminalService terminalService;
    private final @NonNull Win32UserAccountMapper mapper;

    /**
     * Creates {@link Win32UserAccountService} with default configuration
     */
    public Win32UserAccountService() {
        this(new TerminalService(), new Win32UserAccountMapper());
    }

    /**
     * Package Private constructor with injectable dependencies
     *
     * @param terminalService the {@link TerminalService} instance to use, must not be {@code null}
     * @param mapper          the mapper instance to use, must not be {@code null}
     * @since 1.0.0
     */
    Win32UserAccountService(TerminalService terminalService, Win32UserAccountMapper mapper) {
        this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    /**
     * Retrieves an unmodifiable {@link List} of {@link Win32UserAccount}
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an unmodifiable {@link List} of {@link Win32UserAccount} objects.
     * Returns a {@link Collections#emptyList()} if no user accounts are detected.
     * @since 1.0.0
     */
    @Override
    public @NonNull List<Win32UserAccount> get(long timeout) {
        TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_USER_ACCOUNT, timeout);
        return mapper.mapToList(result.getResult(), Win32UserAccount.class);
    }
}