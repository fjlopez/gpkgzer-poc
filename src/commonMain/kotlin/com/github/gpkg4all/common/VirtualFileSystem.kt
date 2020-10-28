package com.github.gpkg4all.common

interface FileTree {
    val children: List<FileItem>
}

data class RootFileTree(
    override val children: List<FileItem> = emptyList()
) : FileTree

sealed class FileItem(
    /**
     * The name of the file or directory.
     */
    open val filename: String,
)

data class Folder(
    override val filename: String,
    override val children: List<FileItem> = emptyList()
) : FileItem(filename), FileTree

data class File<T>(
    override val filename: String,
    val language: String? = null,
    val content: T,
    val asText: (T) -> String = { "" }
) : FileItem(filename) {
    fun toText() = asText(content)
}
