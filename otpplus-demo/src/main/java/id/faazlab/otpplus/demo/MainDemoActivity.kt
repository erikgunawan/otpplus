package id.faazlab.otpplus.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import id.faazlab.otpplus.demo.ui.theme.OTPPlusDemoTheme

/**
 * Created by erikgunawan on 25/12/25.
 *
 * Main Activity for OTPPlus library demo showcasing various usage scenarios.
 */
class MainDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OTPPlusDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainDemoScreen()
                }
            }
        }
    }
}