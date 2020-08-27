package components.utils

import kotlinext.js.Object
import kotlinext.js.jsObject
import react.*

interface FunctionalComponentDefaults<T> {
    var displayName: String
    var defaultProps: T
}

operator fun <P : RProps, R : RProps> HOC<P, R>.invoke(
    component: FunctionalComponent<P>,
    displayName: String,
    defaultProps: P.() -> Unit = {}
): RClass<R> =
    call(null, { props: P ->
        component(props)
    }).also {
        Object.assign(
            it,
            jsObject<FunctionalComponentDefaults<P>> {
                this.displayName = "RConnect ($displayName)"
            }
        )
        Object.assign(
            it.asDynamic().WrappedComponent as FunctionalComponent<P>,
            jsObject<FunctionalComponentDefaults<P>> {
                this.displayName = displayName
                this.defaultProps = jsObject(defaultProps)
            }
        )
    }


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

