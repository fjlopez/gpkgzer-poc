package model

data class Project(
    val outputTarget: OutputTarget?,
    val spec: Spec?,
    val content: ContentTarget?,
    val options: List<Module>
)