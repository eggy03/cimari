/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.user;

import io.github.eggy03.cimari.annotation.DeepImmutable;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of a user on a system. The service which retrieves this info is platform-agnostic.
 * <p>
 * Fields capture basic user information such as username, home directory, and user directory.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new User
 * User user = User.builder()
 *     .userName("john_doe")
 *     .userHome("C:\\Users\\john_doe")
 *     .userDirectory("C:\\Users\\john_doe\\Documents")
 *     .build();
 *
 * // Create a modified copy using the builder
 * User updatedUser = user.toBuilder()
 *     .userName("jane_doe")
 *     .build();
 *
 * }</pre>
 *
 *
 * @see Win32UserAccount
 * @since 1.0.0
 */

@Value
@Builder(toBuilder = true)
@DeepImmutable
public class User {

    /**
     * Current username of the user behind this instance
     */
    @Nullable
    String userName;

    /**
     * Fetches the user's home directory. For more information on what the home directory
     * could mean, check out the definitions provided by your OS.
     */
    @Nullable
    String userHome;

    /**
     * Current working directory of the user. This usually points to the directory where this library code exists.
     */
    @Nullable
    String userDirectory;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }
}