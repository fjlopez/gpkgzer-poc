package components.common.explore

import com.github.gpkg4all.common.File
import com.github.gpkg4all.common.FileItem
import com.github.gpkg4all.common.Folder
import com.github.gpkg4all.common.RootFileTree
import components.common.iconFile
import components.utils.disableTab
import components.utils.functionalComponent
import kotlinx.html.UL
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.*

external interface TreeProps : RProps {
    var selected: FileItem?
    var onClickItem: (FileItem) -> Unit
    var tree: RootFileTree?
}


private val treeComponent = functionalComponent<TreeProps>(displayName = "Tree") { props ->

    var openFolders by useState<List<Folder>>(emptyList())

    fun RDOMBuilder<UL>.renderItem(item: FileItem, depth: Int, parentPath: String) {
        val path = if (parentPath.isEmpty()) item.filename else parentPath + "/" + item.filename
        when (item) {
            is Folder -> {
                // TODO Implement folder management
            }
            is File<*> -> {
                val isSelected = item == props.selected
                val isDisabled = item.language == null
                li("li-file") {
                    attrs.key = "li$path"
                    a(
                        href = "/#",
                        classes = "file level-${depth} ${if (isDisabled) "disabled" else ""} ${if (isSelected) "selected" else ""}"
                    ) {
                        attrs {
                            if (isDisabled) disableTab()
                            key = "s1$path"
                            onClickFunction = { event: Event ->
                                event.preventDefault()
                                if (!isDisabled) {
                                    props.onClickItem(item)
                                }
                            }
                        }
                        span("item-content") {
                            attrs.disableTab()
                            span("text") {
                                attrs.key = "s2$path"
                                span("icon") {
                                    attrs.key = "s3$path"
                                    iconFile()
                                }
                                +item.filename
                            }
                        }
                    }
                }
            }
        }
    }

    ul("explorer-ul") {
        props.tree?.children?.map { renderItem(it, 0, "") }
    }
}

fun RBuilder.tree(selected: FileItem?, onClickItem: (FileItem) -> Unit, tree: RootFileTree?) {
    child(treeComponent) {
        attrs {
            this.selected = selected
            this.onClickItem = onClickItem
            this.tree = tree
        }
    }
}