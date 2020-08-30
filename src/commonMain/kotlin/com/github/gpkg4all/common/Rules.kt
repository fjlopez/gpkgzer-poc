package com.github.gpkg4all.common

sealed class Rule
object CoreRule : Rule()
data class AtLeastOneRule(var modules: List<Module>) : Rule()

@Suppress("FunctionName")
fun AtLeastOneRule(vararg modules: Module) = AtLeastOneRule(modules.asList())