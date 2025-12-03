# Bolt Expo Native App

An Expo Router application configured for Android builds via GitHub Actions.

## Prerequisites

- Node.js 20 or later
- npm or yarn
- Expo account (free at [expo.dev](https://expo.dev))
- EAS CLI (installed automatically via npm scripts)

## Local Development

### Install Dependencies

```bash
npm install
```

### Run Development Server

```bash
npm run dev
```

### Platform-Specific Development

```bash
npm run android  # Run on Android device/emulator
npm run ios      # Run on iOS device/simulator
npm run web      # Run in web browser
```

## GitHub Setup for Android Builds

### 1. Create an Expo Account

1. Go to [expo.dev](https://expo.dev) and create a free account
2. Note your username (you'll need it later)

### 2. Get Your EAS Project ID

Run this command in your project directory:

```bash
npx eas-cli init
```

This will:
- Create an EAS project
- Update your `app.json` with the project ID

### 3. Generate an Expo Access Token

1. Go to [expo.dev/accounts/[your-username]/settings/access-tokens](https://expo.dev/settings/access-tokens)
2. Click "Create Token"
3. Give it a name like "GitHub Actions"
4. Copy the token (you won't see it again!)

### 4. Add Secret to GitHub

1. Go to your GitHub repository
2. Navigate to: Settings > Secrets and variables > Actions
3. Click "New repository secret"
4. Name: `EXPO_TOKEN`
5. Value: Paste your Expo access token
6. Click "Add secret"

### 5. Update Package Name (Optional)

In `app.json`, update the Android package name if desired:

```json
"android": {
  "package": "com.yourcompany.yourapp"
}
```

## Building Android APK

### Manual Build (Local)

```bash
npm run build:android:preview
```

### Automatic Build (GitHub Actions)

Builds trigger automatically on:
- Push to `main` or `master` branch
- Pull requests to `main` or `master` branch
- Manual workflow dispatch

To manually trigger a build:
1. Go to your GitHub repository
2. Click "Actions" tab
3. Select "Build Android APK" workflow
4. Click "Run workflow"

### Checking Build Status

1. Visit [expo.dev](https://expo.dev)
2. Go to your project
3. Click "Builds" to see build status and download APK

## Project Structure

```
.
├── app/                    # App routes (Expo Router)
│   ├── _layout.tsx        # Root layout
│   ├── +not-found.tsx     # 404 page
│   └── (tabs)/            # Tab-based navigation
├── assets/                # Images and static files
├── components/            # Reusable components
├── hooks/                 # Custom React hooks
├── .github/
│   └── workflows/         # GitHub Actions workflows
│       ├── android-build.yml  # Android build workflow
│       └── ci.yml            # Linting and type checking
├── app.json              # Expo configuration
├── eas.json              # EAS Build configuration
└── package.json          # Dependencies and scripts
```

## Available Scripts

- `npm run dev` - Start development server
- `npm run android` - Run on Android
- `npm run ios` - Run on iOS
- `npm run web` - Run in browser
- `npm run build:android` - Build Android APK (production)
- `npm run build:android:preview` - Build Android APK (preview)
- `npm run lint` - Run ESLint
- `npm run typecheck` - Run TypeScript type checking

## Configuration Files

### app.json
Main Expo configuration including:
- App name, version, and metadata
- Platform-specific settings (Android/iOS)
- Plugin configuration

### eas.json
EAS Build profiles:
- `development` - Development builds with dev client
- `preview` - Preview builds (APK for easy distribution)
- `production` - Production builds for app stores

## Troubleshooting

### Build Fails with "EXPO_TOKEN not found"
- Verify you added the `EXPO_TOKEN` secret in GitHub repository settings
- Make sure the secret name is exactly `EXPO_TOKEN`

### Build Fails with "Project not configured"
- Run `npx eas-cli init` locally to set up EAS
- Commit and push the updated `app.json` with the project ID

### "Package name already in use"
- Change the `android.package` in `app.json` to a unique identifier
- Use reverse domain notation: `com.yourcompany.yourapp`

### Local build works but GitHub Action fails
- Ensure all dependencies are listed in `package.json`
- Check the GitHub Actions logs for specific error messages
- Verify your Expo account has sufficient build credits

## Resources

- [Expo Documentation](https://docs.expo.dev/)
- [EAS Build Documentation](https://docs.expo.dev/build/introduction/)
- [Expo Router Documentation](https://docs.expo.dev/router/introduction/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

## License

This project is private and proprietary.
