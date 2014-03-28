# StudGent (Groep 12)

[![MIT License](http://f.cl.ly/items/3l42230F0I0s183x2j0v/mit.png)](http://ahluntang.mit-license.org/)
[![Build Status](https://travis-ci.org/studgent/android.png?branch=master)](https://travis-ci.org/studgent/android) 
[![Coverage Status](https://coveralls.io/repos/studgent/android/badge.png)](https://coveralls.io/r/studgent/android) 
[![Analytics](https://ga-beacon.appspot.com/UA-8128205-6/studgent/android/README.md)](https://github.com/studgent/backend)

[![Ideas](https://badge.waffle.io/studgent/android.png?label=Idea&title=Idea)](https://github.com/studgent/android/issues?labels=Idea) 
[![Feature](https://badge.waffle.io/studgent/android.png?label=Feature&title=Feature)](https://github.com/studgent/android/issues?labels=Feature)
[![In progress](https://badge.waffle.io/studgent/android.png?label=In+progress&title=In+progress)](https://github.com/studgent/android/issues?labels=In+progress)
[![More work needed](https://badge.waffle.io/studgent/android.png?label=More+work+needed&title=More+work+needed)](https://github.com/studgent/android/issues?labels=More+work+needed)
[![To Fix](https://badge.waffle.io/studgent/android.png?label=To+Fix&title=To+Fix)](https://github.com/studgent/android/issues?labels=To+Fix)

## Project info

Interactive Cityguide mainly aimed for students in Ghent. 

## Setup environment
### Requirements

* Android Developer Tools (Eclipse, Android SDK)
* Gradle (included in repository)
* Crashlytics (added library in repository)
* Google Play Services (import from Android SDK in your workspace)

### Android libraries

Most of the libraries are managed through Gradle.

* Google Play Services
* Crashlytics (Crash reporter to web service)

Execute `./gradlew` to assemble and retrieve the libraries. (Windows users can execute `gradlew.bat`)


### Crashlytics

Used to manage crashlogs, uploads crashes to crashlytics.com.

* Install in Eclipse: go to *Help* > *Install Software*
	* In download URL, type: [https://crashlytics.com/download/eclipse](https://crashlytics.com/download/eclipse)
* Install in Android Studio: Download from [https://crashlytics.com/download/androidstudio](https://crashlytics.com/download/androidstudio)
	* Install using *File* > *Settings* (Unix & Windows) or *Preferences* (Mac)

**Crashlytics API key** must be stored in a string with name `CRASHLYTICS_KEY` in *res/values/apikeys.xml*

### Google Maps

Google Maps API v2 requires an API key, this key can be stored as string `GOOGLE_MAPS_KEY` in *res/values/apikeys.xml*
