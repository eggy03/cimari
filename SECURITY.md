# Security Policy

## Supported Versions

Cimari is maintained by a single contributor. Only the versions listed below continue to receive security-related fixes.
Due to immutability of library releases, any change to the library will result in a version numbers being incremented
and only the latest released version will be supported.

| Version | Supported |
|---------|-----------|
| 1.x.x   | Supported |

## Reporting a Vulnerability

If you discover a vulnerability, please disclose it responsibly using the steps below.

### 1. Where to Report

You can report vulnerabilities **privately** using GitHub Security Advisories:

**https://github.com/eggy03/cimari/security/advisories/new**

If that is not possible, you may email:

**eggzerothree@proton.me**

---

### 2. What to Include

To help diagnose and fix the issue, include:

- Description of the vulnerability
- Steps to reproduce
- Affected version(s)
- Your environment (Windows version, PowerShell version)
- Relevant logs or output (only if safe to share)
- Potential impact (optional)

---

### 3. What to Expect

- **Acknowledgement:** within 72 hours, may be longer for emails
- **Status updates:** every 3–7 days until resolution, maybe longer if I'm unavailable
- **Fix timelines:**
    - Critical: as soon as possible
    - Medium: next patch/minor release
    - Low: scheduled based on project bandwidth

If you want public credit for your discovery, you will be acknowledged in the release notes.

---

## Responsible Disclosure

To protect users:

- Please **do not publicly disclose** the issue until a fix is released.
- Avoid posting proof-of-concept exploits in public areas before coordinated disclosure is complete.

---

## Scope

### In Scope

Security issues involving:

- Improper handling of PowerShell/WMI output by Cimari
- Injection vulnerabilities in command during execution
- Unsafe parsing or deserialization
- Sensitive data leakage
- Potential RCE or privilege escalation via library APIs
- Dependency-related security issues

List is non-exhaustive and may get updated frequently

### Out of Scope

- Vulnerabilities in PowerShell or WMI themselves
- Vulnerabilities caused by corrupted or modified WMI repositories
- Misconfigured user environments
- Vulnerabilities on unsupported OS or PowerShell versions

List is non-exhaustive and may get updated frequently

---

## Security Philosophy

Cimari interacts retrieves SMBIOS information via PowerShell.
To minimize risk of vulnerabilities:

- No provision is there for users to provide custom PowerShell commands or scripts in pre-defined service functions.
- No administrator privileges are required to execute any of the pre-defined scripts or commands
- No external network communication is required
- Logs collected do not leave the system
- Full transparency in vulnerability reporting and patching

---

If you are unsure whether something qualifies as a vulnerability, you may e-mail me at: eggzerothree@proton.me.
