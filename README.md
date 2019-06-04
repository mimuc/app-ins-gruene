# Tree App 

This Android Project is used to manage the source code of a biodidactic tree app.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

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


## Running the tests

Explain how to run the automated tests for this system


## Deployment

 We release our apk with [Gradle release plugin](https://github.com/researchgate/gradle-release).
 Standard gradle release call is:

```
  gradle release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=1.0.0 -Prelease.newVersion=1.1.0-SNAPSHOT
```

## Built With

* [Gradle](https://gradle.org/) 


## Versioning

We use [Git](http://git.org/) for versioning.

## Authors

**The Design Workshop II Team SS2019 from Ludwig-Maximilians-University**

## Notes

* This README is written in [Markdown](https://about.gitlab.com/handbook/product/technical-writing/markdown-guide/)
