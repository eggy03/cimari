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
public class Win32Process {

    /**
     * Unique identifier of the process.
     */
    @JsonProperty("ProcessId")
    @Nullable
    Long processId;

    /**
     * Identifier of the session under which this process is running.
     */
    @JsonProperty("SessionId")
    @Nullable
    Long sessionId;

    /**
     * Name of the executable file responsible for this process.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * Short one-line description of the process.
     */
    @JsonProperty("Caption")
    @Nullable
    String caption;

    /**
     * Full description of the process.
     */
    @JsonProperty("Description")
    @Nullable
    String description;

    /**
     * Full path to the executable file of the process.
     */
    @JsonProperty("ExecutablePath")
    @Nullable
    String executablePath;

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
    Integer executionState;

    /**
     * Handle of the process (string representation of ProcessId).
     */
    @JsonProperty("Handle")
    @Nullable
    String handle;

    /**
     * Number of handles currently open by the process.
     */
    @JsonProperty("HandleCount")
    @Nullable
    Long handleCount;

    /**
     * Scheduling priority of the process.
     * <ul>
     *   <li>0 (lowest) to 31 (highest)</li>
     * </ul>
     */
    @JsonProperty("Priority")
    @Nullable
    Long priority;

    /**
     * Number of active threads in the process.
     */
    @JsonProperty("ThreadCount")
    @Nullable
    Long threadCount;

    /**
     * Time spent by the process in kernel mode (in ms).
     */
    @JsonProperty("KernelModeTime")
    @Nullable
    BigInteger kernelModeTime;

    /**
     * Time spent by the process in user mode (in ms).
     */
    @JsonProperty("UserModeTime")
    @Nullable
    BigInteger userModeTime;

    /**
     * Current working set size (in bytes) used by the process.
     */
    @JsonProperty("WorkingSetSize")
    @Nullable
    BigInteger workingSetSize;

    /**
     * Peak working set size (in KB) of the process.
     */
    @JsonProperty("PeakWorkingSetSize")
    @Nullable
    BigInteger peakWorkingSetSize;

    /**
     * Current number of private memory pages used by the process.
     */
    @JsonProperty("PrivatePageCount")
    @Nullable
    BigInteger privatePageCount;

    /**
     * Current amount of page file usage (kilobytes).
     */
    @JsonProperty("PageFileUsage")
    @Nullable
    Long pageFileUsage;

    /**
     * Peak page file usage (kilobytes).
     */
    @JsonProperty("PeakPageFileUsage")
    @Nullable
    Long peakPageFileUsage;

    /**
     * Current virtual address space used by the process (bytes).
     */
    @JsonProperty("VirtualSize")
    @Nullable
    BigInteger virtualSize;

    /**
     * Peak virtual address space used by the process (bytes).
     */
    @JsonProperty("PeakVirtualSize")
    @Nullable
    BigInteger peakVirtualSize;

    /**
     * Date/time when the process was created.
     */
    @JsonProperty("CreationDate")
    @Nullable
    String creationDate;

    /**
     * Date/time when the process was terminated (if available).
     */
    @JsonProperty("TerminationDate")
    @Nullable
    String terminationDate;

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
