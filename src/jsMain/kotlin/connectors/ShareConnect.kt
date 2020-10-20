package connectors

import com.github.gpkg4all.common.Project
import components.common.share.ShareComponentProps
import components.common.share.shareComponent
import components.utils.connects
import react.RBuilder
import react.RProps
import react.redux.Options
import react.redux.rConnect
import reducer.AppState
import redux.RAction
import redux.WrapperAction

external interface ShareProps : RProps {
    var open: Boolean
    var onClose: () -> Unit
}

external interface ShareStateProps : RProps {
    var project: Project
}

external interface ShareDispatchProps : RProps

private val mapStateToProps: ShareStateProps.(AppState, RProps) -> Unit = { state, _ ->
    project = state.project
}

private val mapDispatchToProps: ShareDispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = { _, _ -> }

private val options: Options<AppState, ShareProps, ShareStateProps, ShareComponentProps>.() -> Unit =
    {}

@Suppress("FunctionName")
fun RBuilder.Share(show: Boolean, onClose: () -> Unit) =
    rConnect(mapStateToProps, mapDispatchToProps, options).connects(
        displayName = "Share",
        component = shareComponent
    ).invoke {
        attrs.open = show
        attrs.onClose = onClose
    }
