# Tree App 

This Android Project is used to manage the source code of a biodidactic tree app.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
See deployment for notes on how to deploy the project on a live system.

### What do you need to run the project?

- [Android Studio](https://developer.android.com/studio)
- [Git](https://git-scm.com/)

### How to get the project?

- Open your terminal/cmd & switch to the directory you want to download your project: 
```
cd path/to/your/directory
```
- Clone the project: 
```
git clone https://gitlab.lrz.de/lmu-design-workshop/tree-app-android.git
```
- Now you are able to open the project in Android Studio.

### How to contribute?

- Create an [issue](https://gitlab.com/gitlab-org/gitlab/-/issues), so we can discuss and monitor the topic.
- You can check and manage the state of your issues in the [Issue-Board](https://gitlab.lrz.de/lmu-design-workshop/tree-app-android/-/boards). 
- Use the branch `master` as basis for a new branch `git checkout 123-my-issue`, where `123` is the issue number. Don't forget to update your master before checkout with `git pull origin master`.
- Make your changes, consider formatting them with your IDE (e.g. `Ctrl+Alt+L` in IntelliJ / AndroidStudio).
- Commit your changes, consider [cross-linking](https://docs.gitlab.com/ee/user/project/issues/crosslinking_issues.html) the resolved issue with `(closes #123)` in your commit message.
- Create a Merge-Request for your branch (also consider a cross-link to an existing issue).

You may want to use the Version-Control-System (VCS) [of IntelliJ](https://www.jetbrains.com/help/idea/version-control-integration.html), to easier track and compare your changes.

### How to access the database?

The prepopulated database is stored in `tree-app-android/app/src/main/assets/databases/content.db`. You can access and edit it with every database tool for SQLite.

We recommend one of the following tools:
- [Database Navigator](https://plugins.jetbrains.com/plugin/1800-database-navigator): **Android Studio** → Settings (`Ctrl + Alt + S`) → Plugins → Marketplace → Database Navigator
   - Menu Bar → DB Navigator → Database Browser
   - Create new SQLite connection to the above database path _(if necessary)_
   - Schemas → main → Tables → _double click on your table_ → `No Filter` _(if necessary)_
- [Database Tools and SQL](https://www.jetbrains.com/help/idea/relational-databases.html): **IntelliJ Ultimate** → Settings (`Ctrl + Alt + S`) → Plugins → Marketplace → Database Tools and SQL _(see also the [documentation](https://www.jetbrains.com/help/idea/accessing-android-sqllite-databases-from-product.html))_
- [Database Inspector](https://developer.android.com/studio/inspect/database) _(only for debugging)_: **Android Studio** → Menu Bar → View → Tool Windows → Database Inspector

## How to run tests?

```
./gradlew clean test
```

Detect lint issues:

```
./gradlew lint
```

## How to Tag and Release the project

### Release in Git

 We release our Build and APK with [Gradle release plugin](https://github.com/researchgate/gradle-release).
 Release call for specific version, where `releaseVersion` is the current release version and `newVersion` will be the next version:
 
```
  ./gradlew clean release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=0.1.0-alpha.0 -Prelease.newVersion=0.1.1-SNAPSHOT
```

 Release Call for Standard Version Increase is:
```
  ./gradlew clean release
```

If you just want to build an unsigned APK File, call:
```
  ./gradlew clean assemble
```

### Release in Playstore

Read the following docs:
- [Publish your app](https://developer.android.com/studio/publish)
- [Prepare for release](https://developer.android.com/studio/publish/preparing)
- [Core app quality](https://developer.android.com/docs/quality-guidelines/core-app-quality)
- [Launch checklist](https://developer.android.com/distribute/best-practices/launch/launch-checklist)

## Authors

***
- SoSe19: Practical Course [Design Workshop II](http://www.medien.ifi.lmu.de/lehre/ss19/dw2/)
- SoSe20: [Lisa Görtz](lisagoertz95@gmx.de) and Cara Emberger
- WiSe20: Practical Course [Development of Media Systems III](https://www.medien.ifi.lmu.de/lehre/ws2021/pem3/)
***

## Notes

- Built with [Gradle](https://gradle.org/) 
- Versioning with [Git](http://git.org/)
- Documentation written in [Markdown](https://about.gitlab.com/handbook/product/technical-writing/markdown-guide/)
