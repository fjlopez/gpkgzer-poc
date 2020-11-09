package com.github.gpkg4all.common


/**
 * GeoPackage Builder.
 *
 * @param core the core elements defined by the specification
 * @param level the target level
 * @param options the options that must be available
 * @param extensions the extension that must be available
 */
fun builder(
    core: Spec,
    level: ContentTarget = ContentTargets.metadata,
    options: List<Module> = emptyList(),
    extensions: List<Module> = emptyList(),
    name: String? = null
): RootFileTree {
    return RootFileTree(
        children = (name?.let {
            listOf(readme(it))
        } ?: emptyList()) + listOf(
            File(
                filename = "metadata.sql",
                language = "sql",
                content = core.features.map { it.value.definition },
                asText = { it.joinToString("\n") }
            )
        )
    )
}

fun readme(name: String): File<String> = File(
    filename = "README.md",
    language = "markdown",
    content = """
# How to build this GeoPackage

The GeoPackage Encoding Standard describes a set of conventions for storing the geospatial information within a SQLite database.
This document explains how to build a GeoPackage from the content of the `$name.zip` archive.

## Getting Started

The SQLite project provides a simple command-line program named **sqlite3** (or **sqlite3.exe** on Windows) 
that allows the user to manually enter and execute SQL statements against a SQLite database.

To create a GeoPackage named "`$name.gpkg`" you might do this:

```shellsession
$ sqlite3 $name.gpkg < metadata.sql
```

## Double-click Startup on Windows

Windows users can double-click on the **sqlite3.exe** icon to cause the command-line shell to pop-up a terminal window running SQLite. 
However, because double-clicking starts the **sqlite3.exe** without command-line arguments, no database file will have been specified.

To create a GeoPackage named "`$name.gpkg`" when no database file has been specified you might do this:

```shellsession
SQLite version 3.28.0 2019-03-02 15:25:24
Enter ".help" for usage hints.
Connected to a transient in-memory database.
Use ".open FILENAME" to reopen on a persistent database.
sqlite> .open $name.gpkg
sqlite> .read metadata.sql
sqlite> .quit
```

You might want to use a full pathname to ensure that `$name.gpkg` and `metadata.sql` are loaded from the directory that you think they are in.
Use forward-slashes as the directory separator character. 
In other words use "`c:/work/$name.gpkg`", not "`c:\work\$name.gpkg`".
""".trimIndent(),
    asText = { it }
)