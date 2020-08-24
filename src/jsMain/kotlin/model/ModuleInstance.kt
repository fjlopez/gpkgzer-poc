package model

data class ModuleInstance(
    val module: Module,
    val default: Boolean = false,
    val valid: Boolean = true,
    val invalid: String? = null,
)