# Changelog

All notable changes to this project will be documented in this file.

Please check out the [Releases](https://github.com/eggy03/ferrumx-windows/releases) page to know more about the
commits and PRs that contributed to each of the releases.

This project tries its best to adhere to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

The following headings may be used while categorizing the list of changes made in each version:

- New Features
- Removed Features
- Bug Fixes
- Non-Breaking Changes
- Breaking Changes
- Dependency Updates
- Documentation
- Known Issues

## [4.0.2] - March 18, 2026

### Non-Breaking Changes

- apply `@Immutable` annotation to data entities
- apply `@Unmodifiable` annotation to default methods in `CommonMappingInterface`
- apply `@Unmodifiable` annotation to collection return types for service class methods
- enforce null checking in `command` parameter for `TerminalUtility#executeCommand` via Lombok's `@NonNull` annotation

### Dependency Updates

- Update `lombok` from `1.18.42` to `1.18.44`
- Update `jetbrains.annotations` from `26.0.2` to `26.1.0`
- Update `junit.jupiter` from `6.0.0` to `6.0.3`
- Update `mockito.core` from `5.21.0` to `5.23.0`
- Update `maven.compiler.plugin` from `3.14.1` to `3.15.0`

## [4.0.1] - February 07, 2026

### Non-Breaking Changes

- Infer nullability for fields and methods via JetBrain's `Nullable` and `NotNull` annotations.
  This also covers fields which were accidentally left devoid of any annotations
- Add null checks to method parameters where-ever required via Lombok's `@NonNull` annotation
- Apply style formats to code and Javadocs

## [4.0.0] - February 04, 2026

### Breaking Changes

- Service classes now return immutable lists of entity objects instead of mutable lists.
  The entity objects themselves were already immutable prior to this change.
  This change is only breaking for users who relied on mutating the returned lists directly.

### Non-Breaking Changes

- Removed unreachable defensive null check code in `CommonMappingInterface`'s `mapToList` method

### Dependency Updates

- Update `mockito-core` from `5.20.0` to `5.21.0`
- Update `central-publishing-maven-plugin` from `0.9.0` to `0.10.0`

### Documentation

- Update Javadoc to reflect the immutability of the lists returned by the service classes.
- Update Javadocs in `CommonMappingInterface` to properly describe return behavior for empty, schema mismatched, null
  and malformed JSON.
- Update Javadocs in service interfaces to notify that either the
  default or custom implementations of `CommonMappingInterface` may be used for mapping results

## [3.1.2] - January 28, 2026

### Dependency Updates

- Update AssertJ Core to 3.27.7 to mitigate CVE-2026-24400

## [3.1.1] - January 05, 2026

### Bug Fixes

- Fixed a bug in the `get(timeout)` service function in `Win32DiskDriveToPartitionAndLogicalDisk` class
  that caused it to execute the wrong script and always fetch null values

### New Features

- add `architecture` property to `Win32Processor`

## [3.1.0] - December 30, 2025

### New Features

- Introduce `TerminalUtility` for executing PowerShell commands with a timeout. This utility uses Commons Exec instead
  of `jPowerShell` to launch and close PowerShell sessions

- Service classes now implement a new `get(long timeout)` method which uses `TerminalUtility` and allows you to specify
  how long the PowerShell session will run before it is forcefully terminated regardless of the completion status
  of the script or command. This method does not rely on `jPowerShell` and can be safely used in Executor based
  workflows
  unlike the other methods

### Documentation

- Documented the new code
- Corrected`PCMCIA Type II (18)` to `PCMCIA Type I (18)` in `Win32PortConnector.java`

### Known Issues

- `TerminalUtility` has support for UTF-8 encoding and English Language support only [Limitation]

---

## [3.0.0] - November 21, 2025

This update marks the first public release since v1.3.7 and is a complete re-write of the library.
Please consult the documentation in README, WIKI, Javadocs and Examples to know everything about the changes.

### New Features

*Added the following new and previously removed associated and simple WMI retrieval functions*

- Win32AssociatedProcessorMemory
- Win32NetworkAdapterSetting
- Win32DiskDriveToPartition
- Win32LogicalDisk
- Win32LogicalDiskToPartition
- MSFTNetAdapter
- MSFTNetIPAddress, MSFTDNSClientServerAddress, MSFTNetConnectionProfile
- Win32Environment
- Win32Printer
- Win32UserAccount
- Win32Process
- Win32SoundDevice
- Win32PnPDevice

*Add the following new compounded WMI retrieval functions*

- HardwareId,
- MsftNetAdapterToIpAndDnsAndProfile,
- Win32DiskDriveToPartitionAndLogicalDisk,
- Win32DiskPartitionToLogicalDisk,
- Win32NetworkAdapterToConfiguration,
- Win32ProcessorToCacheMemory

### Bug Fixes

- Corrected the APM reversed query mapping (c9a33dbe5dfd4aa1d85dc8ea166fb1bb5901d523).
  This fix is related to a new feature introduced in the commit (4be83b3a8aba9e98e769ca7c76b68232516dcff5).
- Refactor PowerShell script loading to use input stream (678c6c39a96140ca30b7d1bb38a12848b376cce2).
  This fix is related to a new feature introduced in the commit (ca885e82e903ec69127d44c36dd425f0a0f2e672).

### Non-Breaking Changes

- Added a ReflectionUtility utility class.
  This change solves issue #22 which dynamically retrieves the properties to fetch, from the entity classes, so that
  only select properties to be fetched are passed on to the PowerShell query at runtime, instead of fetching all the
  properties.
- Add `@Nullable` annotations in missing entity fields
- Added `trace` level logging for service methods
- Re-organize unit test package structure
- Tests now validate entity fields
- Test method name changes following the 2.2.0 Interface Update
- Tests added for all the new code
- Centralize dependency versions and generate de-lombok source.jar (pom.xml)

### Breaking Changes

- Replace static MapperUtil with CommonMappingInterface
- Transfer Battery entities, services, mappers and tests to the peripheral package
- Rename CimQuery to Cimv2Namespace and extracted the MSFT queries into StandardCimv2Namespace enum
- Replace Win32ComputerSystemProduct with Win32ComputerSystem WMI class
- Move Win32OperatingSystem class to the system package
- Rename all the query enum names in Cimv2Namespace to have the "Win32_" prefix
- Update the return types of certain Win32 Entity fields to match with the Microsoft docs
- Make Win32BaseboardService implement CommonServiceInterface instead of OptionalCommonServiceInterface
- Entity fields which return Boolean now have custom getters instead of Lombok generated ones

### Dependency Updates

- N/A

### Documentation

- Document all the new classes
- Add granular `@since` tags to individual methods
- Fix API usage examples
- Clarify use cases of the interfaces
- Add package-info docs for base packages
- Update thread safety messages for entities
- Document all the entity fields
- Add copyright headers

### Known Issues

- N/A

---

## [2.2.0 Pre-Release] - October 17, 2025

### New Features

- `Processor` class now has a new field known as `numberOfEnabledCores`

### Removed Features

- Deprecated `getProcessor()` method of return type `Optional<Processor>` has now been removed.

### Non-Breaking Changes

- The project is now Java 8 compatible

### Breaking Changes

- Reversed from multi-module project to single module. The module that contained examples have been shifted to a new
  repository.
- Base package has been renamed from `org.ferrumx` to `io.github.eggy03.ferrumx.windows`
- Maven `group id` changed from `io.github.egg-03` to `io.github.eggy03`. The artifact id has been changed from
  `ferrumx-core` to `ferrumx-windows`
- `MapperUtil` has been moved from the `Utility` package to the `Mapper` package.
- All service classes now implement either a `CommonServiceInterface<T>` or an `OptionalCommonServiceInterface<T>`.
  Accordingly, all service methods have been renamed to either `get()` or `get(Powershell powershell)`, defined in the
  interface contract.

### Dependency Updates

Updated dependencies:

- lombok: 1.18.38 -> 1.18.42
- org.jetbrains:annotations: 13.0 -> 26.0.2
- junit-jupiter-engine: 5.13.4 -> 6.0.0
- mockito-core: 5.19.0 -> 5.20.0

Updated plugins:

- maven-javadoc-plugin: 3.11.2 -> 3.12.0
- central-publishing-maven-plugin: 0.8.0 -> 0.9.0

### Documentation

- Updated documentation to reflect the changes in this version

---

## [2.1.0 Pre-Release] - October 5, 2025

### New Features

- Service classes now have overloaded service methods that accept a PowerShell session,
  allowing callers to manage and reuse sessions.

### Bug Fixes

- Fixed typo in the `@SerializedName` annotation for the `smbiosPresent`
  field that caused the serialized values to return null

### Breaking Changes

- Deprecated the `getProcessor()` method of the `ProcessorService` class as it only returns a single processor
  and fails in case of systems with multiple CPUs. It is now encouraged to use the `getProcessors()` method instead,
  which returns a list of processors

- Entity classes now have a builder pattern

### Documentation

- Updated documentation to reflect the changes in this version

---

## [2.0.1 Pre-Release] - September 29, 2025

### Bug Fixes

- Fix lombok scope configuration

---

## [2.0.0 Pre-Release] - September 29, 2025

This change features a complete re-write of the project from ground up. The legacy Map<String, String> structure
has been replaced with a more fluent Entity-Service structure. No new features have been added but a lot of features
have been removed which will slowly be re-added with the upcoming pre-view releases.

### New Features

- A new class `ComputerSystemProductService` fetches detailed product information like vendor, name, and UUID.

### Removed Features

- Removed custom HWID generation logic.
- Removed `Win32_Printer` and `Win32_SoundDevice` classes.
- Removed the associative classes `Win32_AssociatedProcessorMemory`, `Win32_NetworkAdapterSetting`,
  `Win32_DiskDriveToDiskPartition` and `Win32_LogicalDiskToPartition`

### Breaking Changes

- The legacy shell classes and custom parsing logic have been completely removed and replaced with a new service/entity
  structure.
- Each service now runs a PowerShell query via `jPowershell` that parses the JSON output to its respective entity class
  via GSON. Instead of a Map data structure in v1, you now get typed objects with their fields which are accessible via
  the provided getters.
- Removed all forced checked exceptions. The only time an unchecked exception may be thrown is if the JSON is malformed,
  or if a PowerShell session fails to load.
- Improved null safety with the usage of Optional and empty Lists.
- Multi-Module Project: The codebase is now split into a multi-module Maven project:
  *_ferrumx-core_*: Contains the main library logic.
  *_ferrumx-examples_*: Provides practical usage demonstrations.

- Package Refactoring: All core packages have been moved under the `org.ferrumx.core` namespace, and the root package
  was renamed from `com.ferrumx` to `org.ferrumx` to align with open-source conventions.

### Dependency Updates

- Added `jPowershell` 3.1.1
- Added Google `gson` 2.13.2
- Added `lombok` 1.18.38
- Added `Jetbrains Annotations` 13.0
- Added `Mockito `Core` 5.19.0

### Documentation

- New documentation added for new code

---

## [1.3.7] - August 7, 2025

### Dependency Updates

- Bump `org.apache.commons:commons-lang3` from 3.17.0 to 3.18.0
- Bump `commons-codec` from 1.18.0 to 1.19.0
- Bump `central-publishing-maven-plugin` from 0.7.0 to 0.8.0
- Bump `junit-jupiter-engine` from 5.13.0-M2 to 5.13.4

---

## [1.3.6] - April 28, 2025

### Non-Breaking Changes

- HardwareID now supports collection of multiple CPU IDs in case of systems where there is more than 1 Physical CPU

---

## [1.3.5] - April 28, 2025

### Non-Breaking Changes

- HardwareID now generates a SHA256 digest of CPUID, MotherboardID and DiskIDs.

### Breaking Changes

- Renamed the methods in the parser classes (CIM_ML and CIM_SL) to for a better understanding of what they do

---

## [1.3.4] - April 9, 2025

### Non-Breaking Changes

- Removed custom `tinylog.properties`
- This will be replaced by SLF-4J API in the future

---

## [1.3.3] - April 8, 2025

### Non-Breaking Changes

- Project now supports Maven Build Tools

### Breaking Changes

- Associated classes have been moved to a separate package with its custom formatter.
  This has caused the following imports to change:

```java
    import com.ferrumx.system.hardware.Win32_AssociatedProcessorMemory;
    import com.ferrumx.system.networking.Win32_NetworkAdapterSetting;
    import com.ferrumx.system.operating_system.Win32_DiskDriveToDiskPartition;
    import com.ferrumx.system.operating_system.Win32_LogicalDiskToPartition;
```

to

```java
    import com.ferrumx.system.associatedclasses.Win32_AssociatedProcessorMemory;
    import com.ferrumx.system.associatedclasses.Win32_NetworkAdapterSetting;
    import com.ferrumx.system.associatedclasses.Win32_DiskDriveToDiskPartition;
    import com.ferrumx.system.associatedclasses.Win32_LogicalDiskToPartition;
```

---

## [1.3.2] - December 25, 2024

### Non-Breaking Changes

- Fixed some typos in the Javadoc, removed the version tag and updated references
- Normal tests converted to JUnit Test Cases
- Reduced duplicated code

---

## [1.3.1] - December 11, 2024

### Non-Breaking Changes

- Removed the ErrorLog class which was deprecated since v1.2.4
- The GUI code has been moved to a new repository called FerrumX-GUI

---

## [1.3.0] - September 11, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### New Features

- Added two new battery classes: Win32_Battery and Win32_PortableBattery
- Added a monitor class: Win32_DesktopMonitor

### Breaking Changes

- *Deprecate the custom ErrorLog:* Up till version 1.2.4, all PowerShell errors were automatically
  logged in a text file. Starting from v1.3.0, this behavior has been replaced with a custom exception
  called ShellException that gets thrown in case of any PowerShell errors. All the Win32 classes now
  rethrow this exception. The developer needs to catch this exception and either rethrow it or handle it accordingly.

---

## [1.2.4] - June 23, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### Non-Breaking Changes

HardwareID changes:

- Removed Username, DeviceName, RAM Count and Storage Count
- Added DriveIDs

New ID format: CPUName/CPUID/MotherboardName/DriveIDs [58741d85f84727720c90dad059e3596771c1e396]

---

## [1.2.3] - June 21, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### Non-Breaking Changes

- Removed the unnecessary hard coded System Drive letter from the commands as it does not need them

---

## [1.2.2] - June 11, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### Non-Breaking Changes

- The ExecutorService object in HardwareID class now uses the AutoCloseable interface
- The implicit constructor of HardwareID class has it's access modifier changed from private to protected to allow
  inheritance

---

## [1.2.1] - June 11, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### New Features

- Win32_Processor class includes a new property called "NumberOfLogicalProcessors".
  This is different from "ThreadCount" in a way that "ThreadCount always reports the number of hardware threads in a
  CPU,
  whereas the other reports the number of threads that the OS has been allowed to access.
  For example, you have a CPU that has 12 hardware threads, and you have configured your OS to boot with 8 threads.
  In this case, the "ThreadCount" would be 12 and "NumberOfLogicalProcessors" would be 8.

---

## [1.2.0] - May 7, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### New Features

- Only GUI updates

---

## [1.1.0] - May 7, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### Breaking Changes

- The formatters are now classified into CIM_ML and CIM_SL
  ML stands for Multi-Line and SL for Single-Line
  The Win32 Relation Classes will still have their own formatter.
  The rest of the Win32_Classes will call either CIM_SL or CIM_ML for formatting.
  The Win32_Classes now call either CIM_SL or CIM_ML methods and pass on the attributes, which then carry out the
  parsing,
  formatting and error handling.
  HWID Generation retains it's Multi-threading capability following CIM_SL refactor
  The Win32 Relation Classes still have their own formatter.

- Win32_SystemDrivers has been removed
- Changed the package naming scheme from `com.egg.*` to `com.ferrumx.*`

---

## [1.0.2] - April 16, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### Bug Fixes

- Removed () from the class name literal
- Fixed an error in naming a property in `Win32_Printer` which caused the property's value to be not displayed
- Fixed HWID generation functions
  They will now be generated based on the following nomenclature:
  Username/DeviceName/CPU/CPUID/MotherboardName/RAM-COUNT/STORAGE-COUNT

### Non-Breaking Changes

- Log files will now be generated as FerrumX_ERRORLOG-DateStamp.log [e76dafc19c69cc2da3cc4aa41ae8fbe499f4b426]

---

## [1.0.1] - April 13, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

### Non-Breaking Changes

- Shifted WMIC to FerrumL (a.k.a. Ferrum Legacy) that supports Windows Vista and Windows 7
  This does not break functionality since none of the existing functions depend on WMIC
  for information retrieval

- If a PowerShell process fails, the functions will now return empty Collections.

### Breaking Changes

- Changed `Win32_Processor` function name from `getDeviceIDList()` to `getProcessorList()`

## [1.0.0] - April 10, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate
repository.*

Project name changed from `WSIL` to `FerrumX`

### New Features

- A semi-unique, multithreaded HWID Generation function based on the following format:
  Username/Device-name/CPUName/CPUID/MotherboardName/RAMCount/StorageCount

### Bug Fixes

- Accommodated for multi-line value parsing for property values spanning more than a single line in PowerShell

### Non-Breaking Changes

- Update CIMFormat to make it able to log PowerShell errors and support multi-line property values

### Breaking Changes

- Removed the Bank variable from `Win32_PhysicalMemory` as a form of device ID collection method (when Tag cannot be
  found)
  as it may no longer be necessary

---