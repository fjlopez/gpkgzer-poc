package components.utils

import kotlinx.html.SPAN
import kotlinx.html.tabIndex


fun SPAN.disableTab() {
    tabIndex = "-1"
}