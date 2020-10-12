package components.common.explore

import com.github.gpkg4all.common.File
import com.github.gpkg4all.common.FileItem
import components.utils.functionalComponent
import kotlinext.js.jsObject
import modules.prism.reactrenderer.Highlight
import modules.prism.reactrenderer.PrismDefault
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.dom.key
import react.dom.pre
import react.dom.span

external interface CodeProps<T> : RProps {
    var item: File<T>
    var onChange: () -> Unit
}

private val codeComponent = functionalComponent<CodeProps<Any>>(displayName = "Tree") { props ->
    Highlight {
        attrs {
            Prism = PrismDefault
            language = props.item.language ?: "none"
            code = props.item.asText(props.item.content)
            children = { renderProps ->
                val groupLine = when {
                    renderProps.tokens.size > 999 -> 4
                    renderProps.tokens.size > 99 -> 3
                    renderProps.tokens.size > 9 -> 2
                    else -> 1
                }

                pre("${renderProps.className} line-$groupLine") {
                    attrs["style"] = renderProps.style
                    renderProps.tokens.mapIndexed { line, tokens ->
                        val props1 = renderProps.getLineProps(jsObject {
                            this.line = tokens
                            this.key = line
                        })
                        div(props1.className) {
                            props1.key?.let { attrs.key = it.toString() }
                            span("explorer-number") {
                                attrs["data-value"] = line + 1
                            }
                            tokens.mapIndexed { key, token ->
                                val props2 = renderProps.getTokenProps(jsObject {
                                    this.token = token
                                    this.key = key
                                })
                                span(props2.className) {
                                    props2.style?.let { attrs["style"] = it }
                                    props2.key?.let { attrs.key = it.toString() }
                                    +props2.children
                                }
                            }
                        }
                    }
                }
            }
        }
    }
//    console.info(highlight)
//    highlight {
//        attrs {
//            language = "jsx"
//        }
//    }
}

fun RBuilder.code(item: File<Any>, onChange: () -> Unit) {
    child(codeComponent) {
        attrs {
            this.item = item
            this.onChange = onChange
        }
    }
}