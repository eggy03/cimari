[![Project Stats](https://img.shields.io/badge/OpenHub-cimari-yellow?style=for-the-badge)](https://openhub.net/p/cimari)
[![License](https://img.shields.io/github/license/eggy03/cimari?style=for-the-badge)](https://github.com/eggy03/cimari/blob/main/LICENSE)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.eggy03/cimari?style=for-the-badge&color=pink)](https://central.sonatype.com/artifact/io.github.eggy03/cimari)
![Minimum JDK Version](https://img.shields.io/badge/Minimum%20JDK%20Version-8-blue?style=for-the-badge)

# Table Of Contents

- [About](#about)
- [Introduction](#introduction)
- [Cross Platform Support](#cross-platform-support)
- [Supported Operating Systems](#supported-windows-versions)
- [CI Stats](#ci-stats)
- [Download](#download)
- [Documentation](#documentation)
- [Usage](#usage)
- [License](#license)

# About

Cimari is a project derived from [ferrumx-windows](https://github.com/eggy03/ferrumx-windows), created to introduce
architectural changes to the entity classes, most notable of which is, making them deeply immutable.

FerrumX-Windows relies on [Lombok](https://projectlombok.org/) for a lot of boilerplate code generation for
its entity classes. While Lombok supports basic immutability, it does not make
Collections or Maps immutable, without which, entity classes can only be considered to be shallow immutable.

Cimari addresses this issue by switching to [Immutables](https://immutables.github.io/),
which supports deep immutability.

In addition, Cimari removes the dependency on [jPowerShell](https://github.com/profesorfalken/jPowerShell) along with
the service methods built around it.

The list doesn't end here. Besides the mentioned changes, there are other internal refactors which attempt to make
the project more readable and maintainable.

The full list of changes can be found in [Migration Guide](/docs/MIGRATION.md) and [Changelog](/CHANGELOG.md).

I will continue maintaining FerrumX-Windows separately for as long as I can.
However, all new features may not be backported.

# Introduction

Cimari is a wrapper around a
few [CIMWin32 WMI Provider](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-provider) classes
and select [MSFT](https://learn.microsoft.com/en-us/windows/win32/fwp/wmi/netadaptercimprov/msft-netadapter) classes.

Note that these aforementioned classes are a part
of [Windows Management Instrumentation](https://en.wikipedia.org/wiki/Windows_Management_Instrumentation),
which is Microsoft's implementation of the [DMTF CIM Schema](https://www.dmtf.org/standards/cim),
for retrieving [SMBIOS Information](https://www.dmtf.org/standards/smbios).
Both CIM and SMBIOS standards are defined by Distributed Management Task Force.

Written entirely in Java, the wrapper's primary job is to query the PowerShell for these classes and deserialize
provided [SMBIOS](https://www.dmtf.org/standards/smbios) output into typed entities.
Each entity, to which the output is deserialized, represents a loose mapping of an equivalent `Win32 Provider` or an
`MSFT`class.
For example, the [Win32_Processor](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor)
provider class has an equivalent `Win32Processor.java` entity which is composed of only the read-only properties of the
provider class.

For all the classes, only their read-only properties are accessed.
No methods to mutate properties have been implemented so far.

# Cross-Platform Support

Cimari heavily relies on WMI, which is Microsoft's implementation of CIM for Windows.
This means, only Windows Operating Systems are supported.

There are a few implementations of the CIM Schema that support a variety of Operating Systems:

- [OpenLMI from RedHat](https://docs.redhat.com/en/documentation/red_hat_enterprise_linux/7/html/system_administrators_guide/chap-openlmi)
- [SBLIM Small Footprint CIM Broker](https://github.com/zaneb/sblim-sfcb)
- [OpenPegasus](https://collaboration.opengroup.org/pegasus/)
- [Open Management Infrastructure from Microsoft](https://github.com/microsoft/omi)

Unfortunately, most of them have been deprecated or abandoned and were never widely adopted. Therefore, cross-platform
compatibility is mostly unplanned for now.

However, you can try out [dmidecode4j](https://github.com/eggy03/dmidecode4j) for Linux. It is a wrapper around
the popular utility [dmidecode](https://man.archlinux.org/man/dmidecode.8.en) for UNIX operating systems,
which provides human-readable [SMBIOS](https://www.dmtf.org/standards/smbios) data.
While it does not strictly follow the WMI CIM Schema, you will find it to be similar to FerrumX-Windows and Cimari, if
you are familiar with either of the APIs.

# Supported Windows Versions

- Windows: `7SP1¹`, `8.1¹`, `10²` and `11²`
- PowerShell: `5.1 and above`

¹For `Windows 8.1` and `7SP1`you can install
[Windows Management Framework 5.1](https://www.microsoft.com/en-us/download/details.aspx?id=54616) to upgrade to
PowerShell 5.1.
See [WMF availability across Windows Systems](https://learn.microsoft.com/en-us/powershell/scripting/windows-powershell/wmf-overview?view=powershell-7.5#wmf-availability-across-windows-operating-systems)

²This library is not tested on ARM versions of Windows 10 and 11 editions.

# CI Stats

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=eggy03_cimari&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=eggy03_cimari)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/eggy03/cimari/.github%2Fworkflows%2Fbuild-and-test.yml)
![Commits to main since latest release](https://img.shields.io/github/commits-since/eggy03/cimari/latest)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=eggy03_cimari&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=eggy03_cimari)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=eggy03_cimari&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=eggy03_cimari)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=eggy03_cimari&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=eggy03_cimari)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=eggy03_cimari&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=eggy03_cimari)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=eggy03_cimari&metric=coverage)](https://sonarcloud.io/summary/new_code?id=eggy03_cimari)

# Download

> **Minimum Supported Java Version:** 8

Maven:

```xml

<dependency>
    <groupId>io.github.eggy03</groupId>
    <artifactId>cimari</artifactId>
    <version>VERSION</version>
</dependency>
```

Gradle:

```gradle
implementation group: 'io.github.eggy03', name: 'cimari', version: 'VERSION'
```

Replace `VERSION` with the latest version available
in [central](https://central.sonatype.com/artifact/io.github.eggy03/cimari)

# Documentation

- [Javadocs](https://javadoc.io/doc/io.github.eggy03/cimari)
- [Developer Docs](/docs/DEVELOPER_DOCS.md)
- [Migration Guide](/docs/MIGRATION.md)
- [Examples](/docs/EXAMPLES.md)

# Usage

> [!IMPORTANT]
> More usage examples can be found [here](/docs/EXAMPLES.md).

```java
import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.service.processor.Win32ProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProcessorExample {

    Logger log = LoggerFactory.getLogger(ProcessorExample.class);

    static void main(String[] args) {

        List<Win32Processor> processorList = new Win32ProcessorService().get(15L); // time after which the session will auto close
        processorList.forEach(cpu -> log.info(cpu.toJson())); // JSON pretty print each cpu instance

        // individual fields are accessible via getter methods
        Win32Processor processor = processorList.getFirst(); // Java 21+ equivalent of (get(0))

        log.info("Processor Name: {}", processor.name());
        log.info("Processor Manufacturer: {}", processor.manufacturer());
        log.info("Processor Max Clock Speed: {} MHz", processor.maxClockSpeed());
    }
}
```

# License

This project is licensed under the [MIT License](/LICENSE).
