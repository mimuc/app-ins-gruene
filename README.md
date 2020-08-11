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


### Which branch should I use?
- The final version is on the FinalVersion branch. Pls use this branch ;) ("Delevop" was used during the SoSe20, but is not up to date.)

## How to run tests?

```
./gradlew clean test
```


## How to Tag and Release the projectß

 We release our Build and APK with [Gradle release plugin](https://github.com/researchgate/gradle-release).
 Release Call for specific Version is:
 
```
  ./gradlew clean release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=1.0.0 -Prelease.newVersion=1.1.0-SNAPSHOT
```

 Release Call for Standard Version Increase is:
```
  ./gradlew clean release
```

If you just want to build an unsigned APK File, call:
```
  ./gradlew clean assemble
```

## Built With

* [Gradle](https://gradle.org/) 

## Versioning

We use [Git](http://git.org/) for versioning.

## Authors

**The Design Workshop II Team SS2019 from Ludwig-Maximilians-University**
**SoSe20: Lisa Görtz (lisagoertz95@gmx.de) and Cara Emberger**

## Notes

* This README is written in [Markdown](https://about.gitlab.com/handbook/product/technical-writing/markdown-guide/)
