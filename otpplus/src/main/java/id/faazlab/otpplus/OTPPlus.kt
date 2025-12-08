package id.faazlab.otpplus

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.contextmenu.modifier.appendTextContextMenuComponents
import androidx.compose.foundation.text.contextmenu.modifier.filterTextContextMenuComponents
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.delay

/**
 * Created by erikgunawan on 08/12/25.
 *
 * OTP (One-Time Password) input component with 6-digit input fields
 *
 * This component provides a user-friendly OTP input interface with individual digit boxes.
 * It includes visual feedback for focus states, error states, and shake animation for errors.
 * The component automatically handles numeric input validation and limits input to 6 digits.
 *
 * @param modifier Modifier for customization of the OTP container
 * @param value Current OTP value (should be 0-6 digits)
 * @param errorMessage Error message to display below the OTP fields (optional)
 * @param innerSpace Spacing between individual OTP digit boxes (default: 8.dp)
 * @param focusRequester FocusRequester for programmatic focus control (optional)
 * @param onValueChanged Callback when the OTP value changes, providing the new value
 * @param onComplete Callback when all 6 digits are entered (optional)
 */
@Composable
fun OTPPlus(
    modifier: Modifier = Modifier,
    value: String = "",
    errorMessage: UiText? = null,
    innerSpace: Dp = 8.dp,
    focusRequester: FocusRequester? = null,
    onValueChanged: (String) -> Unit = {},
    onComplete: (() -> Unit)? = null
) {
    var hasFocused by remember { mutableStateOf(false) }

    val isError = errorMessage != null

    var shakeOffset by remember { mutableFloatStateOf(0f) }

    val animatedShake by animateFloatAsState(
        targetValue = if (errorMessage != null) shakeOffset else 0f,
        animationSpec = tween(durationMillis = 100),
        label = "shakeAnimation"
    )

    LaunchedEffect(isError) {
        if (isError) {
            repeat(3) {
                shakeOffset = 10f
                delay(50)
                shakeOffset = -10f
                delay(50)
            }
            shakeOffset = 0f
        }
    }

    // Trigger onComplete callback when 6 digits are entered
    LaunchedEffect(value) {
        if (value.length == 6) {
            onComplete?.invoke()
        }
    }

    Column(
        modifier = modifier
            .offset(x = animatedShake.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= 6 && it.isDigitsOnly())
                    onValueChanged.invoke(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .appendTextContextMenuComponents {  }
                .filterTextContextMenuComponents { false }
                .onFocusChanged { focusState ->
                    hasFocused = focusState.isFocused
                }.then(
                    focusRequester?.let {
                        Modifier.focusRequester(it)
                    } ?: Modifier
                ),
            singleLine = true,
            decorationBox = {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(innerSpace),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(6) { index ->
                        val otpChar = when {
                            index >= value.length -> ""
                            else -> value[index].toString()
                        }

                        val isFocused = if (value.length < 6)
                            value.length == index && hasFocused
                        else value.length == index + 1

                        Box(
                            modifier = Modifier
                                .background(if (isFocused) OTPPlusColor.BoxFocused else OTPPlusColor.BoxDefault, shape = RoundedCornerShape(12.dp))
                                .border(
                                    BorderStroke(
                                        width = 1.dp,
                                        color = if (isError) OTPPlusColor.Error else if (isFocused) OTPPlusColor.Focused else OTPPlusColor.BorderDefault
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .weight(1f)
                                .aspectRatio(0.9375f)
                        ) {
                            Text(
                                text = otpChar,
                                modifier = Modifier.align(Alignment.Center),
                                color = OTPPlusColor.TextDefault
                            )
                        }
                    }
                }
            }
        )

        if (isError) {
            Text(
                text = errorMessage.asString(),
                color = OTPPlusColor.Error,
                modifier = Modifier.padding(top = 6.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
        }
    }
}