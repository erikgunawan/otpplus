package id.faazlab.otpplus

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


/**
 * Created by erikgunawan on 08/12/25.
 *
 * A sealed class representing different types of UI text that can be displayed.
 * This class provides a unified way to handle both static strings and string resources
 * with proper internationalization support.
 */
sealed class UiText {

    /**
     * Represents a dynamic string value that can be set at runtime.
     *
     * @param value The string value to display
     */
    data class DynamicString(val value: String) : UiText()
    data class Combined(val parts: List<UiText>) : UiText()
    /**
     * Represents a string resource that will be resolved from the app's resources.
     *
     * @param resId The string resource ID
     * @param args Optional format arguments for the string resource
     */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    /**
     * Resolves the text to a string value in a Composable context.
     * This function automatically handles string resource resolution.
     *
     * @return The resolved string value
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
            is Combined -> {
                buildString {
                    parts.forEachIndexed { index, part ->
                        append(part.asString())
                        if (index != parts.lastIndex) append(" ")
                    }
                }
            }
        }
    }

    /**
     * Resolves the text to a string value using the provided context.
     * This function is useful when you need to resolve text outside of a Composable context.
     *
     * @param context The context to use for string resource resolution
     * @return The resolved string value
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
            is Combined -> parts.joinToString("") { it.asString(context) }
        }
    }
}