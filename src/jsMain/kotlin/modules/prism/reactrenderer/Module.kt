package modules.prism.reactrenderer

import react.RClass
import react.RProps
import react.ReactElement

external interface PrismTheme

external interface ThemeDict {
    var root: StyleObj
    var plain: StyleObj
}

external interface Token {
    var types: Array<String>
    var content: String
    var empty: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface StyleObj

external interface LineInputProps {
    var key: Any?
        get() = definedExternally
        set(value) = definedExternally
    var style: StyleObj?
        get() = definedExternally
        set(value) = definedExternally
    var className: String?
        get() = definedExternally
        set(value) = definedExternally
    var line: Array<Token>
}

external interface LineOutputProps {
    var key: Any?
        get() = definedExternally
        set(value) = definedExternally
    var style: StyleObj?
        get() = definedExternally
        set(value) = definedExternally
    var className: String
}

external interface TokenInputProps {
    var key: Any?
        get() = definedExternally
        set(value) = definedExternally
    var style: StyleObj?
        get() = definedExternally
        set(value) = definedExternally
    var className: String?
        get() = definedExternally
        set(value) = definedExternally
    var token: Token
}

external interface TokenOutputProps {
    var key: Any?
        get() = definedExternally
        set(value) = definedExternally
    var style: StyleObj?
        get() = definedExternally
        set(value) = definedExternally
    var className: String
    var children: String
}

external interface RenderProps {
    var tokens: Array<Array<Token>>
    var className: String
    var style: StyleObj
    var getLineProps: (input: LineInputProps) -> LineOutputProps
    var getTokenProps: (input: TokenInputProps) -> TokenOutputProps
}

external interface HighlightProps : RProps {
    var Prism: dynamic
    var theme: PrismTheme?
        get() = definedExternally
        set(value) = definedExternally
    var language: String
    var code: String
    var children: (props: RenderProps) -> ReactElement
}


external interface HighlightClass : RClass<HighlightProps> {
    var themeDict: ThemeDict
    var getLineProps: (lineInputProps: LineInputProps) -> LineOutputProps
    var getTokenProps: (tokenInputPropsL: TokenInputProps) -> TokenOutputProps
}

@JsModule("prism-react-renderer")
@JsNonModule
external val HighlightModule: dynamic

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
val Highlight = HighlightModule.default as HighlightClass

@JsModule("prism-react-renderer/prism")
@JsNonModule
external val PrismModule: dynamic
val PrismDefault = PrismModule.default

