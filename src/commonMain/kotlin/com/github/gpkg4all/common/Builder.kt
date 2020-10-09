package com.github.gpkg4all.common

data class File(
    val name: String,
    val body: String
)

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
    extensions: List<Module> = emptyList()
): List<File> {
    return listOf(File(
        name = "metadata.sql",
        body = core.features.joinToString(separator = "\n") { it.value.definition }))
}