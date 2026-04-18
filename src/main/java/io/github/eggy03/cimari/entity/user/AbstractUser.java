/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.user;

import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonSerialize;

/**
 * Immutable representation of a user on a system. The service which retrieves this info is platform-agnostic.
 * <p>
 * Fields capture basic user information such as username, home directory, and user directory.
 * </p>
 *
 * @see Win32UserAccount
 * @since 1.0.0
 */
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = User.class)
public abstract class AbstractUser {

    /**
     * Current username of the user behind this instance
     */
    @Nullable
    public abstract String userName();

    /**
     * Fetches the user's home directory. For more information on what the home directory
     * could mean, check out the definitions provided by your OS.
     */
    @Nullable
    public abstract String userHome();

    /**
     * Current working directory of the user. This usually points to the directory where this library code exists.
     */
    @Nullable
    public abstract String userDirectory();

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