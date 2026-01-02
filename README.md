# OTP+

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Compose-1.5.0-blue.svg)](https://developer.android.com/jetpack/compose)
[![Play Store](https://img.shields.io/badge/Play%20Store-Available-brightgreen)](https://play.google.com/store/apps/details?id=id.faazlab.otpplus.demo)

A simple and customizable OTP (One-Time Password) input component for Android built with Jetpack Compose. Features include focus states, error handling with shake animation, auto-complete callback, and full customization support.

> 📱 **Try the [Demo App](#demo-app) on Play Store or check out the screenshots below!**

## Features

- ✅ **6-Digit OTP Input** - Clean and intuitive input interface
- ✅ **Visual Feedback** - Focus states and error states with shake animation
- ✅ **Auto-Complete Callback** - Automatic trigger when all digits are entered
- ✅ **Error Handling** - Built-in error message display with shake animation
- ✅ **Focus Management** - Programmatic focus control with `FocusRequester`
- ✅ **Customizable** - Custom spacing, colors, and styling
- ✅ **i18n Support** - Support for string resources and internationalization
- ✅ **Material Design 3** - Follows Material Design guidelines

## Installation

Add the dependency to your `build.gradle.kts` (or `build.gradle`):

```kotlin
dependencies {
    implementation("com.github.erikgunawan:otpplus:1.0.0")
}
```

Or if using Groovy:

```groovy
dependencies {
    implementation 'com.github.erikgunawan:otpplus:1.0.0'
}
```

### Setup JitPack Repository

Add JitPack to your project's `settings.gradle.kts` (or `settings.gradle`):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Add this
    }
}
```

Or if using Groovy:

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' } // Add this
    }
}
```

## Requirements

- **Min SDK**: 24 (Android 7.0)
- **Compile SDK**: 36
- **Kotlin**: 2.0.21+
- **Jetpack Compose**: 1.5.0+

## Basic Usage

### Simple OTP Input

```kotlin
import id.faazlab.otpplus.OTPPlus
import id.faazlab.otpplus.UiText

@Composable
fun OTPInputScreen() {
    var otpValue by remember { mutableStateOf("") }

    OTPPlus(
        value = otpValue,
        onValueChanged = { newValue ->
            otpValue = newValue
        }
    )
}
```

### With Error Handling

```kotlin
@Composable
fun OTPInputWithError() {
    var otpValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<UiText?>(null) }

    OTPPlus(
        value = otpValue,
        errorMessage = errorMessage,
        onValueChanged = { newValue ->
            otpValue = newValue
            errorMessage = null // Clear error when user types
        },
        onComplete = {
            // Validate OTP when 6 digits are entered
            if (otpValue != "123456") {
                errorMessage = UiText.DynamicString("Invalid OTP")
            }
        }
    )
}
```

### With Auto-Complete Callback

```kotlin
@Composable
fun OTPInputWithAutoComplete() {
    var otpValue by remember { mutableStateOf("") }

    OTPPlus(
        value = otpValue,
        onValueChanged = { newValue ->
            otpValue = newValue
        },
        onComplete = {
            // This callback is automatically called when 6 digits are entered
            // You can use this for auto-submit or immediate validation
            validateOTP(otpValue)
        }
    )
}
```

## Advanced Usage

### Focus Management

Use `FocusRequester` for programmatic focus control:

```kotlin
@Composable
fun OTPInputWithFocus() {
    var otpValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Column {
        OTPPlus(
            value = otpValue,
            focusRequester = focusRequester,
            onValueChanged = { newValue ->
                otpValue = newValue
            }
        )

        Button(onClick = { focusRequester.requestFocus() }) {
            Text("Focus OTP Input")
        }
    }
}
```

### Custom Spacing

Customize the spacing between OTP boxes:

```kotlin
@Composable
fun OTPInputWithCustomSpacing() {
    var otpValue by remember { mutableStateOf("") }

    OTPPlus(
        value = otpValue,
        innerSpace = 12.dp, // Default is 8.dp
        onValueChanged = { newValue ->
            otpValue = newValue
        }
    )
}
```

### Using String Resources

Use `UiText.StringResource` for internationalization:

```kotlin
@Composable
fun OTPInputWithStringResource() {
    var otpValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<UiText?>(null) }

    OTPPlus(
        value = otpValue,
        errorMessage = errorMessage,
        onValueChanged = { newValue ->
            otpValue = newValue
            errorMessage = null
        }
    )

    // Set error using string resource
    Button(onClick = {
        errorMessage = UiText.StringResource(R.string.otp_error_invalid)
    }) {
        Text("Validate")
    }
}
```

### Complete Example with Validation

```kotlin
@Composable
fun CompleteOTPExample() {
    var otpValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<UiText?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OTPPlus(
            value = otpValue,
            errorMessage = errorMessage,
            onValueChanged = { newValue ->
                otpValue = newValue
                errorMessage = null
                isSuccess = false
            },
            onComplete = {
                // Auto-validate when 6 digits are entered
                validateOTP(otpValue)
            }
        )

        if (isSuccess) {
            Text(
                text = "✓ OTP successfully validated!",
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = {
                when {
                    otpValue.isEmpty() -> {
                        errorMessage = UiText.DynamicString("Please enter OTP")
                    }
                    otpValue.length < 6 -> {
                        errorMessage = UiText.DynamicString("OTP must be 6 complete digits")
                    }
                    else -> {
                        validateOTP(otpValue)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit OTP")
        }
    }
}

fun validateOTP(otp: String) {
    // Your validation logic here
    if (otp == "123456") {
        isSuccess = true
        errorMessage = null
    } else {
        isSuccess = false
        errorMessage = UiText.DynamicString("Invalid OTP")
    }
}
```

## API Reference

### OTP+ Composable (`OTPPlus`)

```kotlin
@Composable
fun OTPPlus(
    modifier: Modifier = Modifier,
    value: String = "",
    errorMessage: UiText? = null,
    innerSpace: Dp = 8.dp,
    focusRequester: FocusRequester? = null,
    onValueChanged: (String) -> Unit = {},
    onComplete: (() -> Unit)? = null
)
```

#### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `modifier` | `Modifier` | `Modifier` | Modifier for customization of the OTP container |
| `value` | `String` | `""` | Current OTP value (should be 0-6 digits) |
| `errorMessage` | `UiText?` | `null` | Error message to display below the OTP fields (optional) |
| `innerSpace` | `Dp` | `8.dp` | Spacing between individual OTP digit boxes |
| `focusRequester` | `FocusRequester?` | `null` | FocusRequester for programmatic focus control (optional) |
| `onValueChanged` | `(String) -> Unit` | `{}` | Callback when the OTP value changes, providing the new value |
| `onComplete` | `(() -> Unit)?` | `null` | Callback when all 6 digits are entered (optional) |

### UiText

`UiText` is a sealed class for handling different types of UI text with i18n support:

```kotlin
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class Combined(val parts: List<UiText>) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()
    
    @Composable
    fun asString(): String
    
    fun asString(context: Context): String
}
```

#### Usage Examples

```kotlin
// Dynamic string
val error1 = UiText.DynamicString("Invalid OTP")

// String resource
val error2 = UiText.StringResource(R.string.otp_error)

// Combined
val error3 = UiText.Combined(
    listOf(
        UiText.StringResource(R.string.error_prefix),
        UiText.DynamicString("Invalid OTP")
    )
)
```

## Demo App

Try the OTP+ demo app directly on your device! The demo showcases various usage scenarios:

1. **Positive Scenario** - Successful OTP validation
2. **Error Scenario** - Error handling with shake animation
3. **Empty Validation** - Validation for empty or incomplete OTP
4. **Auto-Complete Callback** - Automatic callback when 6 digits are entered
5. **Focus Management** - Programmatic focus control
6. **Custom Spacing** - Customizable spacing between boxes

### 📱 Download from Play Store

<a href="https://play.google.com/store/apps/details?id=id.faazlab.otpplus.demo" target="_blank">
  <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80">
</a>

> **Note**: Update the Play Store link above with your actual published app URL.

### Screenshots

#### Main Demo Screen
![OTP+ Demo Main Screen](screenshots/demo-main-screen.png)

The demo app showcases all 6 usage scenarios in a clean, card-based interface.

#### Positive Scenario
![Positive Scenario](screenshots/demo-positive-scenario.png)

Successfully validated OTP with visual feedback and success message.

#### Error Scenario
![Error Scenario](screenshots/demo-error-scenario.png)

Error handling with shake animation and error message display.

#### Empty Validation
![Empty Validation](screenshots/demo-empty-validation.png)

Validation for empty or incomplete OTP input.

#### Auto-Complete Callback
![Auto-Complete Callback](screenshots/demo-auto-complete.png)

Automatic callback trigger when 6 digits are entered.

> **Note**: A demo video is also available showing the component in action. The video demonstrates all scenarios including animations and interactions.

### Running the Demo Locally

You can also build and run the demo app locally:

```bash
./gradlew :otpplus-demo:installDebug
```

Or install directly:

```bash
./gradlew :otpplus-demo:assembleDebug
adb install otpplus-demo/build/outputs/apk/debug/otpplus-demo-debug.apk
```

## Customization

### Colors

The library uses predefined colors in `OTPPlusColor` (internal implementation):

- `Error` - Error state color
- `Focused` - Focused box border color
- `BoxDefault` - Default box background
- `BoxFocused` - Focused box background
- `BorderDefault` - Default border color
- `TextDefault` - Text color

To customize colors, you can create your own color scheme or modify the library source.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

```
Copyright 2025 Erik Gunawan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Author

**Erik Gunawan**

- GitHub: [@erikgunawan](https://github.com/erikgunawan)
- Repository: [otp-plus](https://github.com/erikgunawan/otp-plus)

## Acknowledgments

- Built with [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Follows [Material Design 3](https://m3.material.io/) guidelines

---

⭐ If you find this library useful, please consider giving it a star!