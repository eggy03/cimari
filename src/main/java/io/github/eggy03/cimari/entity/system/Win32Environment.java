/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of the environment variables in a system running Windows.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Environment} WMI class.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-environment">Win32_Environment Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_Environment")
@NullMarked
public class Win32Environment {

    /**
     * Character string that specifies the name of a Windows-based environment variable.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Indicates whether the variable is a system variable.
     */
    @JsonProperty("SystemVariable")
    @Nullable
    Boolean systemVariable;
    /**
     * Placeholder variable of a Windows-based environment variable.
     * Information like the file system directory can change from computer to computer.
     * The operating system substitutes placeholders for these.
     */
    @JsonProperty("VariableValue")
    @Nullable
    String variableValue;

    public @Nullable Boolean isSystemVariable() {
        return systemVariable;
    }

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
