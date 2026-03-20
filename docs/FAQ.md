### **Q: What version of Windows is required to use this library?**

FerrumX-Windows has been tested on **Windows 10, and Windows 11**.
Older OSes (7SP1 and 8.1) may work if **PowerShell 5.1** can be installed with Windows Management Framework 5.1.

---

### **Q: Does this library work on Linux or macOS?**

Not yet. Information gathered by this library comes from WMI classes.
Cross-platform support is planned but not yet implemented.

---

### **Q: What Java versions are supported?**

**Java 8 and newer**.

---

### **Q: Do I need PowerShell installed?**

Yes. PowerShell is required to query WMI classes. **PowerShell 5.1+** is recommended.
PowerShell should be available by default on all supported windows editions unless explicitly disabled by your
administrator.

---

### **Q: Is administrator privilege required?**

As of now, NONE of the queries or scripts require any sort of admin privileges.

---

### **Q: Why are calls slow or delayed sometimes?**

Each function call causes a PowerShell session to spawn, which adds overhead. Re-using the session for batched queries
greatly improves performance after the initial startup. You can keep a session (or multiple ones) open
for as long as you want to but don't forget to close them before exiting your program. There are also auto-managed
sessions
that save you the headache of managing your own sessions but they immediately close the session after each call (
intended behavior).

---

### **Q: How can I re-use a session?**

Create your own PowerShell session and use it like the example below

```java
public class ReusableSessionExample {
    static void main(String[] args) {
        try (PowerShell session = PowerShell.openSession()) {
            List<Win32Processor> processors = new Win32ProcessorService().get(session);
            List<Win32CacheMemory> cacheMemoryList = new Win32CacheMemoryService().get(session);
        }
    }
}
```

---

### **Q: Why do some fields return null or appear missing?**

There could be many reasons for that. Some of them could be:

- Your hardware does not expose the field
- The WMI provider does not support it on your Windows version
- Unsupported PowerShell version
- WMI or PowerShell is blocked by your sysadmin or the session is restricted
- Your WMI repository is corrupted
- The output of your shell is not parsable (you can set your logging environment to TRACE level, to see the full output
  of your PowerShell)

---

### **Q: What can I do if I face such an issue ?**

You can:

- Check for existing issues in the issues page
- Check whether your PowerShell and or Windows version is supported
- Check that your WMI repository is not corrupted
- Check your PowerShell output for errors (either by enabling TRACE in your logging framework or manually querying in
  PowerShell)
- Fill up an issue in the issues page with steps to reproduce, windows and PowerShell versions along with the trace
  output
  Check out the contribution guidelines to see how to report an issue

---

### **Q: How stable is the library?**

The API of this library has evolved greatly over the course of two years. Each major version saw huge changes in the
syntax of its API and as of v3, the library is stable enough to be used in production environments. That being said,
there
is no guarantee about the syntax evolvement of the API in the near future, which might introduce a lot of breaking
changes.

---

### **Q: How do I contribute to this project?**

You can contribute via one of the following methods :)

- Contribute to the project's wiki (it's sparse)
- Contribute to the [examples repository](https://github.com/eggy03/ferrumx-windows-examples)
- Test the library in your system and submit suggestions and bugs by opening
  an [issue](https://github.com/eggy03/ferrumx-windows/issues)
- Report vulnerabilities in accordance with the [Security Policy](/SECURITY.md)
- Check out the [Contribution Guideline](/CONTRIBUTING.md) to know more about contributing to code and other ways of
  contributing

---

### **Q: Is there an alternative library I can use instead ?**

Yes :) And in many ways, it's way better than this one :)
[OSHI](https://github.com/oshi/oshi) has been in development since 2010 and has a very mature ecosystem :)
They skip WMI and use JNA to load native dlls (which is fast) and thus, require no PowerShell and have also been working
on migrating to
JDK25's FFM API which is much faster than JNA or JNI. `OSHI` also supports cross-platform implementation.
In contrast, `ferrumx-windows` uses WMI via PowerShell. I do intend to provide cross-platform support
in the future.

My version of JNA based implementation is called [PineTree,](https://github.com/eggy03/PineTree) but it is in very early
stages of development and uses WMI via COM API