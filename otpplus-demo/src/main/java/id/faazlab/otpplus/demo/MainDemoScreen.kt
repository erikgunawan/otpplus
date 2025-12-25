package id.faazlab.otpplus.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.faazlab.otpplus.OTPPlus
import id.faazlab.otpplus.UiText

/**
 * Created by erikgunawan on 25/12/25.
 *
 * Demo screen showcasing various usage scenarios of OTPPlus component:
 * 1. Positive scenario - OTP successfully validated
 * 2. Error scenario - Invalid OTP with shake animation
 * 3. Empty validation - Validation when submitting with empty field
 * 4. Auto-complete callback - Automatic trigger when 6 digits are entered
 * 5. Focus management - Programmatic focus control
 * 6. Custom spacing - Customize spacing between boxes
 */
@Composable
fun MainDemoScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Header
        Text(
            text = "OTPPlus Demo",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Various usage scenarios of OTP input component",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Divider()

        // Scenario 1: Positive Scenario
        PositiveScenarioCard()

        // Scenario 2: Error Scenario
        ErrorScenarioCard()

        // Scenario 3: Empty Validation
        EmptyValidationCard()

        // Scenario 4: Auto-complete Callback
        AutoCompleteScenarioCard()

        // Scenario 5: Focus Management
        FocusManagementCard()

        // Scenario 6: Custom Spacing
        CustomSpacingCard()
    }
}

/**
 * Scenario 1: Positive Scenario
 * Displays OTP input with successful validation
 */
@Composable
fun PositiveScenarioCard() {
    var otpValue by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "1. Positive Scenario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Enter OTP: 123456 to see success message",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OTPPlus(
                value = otpValue,
                onValueChanged = { newValue ->
                    otpValue = newValue
                    statusMessage = null
                    isSuccess = false
                },
                onComplete = {
                    // Simulate OTP validation
                    if (otpValue == "123456") {
                        statusMessage = "✓ OTP successfully validated!"
                        isSuccess = true
                    } else {
                        statusMessage = "Invalid OTP. Please try again."
                        isSuccess = false
                    }
                }
            )

            if (statusMessage != null) {
                Text(
                    text = statusMessage!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSuccess) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }

            Button(
                onClick = {
                    if (otpValue.length < 6) {
                        statusMessage = "Please enter 6 digit OTP"
                        isSuccess = false
                    } else if (otpValue == "123456") {
                        statusMessage = "✓ OTP successfully validated!"
                        isSuccess = true
                    } else {
                        statusMessage = "Invalid OTP. Please try again."
                        isSuccess = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit OTP")
            }
        }
    }
}

/**
 * Scenario 2: Error Scenario
 * Displays error message with shake animation
 */
@Composable
fun ErrorScenarioCard() {
    var otpValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<UiText?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "2. Error Scenario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Enter wrong OTP to see error message and shake animation",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OTPPlus(
                value = otpValue,
                errorMessage = errorMessage,
                onValueChanged = { newValue ->
                    otpValue = newValue
                    errorMessage = null // Clear error when user types
                },
                onComplete = {
                    // Simulate failed validation
                    if (otpValue != "123456") {
                        errorMessage = UiText.DynamicString("Invalid OTP. Please try again.")
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (otpValue.isEmpty()) {
                            errorMessage = UiText.DynamicString("OTP cannot be empty")
                        } else if (otpValue.length < 6) {
                            errorMessage = UiText.DynamicString("OTP must be 6 digits")
                        } else if (otpValue != "123456") {
                            errorMessage = UiText.DynamicString("Invalid OTP. Please try again.")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Trigger Error")
                }

                OutlinedButton(
                    onClick = {
                        errorMessage = null
                        otpValue = ""
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear")
                }
            }
        }
    }
}

/**
 * Scenario 3: Empty Validation
 * Validation when submitting with empty or incomplete field
 */
@Composable
fun EmptyValidationCard() {
    var otpValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<UiText?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "3. Empty Validation",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Try submitting without entering OTP or with incomplete OTP",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OTPPlus(
                value = otpValue,
                errorMessage = errorMessage,
                onValueChanged = { newValue ->
                    otpValue = newValue
                    if (isSubmitted) {
                        errorMessage = null // Clear error when user types after submission
                        isSubmitted = false
                    }
                }
            )

            Button(
                onClick = {
                    isSubmitted = true
                    when {
                        otpValue.isEmpty() -> {
                            errorMessage = UiText.DynamicString("Please enter OTP")
                        }
                        otpValue.length < 6 -> {
                            errorMessage = UiText.DynamicString("OTP must be 6 digits complete")
                        }
                        else -> {
                            errorMessage = null
                            // Process OTP
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}

/**
 * Scenario 4: Auto-complete Callback
 * Automatic trigger when 6 digits are entered
 */
@Composable
fun AutoCompleteScenarioCard() {
    var otpValue by remember { mutableStateOf("") }
    var autoCompleteTriggered by remember { mutableStateOf(false) }
    var triggerCount by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "4. Auto-complete Callback",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "onComplete callback will be automatically called when 6 digits are entered",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OTPPlus(
                value = otpValue,
                onValueChanged = { newValue ->
                    otpValue = newValue
                    autoCompleteTriggered = false
                },
                onComplete = {
                    // This callback is automatically called when value.length == 6
                    autoCompleteTriggered = true
                    triggerCount++
                    // Can be used for auto-submit or immediate validation
                }
            )

            if (autoCompleteTriggered) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✓ Auto-complete triggered!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Callback called $triggerCount time(s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    otpValue = ""
                    autoCompleteTriggered = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset")
            }
        }
    }
}

/**
 * Scenario 5: Focus Management
 * Programmatic focus control with FocusRequester
 */
@Composable
fun FocusManagementCard() {
    var otpValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "5. Focus Management",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Use FocusRequester for programmatic focus control",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OTPPlus(
                value = otpValue,
                focusRequester = focusRequester,
                onValueChanged = { newValue ->
                    otpValue = newValue
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        focusRequester.requestFocus()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Request Focus")
                }

                OutlinedButton(
                    onClick = {
                        otpValue = ""
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear")
                }
            }
        }
    }
}

/**
 * Scenario 6: Custom Spacing
 * Customize spacing between OTP boxes
 */
@Composable
fun CustomSpacingCard() {
    var otpValue by remember { mutableStateOf("") }
    var spacing by remember { mutableIntStateOf(8) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "6. Custom Spacing",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Customize spacing between OTP boxes with innerSpace parameter",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OTPPlus(
                value = otpValue,
                innerSpace = spacing.dp,
                onValueChanged = { newValue ->
                    otpValue = newValue
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Spacing: ${spacing}dp",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = {
                        spacing = when (spacing) {
                            4 -> 8
                            8 -> 12
                            12 -> 16
                            else -> 4
                        }
                    }
                ) {
                    Text("Change Spacing")
                }
            }

            OutlinedButton(
                onClick = {
                    otpValue = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear")
            }
        }
    }
}