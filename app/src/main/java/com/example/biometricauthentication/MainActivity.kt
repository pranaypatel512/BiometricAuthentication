package com.example.biometricauthentication

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.biometricauthentication.ui.theme.BiometricAuthenticationTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiometricAuthenticationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BiometricAuthenticationDemo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Log.d("BiometricAuth", "No biometric hardware available.")
            false
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Log.d("BiometricAuth", "Biometric hardware unavailable.")
            false
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Log.d("BiometricAuth", "No biometrics enrolled.")
            false
        }

        else -> false
    }
}

@Composable
fun BiometricAuthenticationDemo(modifier: Modifier) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity // Safe cast to FragmentActivity


    var authenticationResult by remember { mutableStateOf("Not Authenticated") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Biometric Authentication",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (activity != null) {
                if (isBiometricAvailable(context)) {
                    promptEnrollBiometric(context)
                    /*showBiometricPrompt(
                        activity = activity,
                        onSuccess = { authenticationResult = "Authentication Succeeded!" },
                        onFailure = { authenticationResult = "Authentication Failed!" }
                    )*/
                } else {
                    promptEnrollBiometric(context)
                    authenticationResult = "Biometric not available or not configured."
                }
            }
        }) {
            Text(text = "Authenticate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = authenticationResult,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Prompt the user to enroll biometric credentials.
 */
fun promptEnrollBiometric(context: Context) {
    try {
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            )
        }
        if (context is FragmentActivity) {
            context.startActivity(enrollIntent)
        }
    } catch (e: ActivityNotFoundException) {
        // Fallback to a more generic settings screen
        val fallbackIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
        if (context is FragmentActivity) {
            context.startActivity(fallbackIntent)
        }
    }
}

fun showBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)

    val biometricPrompt =
        BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("BiometricAuth", "Error: $errString")
                onFailure()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("BiometricAuth", "Authentication succeeded!")
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BiometricAuth", "Authentication failed.")
                onFailure()
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Log in using your biometric credentials")
        .setNegativeButtonText("Cancel")
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        .build()

    biometricPrompt.authenticate(promptInfo)
}


