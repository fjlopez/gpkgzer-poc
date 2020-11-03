package components.common.explore

import com.github.gpkg4all.common.File
import components.utils.functionalComponent
import kotlinext.js.jsObject
import modules.prism.reactrenderer.Highlight
import modules.prism.reactrenderer.PrismDefault
import modules.react.markdown.ReactMarkdown
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

private val markdownComponent = functionalComponent<CodeProps<String>>(displayName = "Markdown") { props ->
    div("markdown") {
        ReactMarkdown(
            children = props.item.content,
            linkTarget = "_blank"
        )
    }
}

private val sqlComponent = functionalComponent<CodeProps<Any>>(displayName = "SQL") { props ->
    Highlight {
        attrs {
            Prism = PrismDefault
            language = props.item.language ?: "none"
            code = props.item.toText()
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
}

@Suppress("FunctionName")
fun RBuilder.Code(item: File<Any>, onChange: () -> Unit) {
    when (item.language) {
        "sql" -> sqlComponent
        "markdown" -> markdownComponent
        else -> null
    }?.let {
        child(it) {
            attrs {
                this.item = item
                this.onChange = onChange
            }
        }
    }
}