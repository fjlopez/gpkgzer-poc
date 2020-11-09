package modules.react.markdown

import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.ReactElement


@JsNonModule
@JsModule("react-markdown")
@Suppress("FunctionName")
external fun ReactMarkdown(props: ReactMarkdownProps): ReactElement

external interface RendererProps : RProps {
    var language: String?
    var value: String?
}

external interface Renderers {
    var code: ((RendererProps) -> ReactElement)?
}

external interface ReactMarkdownProps : RProps {
    var children: String
    var linkTarget: String?
    var renderers: Renderers
}

@Suppress("FunctionName")
fun RBuilder.ReactMarkdown(children: String = "", linkTarget: String? = null, renderers: Renderers? = null) {
    val props = jsObject<ReactMarkdownProps> {
        this.children = children
        linkTarget?.let { this.linkTarget = it }
        renderers?.let { this.renderers = it }
    }
    child(ReactMarkdown(props))
}