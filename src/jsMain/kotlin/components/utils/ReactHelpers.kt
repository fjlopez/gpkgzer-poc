package components.utils

import kotlinext.js.Object
import kotlinext.js.jsObject
import react.FunctionalComponent
import react.HOC
import react.RClass
import react.RProps

interface FunctionalComponentDefaults<T> {
    var displayName: String
    var defaultProps: T
}

fun <T: RProps> addDefaults(component: FunctionalComponent<T>, name: String, props: T.()->Unit = {}) = Object.assign(
    component,
    jsObject<FunctionalComponentDefaults<T>> {
        displayName = name
        defaultProps = jsObject(props)
    }
)

operator fun <P : RProps, R : RProps> HOC<P, R>.invoke(component: FunctionalComponent<P>): RClass<R> =
    call(null, { props: P ->
        component(props)
    })
