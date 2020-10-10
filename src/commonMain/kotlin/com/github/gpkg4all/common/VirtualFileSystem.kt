package com.github.gpkg4all.common

interface FileTree<T> {
    val children: List<FileItem<T>>
}

data class RootFileTree<T> (
    override val children: List<FileItem<T>> = emptyList()
) : FileTree<T>

fun <T,S> RootFileTree<T>.map(block: (current: FileItem<T>) -> S): RootFileTree<S> =
    RootFileTree(children.map { it.map(block) })

sealed class FileItem<T>(
    /**
     * The name of the file or directory.
     */
    open val filename: String,

    open val properties: T
) {
    abstract fun <S> map(block: (FileItem<T>) -> S): FileItem<S>
}

data class Folder<T>(
    override val filename: String,
    override val properties: T,
    override val children: List<FileItem<T>> = emptyList()
) : FileItem<T>(filename, properties), FileTree<T> {
    override fun <S> map(block: (FileItem<T>) -> S): Folder<S> =
        Folder(filename = this.filename, children = this.children.map { it.map(block) } , properties = block(this))
}

data class File<T,R>(
    override val filename: String,
    override val properties: T,
    val content: R
) : FileItem<T>(filename, properties) {
    override fun <S> map(block: (FileItem<T>) -> S): File<S,R> =
        File(filename = this.filename, content = this.content , properties = block(this))
}
