package com.github.gpkg4all.common

import kotlinx.serialization.Serializable

@Serializable
data class ProjectDescriptor(
    val outputTarget: String?,
    val spec: String?,
    val content: String?,
    val options: List<String>,
    val extensions: List<String>,
    val name: String
)

fun Project.toDescriptor(): ProjectDescriptor = ProjectDescriptor(
    outputTarget = outputTarget?.key,
    spec = spec?.key,
    content = content?.key,
    options = options.map { it.module.key },
    extensions = extensions.map { it.module.key },
    name = name
)
