@file:JsNonModule
@file:JsModule("react-syntax-highlighter")

package modules.react.syntaxhighlighter

import react.RProps
import react.ReactElement

external interface PrismProps : RProps {
    var language: String?
    var children: String?
    var style: Map<String, Map<String, String>>?
}

external val Prism: (PrismProps) -> ReactElement

