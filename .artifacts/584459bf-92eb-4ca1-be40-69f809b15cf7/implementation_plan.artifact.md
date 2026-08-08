# Implementation Plan - FriendlyLines Tablet App

Create a tablet-optimized application with two separate entry points (Therapist and Child) that share data, using Jetpack Compose and supporting Polish/English localization.

## User Review Required

> [!IMPORTANT]
> The app will feature two separate launcher icons on the device home screen: one for the "Therapist" and one for the "Child". Both will be part of the same installed package to facilitate seamless data exchange.

## Proposed Changes

### Configuration & Resources

#### [MODIFY] [build.gradle.kts](file:///C:/Users/hania/AndroidStudioProjects/FriendlyLines/app/build.gradle.kts)
- Add Jetpack Compose dependencies and enable Compose.
- Ensure landscape orientation is handled.

#### [MODIFY] [strings.xml](file:///C:/Users/hania/AndroidStudioProjects/FriendlyLines/app/src/main/res/values/strings.xml)
- Define Polish strings as the default.

#### [NEW] [strings.xml (en)](file:///C:/Users/hania/AndroidStudioProjects/FriendlyLines/app/src/main/res/values-en/strings.xml)
- Define English translations.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/hania/AndroidStudioProjects/FriendlyLines/app/src/main/AndroidManifest.xml)
- Define two Activities (`TherapistActivity`, `ChildActivity`) both with `LAUNCHER` intent filters.
- Force `landscape` orientation for both.

### Core Logic & UI

#### [NEW] [DataRepository.kt](file:///C:/Users/hania/AndroidStudioProjects/FriendlyLines/app/src/main/java/com/example/friendlylines/data/DataRepository.kt)
- A shared repository to handle data exchange between the two modules.

#### [NEW] [ChildActivity.kt](file:///C:/Users/hania/AndroidStudioProjects/FriendlyLines/app/src/main/java/com/example/friendlylines/ChildActivity.kt)
- The main entry point for the child app.

#### [NEW] [TherapistActivity.kt](file:///C:/Users/hania/AndroidStudioProjects/FriendlyLines/app/src/main/java/com/example/friendlylines/TherapistActivity.kt)
- The main entry point for the therapist app.

#### [NEW] [ChildScreen.kt](file:///C:/Users/hania/AndroidStudioProjects/FriendlyLines/app/src/main/java/com/example/friendlylines/ui/ChildScreen.kt)
- Composable implementing the UI from the provided image.

## Verification Plan

### Automated Tests
- Build the project to ensure all dependencies are correct.

### Manual Verification
- Deploy to a tablet (or tablet emulator).
- Verify two icons appear in the launcher.
- Verify Child screen matches the provided image.
- Change device language to English and verify strings update.
- Verify the app stays in landscape mode.
