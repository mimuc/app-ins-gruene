# Deployment

## Tag and Release in Git

First of all increase the app `versionCode` in `build.gradle` (app).

We release our Build and APK with [Gradle release plugin](https://github.com/researchgate/gradle-release).

Release Call for Standard Version Increase is (use this in default release process with GitHub Actions):
```
  ./gradlew clean release -Prelease.releaseVersion=0.1.0-alpha.1
```

Release call for specific version, where `releaseVersion` is the current release version and `newVersion` will be the next version:

```
  ./gradlew clean release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=0.1.0-alpha.1 -Prelease.newVersion=0.1.1-SNAPSHOT
```

If you just want to build an unsigned APK File, call:
```
  ./gradlew clean assemble
```

## Release in Google Play

### Manual

Read the following docs:
- [Publish your app](https://developer.android.com/studio/publish)
- [Prepare for release](https://developer.android.com/studio/publish/preparing)
- [Core app quality](https://developer.android.com/docs/quality-guidelines/core-app-quality)
- [Launch checklist](https://developer.android.com/distribute/best-practices/launch/launch-checklist)

OPTIONALLY replace the resources used in the release:
- Add the "Freude"-Font to `app/src/main/res/font`.
- Change the path in `app/src/main/res/font/main.xml` accordingly.
- Untrack the file changes: `git update-index --assume-unchanged app/src/main/res/font/main.xml`.

### GitHub Actions

Once you upload the Tag in Git, the App will automatically released in the Google Play Store via [GitHub Actions](.github/workflows/android-release.yml).
For updating the SECRETS read the docs of [r0adkll/sign-android-release](https://github.com/r0adkll/sign-android-release) and [r0adkll/upload-google-play](https://github.com/r0adkll/upload-google-play).

We store licensed resources for the release [separately and encrypted](https://docs.github.com/en/actions/reference/encrypted-secrets#limits-for-secrets) in the docs folder.
