package modules.react.markdown

import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.ReactElement


@JsNonModule
@JsModule("react-markdown")
@Suppress("FunctionName")
external fun ReactMarkdown(props: ReactMarkdownProps): ReactElement

external interface ReactMarkdownProps : RProps {
    var children: String
    var linkTarget: String?
}

@Suppress("FunctionName")
fun RBuilder.ReactMarkdown(children: String = "", linkTarget: String? = null) {
    child(modules.react.markdown.ReactMarkdown(jsObject {
        this.children = children
        this.linkTarget = linkTarget
    }))
}