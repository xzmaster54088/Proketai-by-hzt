# PocketDev - AI-Powered Code Generation App

PocketDev is an Android application that allows developers to generate code using AI models (OpenAI, DeepSeek, Ollama) and automatically push the generated code to GitHub repositories.

## Features

### 🚀 Core Features
- **Dynamic AI Model Support**: Switch between OpenAI, DeepSeek, and local Ollama instances
- **JSON-Powered Code Generation**: AI responses are structured JSON for reliable parsing
- **GitHub Integration**: OAuth authentication and automatic code pushing
- **Modern Android Architecture**: MVVM + Hilt + Jetpack Compose
- **Material Design 3**: Beautiful, modern UI with dynamic colors

### 🔧 Technical Stack
- **Language**: Kotlin
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **UI**: Jetpack Compose + Material 3
- **Networking**: Retrofit + OkHttp + Moshi
- **Storage**: DataStore (EncryptedSharedPreferences)
- **Navigation**: Compose Navigation

## Project Structure

```
app/src/main/java/com/pocketdev/
├── data/                    # Data layer
│   ├── repository/          # Data repositories
│   ├── remote/              # Network layer
│   │   ├── interceptor/     # DynamicHostInterceptor
│   │   ├── service/         # API services
│   │   └── model/           # Network models
│   └── local/               # Local data sources
├── domain/                  # Domain layer
│   ├── model/               # Domain models
│   └── usecase/             # Use cases
├── ui/                      # UI layer
│   ├── screens/             # Composable screens
│   ├── components/          # Reusable components
│   ├── viewmodel/           # ViewModels
│   └── theme/               # Theme and styling
└── di/                      # Dependency injection
```

## Setup Instructions

### 1. Prerequisites
- Android Studio Hedgehog or later
- Android SDK 34
- Java 17

### 2. API Configuration

#### OpenAI Setup
1. Get your API key from [OpenAI Platform](https://platform.openai.com/)
2. In the app, go to Settings
3. Set Base URL: `https://api.openai.com`
4. Enter your API key
5. Set Model Name: `gpt-3.5-turbo` or `gpt-4`

#### DeepSeek Setup
1. Get your API key from [DeepSeek Platform](https://platform.deepseek.com/)
2. Set Base URL: `https://api.deepseek.com`
3. Enter your API key
4. Set Model Name: `deepseek-chat`

#### Ollama Setup (Local)
1. Install [Ollama](https://ollama.ai/) on your machine
2. Pull a model: `ollama pull llama2`
3. For Android emulator, use: `http://10.0.2.2:11434`
4. For physical device, use your computer's IP address
5. Set Model Name: `llama2` or your preferred model

#### GitHub OAuth Setup
1. Create a GitHub OAuth App:
   - Go to GitHub Settings → Developer settings → OAuth Apps
   - Create New OAuth App
   - Application name: `PocketDev`
   - Homepage URL: `https://your-domain.com` (can be placeholder)
   - Authorization callback URL: `pocketdev://oauth`
2. Note the Client ID and Client Secret
3. Update `GithubAuthManager.kt` with your credentials

### 3. Security Considerations

#### API Key Storage
- API keys are stored using EncryptedSharedPreferences via DataStore
- Never commit actual API keys to version control
- Use environment variables or secure configuration for production

#### Network Security
- All network requests use HTTPS
- DynamicHostInterceptor securely handles API key injection
- GitHub OAuth uses Custom Tabs for secure authentication

## Usage

### 1. Configure AI Settings
- Open the app and navigate to Settings
- Configure your preferred AI provider (OpenAI, DeepSeek, or Ollama)
- Save the configuration

### 2. Connect GitHub
- Go to Settings → GitHub Integration
- Click "Connect GitHub Account"
- Authorize the app through GitHub OAuth

### 3. Generate Code
- In the main chat screen, describe what code you want to generate
- Examples:
  - "Create a Kotlin function to sort a list of integers"
  - "Generate a React component for a user profile card"
  - "Write a Python script to read a CSV file"
- The AI will respond with structured JSON containing:
  - Explanation (Markdown format)
  - Code actions (file operations)

### 4. Push to GitHub
- Review the generated code in the Code Cards
- Click "Push to GitHub" to automatically commit the code
- The app will create or update files in your repository

## System Prompt

The AI is configured with a strict system prompt that enforces JSON responses:

```json
{
  "explanation": "Markdown explanation for the user",
  "actions": [
    {
      "fileName": "path/to/file.ext",
      "codeContent": "Actual code content",
      "commitMessage": "Descriptive commit message"
    }
  ]
}
```

## Development

### Building the Project

#### Using Gradle Wrapper
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK and AAB
./gradlew assembleRelease
./gradlew bundleRelease

# Run tests
./gradlew test

# Run lint checks
./gradlew lintDebug
```

#### Using Build Script (Recommended)
```bash
# Make the script executable
chmod +x scripts/build.sh

# Build debug APK
./scripts/build.sh debug

# Build release versions
./scripts/build.sh release

# Run tests and lint
./scripts/build.sh all

# Clean project
./scripts/build.sh clean
```

### Continuous Integration

The project uses GitHub Actions for CI/CD with two workflows:

#### 1. Android CI (`android-ci.yml`)
- **Triggers**: On push to main/develop branches and pull requests
- **Jobs**:
  - Build: Compiles debug APK and runs tests
  - Lint: Runs Android lint checks
- **Artifacts**: Uploads debug APK and lint results

#### 2. Android Release (`android-release.yml`)
- **Triggers**: On version tags (v*) or manual dispatch
- **Jobs**: Builds release APK and AAB, creates GitHub release
- **Artifacts**: Uploads release APK and AAB to GitHub Releases

### Code Style
- Follow Kotlin coding conventions
- Use ktlint for code formatting
- Write unit tests for ViewModels and UseCases

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Troubleshooting

### Common Issues

1. **Network Errors**
   - Check your API key and base URL
   - Verify internet connectivity
   - For Ollama, ensure the service is running

2. **GitHub Push Failures**
   - Verify OAuth permissions include "repo" scope
   - Check repository access permissions
   - Ensure the file path doesn't conflict with existing files

3. **JSON Parsing Errors**
   - The AI might not follow the JSON format strictly
   - Check the system prompt configuration
   - The app includes fallback handling for malformed responses

### Debug Mode
Enable debug logging in `NetworkModule.kt` to see network requests and responses.
