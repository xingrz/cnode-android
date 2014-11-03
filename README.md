CNode Android
==========

[![build status][travis-image]][travis-url] [![latest release][release-image]][release-url] [![issues][issues-image]][issues-url]

[travis-image]: https://img.shields.io/travis/xingrz/cnode-android.svg?style=flat-square
[travis-url]: https://travis-ci.org/xingrz/cnode-android
[release-image]: https://img.shields.io/github/release/xingrz/cnode-android.svg?style=flat-square
[release-url]: https://github.com/xingrz/cnode-android/releases/latest
[issues-image]: https://img.shields.io/github/issues/xingrz/cnode-android.svg?style=flat-square
[issues-url]: https://github.com/xingrz/cnode-android/issues

A [CNode](https://cnodejs.org) client for Android.


## Prerequisites

The following is required for developing:

- Oracle JDK 7 (8 is recommended)
- Android Studio 0.9+ (canary build)

The following SDK components is required for building:

- Android API Platform 21
- Android SDK Build-tools 21.1
- Android Support Repository

You may install these SDK components by:

```sh
echo y | android update sdk --no-ui --all --filter "tools"
echo y | android update sdk --no-ui --all --filter "build-tools-21.1.0"
echo y | android update sdk --no-ui --all --filter "android-21"
echo y | android update sdk --no-ui --all --filter "extra-android-m2repository"
```


## Build

Build and generate a debug APK in `./CNode/build/outputs/apk`:

```sh
./gradlew assembleDebug
```


## License

The [MIT License](LICENSE).
