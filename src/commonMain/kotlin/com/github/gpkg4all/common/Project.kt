package com.github.gpkg4all.common

typealias ProjectReducer<T> = Project.(T) -> Project

data class Project(
    val outputTarget: OutputTarget?,
    val spec: Spec?,
    val content: ContentTarget?,
    val options: List<ModuleInstance>,
    val extensions: List<ModuleInstance>
)

val updateSpec: ProjectReducer<Spec?> = { newSpec ->
    if (spec != newSpec) copy(spec = newSpec) else this
}

val updateOutputTarget: ProjectReducer<OutputTarget?> = { outputTarget ->
    if (outputTarget != outputTarget) copy(outputTarget = outputTarget) else this
}

val updateContent: ProjectReducer<ContentTarget?> = { contentTarget ->
    if (content != contentTarget) copy(content = contentTarget) else this
}

val toggleOption: ProjectReducer<ModuleInstance> = { instance ->
    val newOptions = if (instance in options) options - instance else options + instance
    copy(options = newOptions)
}

val removeExtension: ProjectReducer<ModuleInstance> = { instance ->
    require(instance in extensions) { "Instance to be removed must be present" }
    copy(extensions = extensions - instance)
}

val addExtension: ProjectReducer<ModuleInstance> = { instance ->
    require(instance !in extensions) { "Instance to be added must not be present" }
    copy(extensions = extensions + instance)
}

val validateExtensions: Project.(List<ModuleInstance>) -> List<ModuleInstance> = { list ->
    if (spec == null)
        list
    else
        list.map {
            val reasons = mutableListOf<String>()
            // Check Spec
            when {
                it.module.deprecatedBy == null && it.module.officialSince > spec ->
                    reasons += "GeoPackage >= ${it.module.officialSince.key}"
                it.module.deprecatedBy != null && it.module.officialSince > spec && it.module.deprecatedBy <= spec ->
                    reasons += "GeoPackage >= ${it.module.officialSince.key} and < ${it.module.deprecatedBy.key}"
                it.module.deprecatedBy != null && it.module.officialSince <= spec && it.module.deprecatedBy <= spec ->
                    reasons += "GeoPackage < ${it.module.deprecatedBy.key}"
            }
            // Extensions is required
            if (!options.any { option -> option.module == Modules.extensions }) {
                reasons += "Extensions option"
            }
            // Some option is required
            if (it.module.dependsOn is AtLeastOneRule) {
                val dependencies = it.module.dependsOn.modules
                if (!options.any { option -> option.module in dependencies }) {
                    when {
                        dependencies.size > 1 -> {
                            val (head, last) = dependencies.map { option -> option.title }
                                .chunked(dependencies.size - 1)
                            reasons += "at least one of ${head.joinToString(separator = " option, ")} or ${last.first()} option"
                        }
                        dependencies.size == 1 -> {
                            reasons += dependencies.first().title + " option"
                        }
                    }
                }
            }
            if (it.module.requireUdt && content == ContentTargets.metadata) {
                reasons += "User Tables (example)"
            }
            // Reasons
            when {
                reasons.size > 1 -> {
                    val (head, last) = reasons.chunked(reasons.size - 1)
                    it.copy(
                        valid = false,
                        invalid = "Requires ${head.joinToString(separator = ", ")}, and ${last.first()}"
                    )
                }
                reasons.size == 1 -> {
                    it.copy(valid = false, invalid = "Requires ${reasons.first()}")
                }
                !it.valid -> it.copy(valid = true)
                else -> it
            }
        }
}

val updateExtensionValidity: Project.() -> Project = {
    copy(extensions = validateExtensions(this, extensions))
}
