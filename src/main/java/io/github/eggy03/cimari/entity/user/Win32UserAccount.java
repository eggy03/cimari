/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of a User Account in system running Windows.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_UserAccount} WMI class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32UserAccount systemAccount = Win32UserAccount.builder()
 *     .sid("S-1-5-21-0987654321-1002")
 *     .sidType(1)
 *     .accountType(512)
 *     .caption("User2")
 *     .description("Administrator account")
 *     .domain("WORKGROUP")
 *     .name("Admin")
 *     .build();
 *
 * // Create a modified copy
 * Win32UserAccount updated = systemAccount.toBuilder()
 *     .name("Admin-PC")
 *     .build();
 *
 * }</pre>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-useraccount">Win32_UserAccount Documentation</a>
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "Win32_UserAccount")
public class Win32UserAccount {

    /**
     * Security identifier (SID) for this account.
     * A SID is a string value of variable length that is used to identify a trustee.
     * Each account has a unique SID that an authority, such as a Windows domain, issues.
     * The SID is stored in the security database.
     * When a user logs on, the system retrieves the user SID from the database,
     * places the SID in the user access token,
     * and then uses the SID in the user access token to identify the user
     * in all subsequent interactions with Windows security.
     * Each SID is a unique identifier for a user or group, and a different user or group cannot have the same SID.
     */
    @JsonProperty("SID")
    @Nullable
    String sid;

    /**
     * Type of SID.
     * <ul>
     *   <li>1 — User</li>
     *   <li>2 — Group</li>
     *   <li>3 — Domain</li>
     *   <li>4 — Alias</li>
     *   <li>5 — WellKnownGroup</li>
     *   <li>6 — DeletedAccount</li>
     *   <li>7 — Invalid</li>
     *   <li>8 — Unknown</li>
     *   <li>9 — Computer</li>
     * </ul>
     */
    @JsonProperty("SIDType")
    @Nullable
    Integer sidType;

    /**
     * Type of user account.
     * <ul>
     *   <li>256 — Temporary duplicate account</li>
     *   <li>512 — Normal account</li>
     *   <li>2048 — Interdomain trust account</li>
     *   <li>4096 — Workstation trust account</li>
     *   <li>8192 — Server trust account</li>
     * </ul>
     */
    @JsonProperty("AccountType")
    @Nullable
    Long accountType;

    /**
     * Caption of the user account (domain/username).
     */
    @JsonProperty("Caption")
    @Nullable
    String caption;

    /**
     * Description of the user account.
     */
    @JsonProperty("Description")
    @Nullable
    String description;

    /**
     * Domain to which the user account belongs.
     */
    @JsonProperty("Domain")
    @Nullable
    String domain;

    /**
     * Name of the user account.
     */
    @JsonProperty("Name")
    @Nullable
    String name;

    /**
     * True if the account is disabled.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("Disabled")
    @Nullable
    Boolean disabled;
    /**
     * True if this is a local account.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("LocalAccount")
    @Nullable
    Boolean localAccount;
    /**
     * True if the account is locked out.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("Lockout")
    @Nullable
    Boolean lockout;
    /**
     * True if a password is required.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("PasswordRequired")
    @Nullable
    Boolean passwordRequired;
    /**
     * True if the password expires.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("PasswordExpires")
    @Nullable
    Boolean passwordExpires;
    /**
     * True if the password can be changed.
     */
    @Getter(AccessLevel.NONE)
    @JsonProperty("PasswordChangeable")
    @Nullable
    Boolean passwordChangeable;
    /**
     * Current operational status of the account.
     * <p>Possible OPERATIONAL values:</p>
     * <ul>
     *   <li>"OK"</li>
     *   <li>"Degraded"</li>
     *   <li>"Pred Fail"</li>
     * </ul>
     * <p>Possible NON-OPERATIONAL values:</p>
     * <ul>
     *   <li>"Unknown"</li>
     *   <li>"Error"</li>
     *   <li>"Starting"</li>
     *   <li>"Stopping"</li>
     *   <li>"Service"</li>
     * </ul>
     * <p>Possible OTHER values:</p>
     * <ul>
     *   <li>"Stressed"</li>
     *   <li>"NonRecover"</li>
     *   <li>"No Contact"</li>
     *   <li>"Lost Comm"</li>
     * </ul>
     */
    @JsonProperty("Status")
    @Nullable
    String status;

    public @Nullable Boolean isDisabled() {
        return disabled;
    }

    public @Nullable Boolean isLocalAccount() {
        return localAccount;
    }

    public @Nullable Boolean isLockedOut() {
        return lockout;
    }

    public @Nullable Boolean isPasswordRequired() {
        return passwordRequired;
    }

    public @Nullable Boolean doesPasswordExpire() {
        return passwordExpires;
    }

    public @Nullable Boolean isPasswordChangeable() {
        return passwordChangeable;
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
