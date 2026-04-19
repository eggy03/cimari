/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.math.BigInteger;

/**
 * Immutable representation of a process in a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Process} WMI class.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-process">Win32_Process Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_Process")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32Process.class)
@JsonDeserialize(as = ImmutableWin32Process.class)
public abstract class Win32Process {

    /**
     * Unique identifier of the process.
     */
    @JsonProperty("ProcessId")
    @Nullable
    public abstract Long processId();

    /**
     * Identifier of the session under which this process is running.
     */
    @JsonProperty("SessionId")
    @Nullable
    public abstract Long sessionId();

    /**
     * Name of the executable file responsible for this process.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Short one-line description of the process.
     */
    @JsonProperty("Caption")
    @Nullable
    public abstract String caption();

    /**
     * Full description of the process.
     */
    @JsonProperty("Description")
    @Nullable
    public abstract String description();

    /**
     * Full path to the executable file of the process.
     */
    @JsonProperty("ExecutablePath")
    @Nullable
    public abstract String executablePath();

    /**
     * Current execution state of the process.
     * <ul>
     *   <li>0 — Unknown</li>
     *   <li>1 — Other</li>
     *   <li>2 — Ready</li>
     *   <li>3 — Running</li>
     *   <li>4 — Blocked</li>
     *   <li>5 — Suspended Blocked</li>
     *   <li>6 — Suspended Ready</li>
     *   <li>7 — Terminated</li>
     *   <li>8 — Stopped</li>
     *   <li>9 — Growing</li>
     * </ul>
     */
    @JsonProperty("ExecutionState")
    @Nullable
    public abstract Integer executionState();

    /**
     * Handle of the process (string representation of ProcessId).
     */
    @JsonProperty("Handle")
    @Nullable
    public abstract String handle();

    /**
     * Number of handles currently open by the process.
     */
    @JsonProperty("HandleCount")
    @Nullable
    public abstract Long handleCount();

    /**
     * Scheduling priority of the process.
     * <ul>
     *   <li>0 (lowest) to 31 (highest)</li>
     * </ul>
     */
    @JsonProperty("Priority")
    @Nullable
    public abstract Long priority();

    /**
     * Number of active threads in the process.
     */
    @JsonProperty("ThreadCount")
    @Nullable
    public abstract Long threadCount();

    /**
     * Time spent by the process in kernel mode (in ms).
     */
    @JsonProperty("KernelModeTime")
    @Nullable
    public abstract BigInteger kernelModeTime();

    /**
     * Time spent by the process in user mode (in ms).
     */
    @JsonProperty("UserModeTime")
    @Nullable
    public abstract BigInteger userModeTime();

    /**
     * Current working set size (in bytes) used by the process.
     */
    @JsonProperty("WorkingSetSize")
    @Nullable
    public abstract BigInteger workingSetSize();

    /**
     * Peak working set size (in KB) of the process.
     */
    @JsonProperty("PeakWorkingSetSize")
    @Nullable
    public abstract BigInteger peakWorkingSetSize();

    /**
     * Current number of private memory pages used by the process.
     */
    @JsonProperty("PrivatePageCount")
    @Nullable
    public abstract BigInteger privatePageCount();

    /**
     * Current amount of page file usage (kilobytes).
     */
    @JsonProperty("PageFileUsage")
    @Nullable
    public abstract Long pageFileUsage();

    /**
     * Peak page file usage (kilobytes).
     */
    @JsonProperty("PeakPageFileUsage")
    @Nullable
    public abstract Long peakPageFileUsage();

    /**
     * Current virtual address space used by the process (bytes).
     */
    @JsonProperty("VirtualSize")
    @Nullable
    public abstract BigInteger virtualSize();

    /**
     * Peak virtual address space used by the process (bytes).
     */
    @JsonProperty("PeakVirtualSize")
    @Nullable
    public abstract BigInteger peakVirtualSize();

    /**
     * Date/time when the process was created.
     */
    @JsonProperty("CreationDate")
    @Nullable
    public abstract String creationDate();

    /**
     * Date/time when the process was terminated (if available).
     */
    @JsonProperty("TerminationDate")
    @Nullable
    public abstract String terminationDate();

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    public String toJson() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }
}
