/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.service.user;

import io.github.eggy03.cimari.entity.user.User;
import org.jspecify.annotations.NonNull;

/**
 * Service class for fetching the current system user information.
 * <p>
 * This class retrieves the current user's details such as username,
 * home directory, and working directory using standard Java system properties.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * UserService userService = new UserService();
 * User currentUser = userService.getUser();
 * System.out.println(currentUser);
 * }</pre>
 *
 * @since 1.0.0
 */

public class UserService {

    /**
     * Retrieves the current system user information.
     *
     * @return a non-null {@link User} object containing the username,
     * user home directory, and current working directory.
     */
    public @NonNull User getUser() {

        return new User.Builder()
                .userName(System.getProperty("user.name"))
                .userHome(System.getProperty("user.home"))
                .userDirectory(System.getProperty("user.dir"))
                .build();
    }
}
