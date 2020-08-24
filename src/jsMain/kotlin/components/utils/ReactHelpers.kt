package components.utils

import kotlinext.js.Object
import kotlinext.js.jsObject
import react.*

interface FunctionalComponentDefaults<T> {
    var displayName: String
    var defaultProps: T
}

fun <T : RProps> addDefaults(component: FunctionalComponent<T>, name: String, props: T.() -> Unit = {}) = Object.assign(
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

fun <P : RProps> functionalComponent(
    displayName: String,
    defaultProps: P.() -> Unit = {},
    func: RBuilder.(props: P) -> Unit
): FunctionalComponent<P> = Object.assign(
    functionalComponent(func),
    jsObject<FunctionalComponentDefaults<P>> {
        this.displayName = displayName
        this.defaultProps = jsObject(defaultProps)
    })

