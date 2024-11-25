# Android Biometric Authentication Example

This repository demonstrates how to implement **Biometric Authentication** in an Android application using **Jetpack Compose**. It showcases the integration of the [`BiometricPrompt`](https://developer.android.com/reference/androidx/biometric/package-summary) API with Compose to provide a modern, user-friendly way to secure access to your app.

## Overview

Biometric authentication allows users to log in using fingerprint, face, or other biometric data. This example uses the `BiometricPrompt` API provided by Android, along with Jetpack Compose for building the UI.

The project includes:
- A Jetpack Compose UI for displaying an authentication button.
- A helper function to handle biometric authentication using `BiometricPrompt`.
- Integration that works seamlessly with `FragmentActivity` and Compose.

## Features

- Detects if biometric authentication is available on the device.
- Prompts users to authenticate using their biometric credentials.
- Displays the result of authentication (success or failure) in the UI.

## Prerequisites

- Android Studio Giraffe or later.
- A device or emulator with biometric hardware (e.g., fingerprint scanner or face recognition).
- Minimum SDK level: 23 (Android 6.0).

## Setup

1. Clone this repository:
   ```bash
   git clone https://github.com/pranaypatel512/BiometricAuthentication.git
   ```
2. Open the project in Android Studio.
3. Build and run the project on a compatible device or emulator.

## How It Works
`. The app checks if biometric authentication is available on the device using the BiometricManager API.
2. When the "Authenticate" button is clicked:
 * A BiometricPrompt dialog appears.
 * Users authenticate using their biometric credentials.
3. The authentication result is displayed on the screen.

## Blog Post
For a detailed explanation of the implementation, check out the Medium Blog Post (to be published).

## Code Highlights
1. Composable UI: A simple Compose UI for displaying the authentication status.
2. BiometricPrompt Integration: A helper function to configure and display the biometric prompt.
3. Fragment Compatibility: Ensures compatibility with FragmentActivity for biometric authentication.

### Find this project useful ? ❤️

- Support it by clicking the ⭐️ button on the upper right of this page. ✌️
