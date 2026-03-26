# Brief history about major releases since v1

### About v2

The **2.x** versions (`v2.0.0`–`v2.2.0`) served as **pre-releases** that introduced the new service-layered
architecture.

- **`v2.0.0`** was a complete rewrite of the project.
  See [v2.0.0 pre-release notes](https://github.com/eggy03/ferrumx-windows/releases/tag/v2.0.0)
- **`v2.1.0`** and **`v2.2.0`** built upon that foundation with architectural enhancements and breaking changes.
- Each 2.x pre-release was **incompatible** with the others.

While v2 is publicly available, it is not recommended for general use.

### About v3

- **`v3.0.0`** is the stable release that is built upon the 2.x architecture.
- It introduces new MSFT classes and re-introduces several entities from **`v1.3.7`** that were not present in the 2.x
  versions.
- It also renames all of the `v2.x` classes to have a `Win32` prefix, similar to the `v1.x` naming convention.

### About v4

- **`v4.0.0`** can be considered as a drop-in replacement for `3.x.x`.
- The only breaking change is that the service classes now return immutable
  collections [See PR 26](https://github.com/eggy03/ferrumx-windows/pull/26).
  The breaking change will affect you if and only if your workflow directly mutates the collection returned by the
  service classes.

# Migration Guide (v1 to v3)

### JDK Version Note

JDK Requirements

- v1: Required JDK 21+

- v3: Requires JDK 8+

### Dependency Installation

The Maven co-ordinates changed in v3

| Version | groupId          | artifactId      |
|---------|------------------|-----------------|
| v1      | io.github.egg-03 | ferrum-x        |
| v3      | io.github.eggy03 | ferrumx-windows |

```xml

<dependency>
    <groupId>io.github.eggy03</groupId>
    <artifactId>ferrumx-windows</artifactId>
    <version>X.Y.Z</version>
</dependency>
```

Replace `X.Y.Z` with the latest
version [![Maven Central Version](https://img.shields.io/maven-central/v/io.github.eggy03/ferrumx-windows)](https://central.sonatype.com/artifact/io.github.eggy03/ferrumx-windows)

### Additional Resources

The migration guide does not include every single detail and does focus on all the new features introduced, such as
the [MSFT Classes](https://eggy03.github.io/ferrumx-windows-documentation/io/github/eggy03/ferrumx/windows/mapping/network/package-summary.html)
or
the [Compound Entity Package](https://eggy03.github.io/ferrumx-windows-documentation/io/github/eggy03/ferrumx/windows/entity/compounded/package-summary.html)

To know more about all the changes introduced, check out the:

- [Javadocs](https://eggy03.github.io/ferrumx-windows-documentation)
- [Examples](https://github.com/eggy03/ferrumx-windows-examples)
- [Releases](https://github.com/eggy03/ferrumx-windows/releases)

### Example Code Comparison

v1.3.7

```java
public class Win32DesktopMonitorExample {

    static void main(String[] args) throws IOException, ShellException, InterruptedException {

        // get a List of all monitor IDs
        List<String> monitorIdList = Win32_DesktopMonitor.getMonitorID();

        // For each monitorID, fetch a map of its properties
        for (String monitorId : monitorIdList) {
            Map<String, String> monitorMap = Win32_DesktopMonitor.getMonitorProperties(monitorId);
            // iterate through the map and print the properties
            monitorMap.forEach((k, v) -> System.out.println(k + ": " + v));
        }
    }
}
```

v3

```java
public class Win32DesktopMonitorExample {

    static void main(String[] args) {

        // retrieve and print all monitor data using an auto-managed PowerShell session
        // The "get()" method returns a list of Win32DesktopMonitor entity objects,
        // and each object’s "toString()" prints its fields in JSON pretty-print format.
        new Win32DesktopMonitorService()
                .get()
                .forEach(monitor -> System.out.println(monitor.toString()));
    }
}
```

### Principal Changes

| Area                  | v1.3.7                    | v3.0.0                                               |
|-----------------------|---------------------------|------------------------------------------------------|
| Entity Access         | Static classes            | Service-based                                        |
| PowerShell Management | Implicit session per call | Optional persistent session                          |
| Data Model            | Map<String, String>       | Strongly-typed entity classes                        |
| Naming Convention     | Mixed prefixes            | Mixed prefixes but retains the Win32 prefix like 1.x |
| Exception Handling    | 4 checked exceptions      | No checked exceptions                                |

#### Entity Access

v1.3.7

```java
List<String> monitorIdList = Win32_DesktopMonitor.getMonitorID();
Map<String, String> monitorMap = Win32_DesktopMonitor.getMonitorProperties(monitorId);
```

v3.0.0

```java
List<Win32DesktopMonitor> monitorList = new Win32DesktopMonitorService().get();
```

#### PowerShell Management

In v1, all function calls by created an implicit auto-managed session with no freedom to manually manage a session.

With v3, PowerShell session invocations are powered
by [the jPowerShell library](https://github.com/profesorfalken/jPowerShell). Each service in `ferrumx-windows` allows
you to use either a self-managed session or an auto-managed session.

- Auto-Managed Session

```java
public class Example {
    static void main() {
        new Win32DesktopMonitorService().get();
    }
}
```

The get method automatically manages the session with default configurations so you don't have to.

- Self-Managed Session

All services in `ferrumx-windows` also have a `get(PowerShell)` function that accepts your managed PowerShell session as
a parameter. It is your responsibility to create and close the session after the task associated with the session is
over, in order to avoid resource leaks.
You can create your own session, either in a try-with-resources block (jPowerShell PowerShell sessions implement
Auto-Closeable)
or manually open and close the session.

```java
public class Example {
    static void main() {
        try (PowerShell shell = PowerShell.openSession()) {
            new Win32DesktopMonitorService().get(shell);
        }
    }
}
```

#### Data Model

In v1, all properties and their values were represented as `Map<String, String>`.
This had a major disadvantage as consumers would need to know the exact property name to get its value.
With v3, there are dedicated Win32 entity classes with strongly typed fields with getters,
so you don't have to remember what property the library fetches.

v1.3.7

```java
public class Example {

    static void main() {
        List<String> monitorIdList = Win32_DesktopMonitor.getMonitorID();
        Map<String, String> monitorMap = Win32_DesktopMonitor.getMonitorProperties(monitorIdList.get(0));
        monitorMap.get("PNPDeviceID");
        monitorMap.get("Name");
        monitorMap.get("Status");
    }
}
```

v3.0.0

```java
public class Example {

    static void main() {
        List<Win32DesktopMonitor> monitorList = new Win32DesktopMonitorService().get();
        Win32DesktopMonitor firstMonitor = monitorList.get(0);
        firstMonitor.getName();
        firstMonitor.getStatus();
    }
}
```

#### Naming Conventions

In v1, all classes had the `Win32_` prefix.
v3 retains the same convention for the classes ported from v1, but without the `_`.
So `Win32_DesktopMonitor` in v1 is now `Win32DesktopMonitor` in v3

#### Exception Handling

In v1, you had to handle `IOException`, `ShellException` and `InterruptedException` manually.
In v3 these are handled by the underlying jPowerShell library which wraps it in its own unchecked exception.
v3 also uses the `gson` library, so `JsonSyntaxException` is another unchecked exception that would be thrown if the
PowerShell's output does not match the expected structure.

# Migration Guide (v3 to v4)

- **`v4.0.0`** can be considered as a drop-in replacement for `3.x.x`.
- The only breaking change is that the service classes now return immutable
  collections [See PR 26](https://github.com/eggy03/ferrumx-windows/pull/26).
  The breaking change will affect you if and only if your workflow directly mutates the collection returned by the
  service classes.