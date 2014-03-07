# StudGent (Groep 12)

[![Build Status](https://travis-ci.org/studgent/android.png?branch=master)](https://travis-ci.org/studgent/android) 
[![Coverage Status](https://coveralls.io/repos/studgent/android/badge.png)](https://coveralls.io/r/studgent/android) 

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
* Crashlytics

#### Android libraries

Most of the libraries are managed through Gradle.

* Gson 2.2.4 (parse JSON, stringify to JSON)
* Mixare (Augmented Reality helper library)
* Crashlytics (Crash reporter to web service)

Execute `./gradlew` to assemble and retrieve the libraries. (Windows users can execute `gradlew.bat`)

### Crashlytics

Used to manage crashlogs, uploads crashes to crashlytics.com.

* Install in Eclipse: go to *Help* > *Install Software*
	* In download URL, type: [https://crashlytics.com/download/eclipse](https://crashlytics.com/download/eclipse)
* Install in Android Studio: Download from [https://crashlytics.com/download/androidstudio](https://crashlytics.com/download/androidstudio)
	* Install using *File* > *Settings* (Unix & Windows) or *Preferences* (Mac)

**Crashlytics API key** must be stored in a string with name `CRASHLYTICS_KEY` in *res/values/apikeys.xml*


