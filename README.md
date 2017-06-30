# SMS Group Sender

[![CircleCI](https://img.shields.io/circleci/project/bertrandmartel/sms-group-sender.svg?maxAge=2592000?style=plastic)](https://circleci.com/gh/bertrandmartel/hci-debugger)
[![License](http://img.shields.io/:license-mit-blue.svg)](LICENSE.md)

SMS sender Android application used to define your own group of contacts and store messages designed to be sent to one or multiple groups. The messages have a topic field. All messages belonging to the same topic can be sent to one or multiple groups.

This application features : 

* creation of groups of contacts
* creation of messages with a unique title and a topic, several messages can have same topic
* creation of sending configurations to send one or many messages from the same topic to 1 or many groups of contacts
* possibility to send messages with 1-to-1 mode : 1 message for each contacts until no more message are left to be sent

[![Download SMS Sender Group from Google Play](http://www.android.com/images/brand/android_app_on_play_large.png)](https://play.google.com/store/apps/details?id=fr.bmartel.groupsms)

## Build

```
git clone git@github.com:bertrandmartel/sms-group-sender.git
cd sms-group-sender
./gradlew clean build
```

## External libraries

* [BottomBar](https://github.com/roughike/BottomBar)
* [GSON](https://github.com/google/gson)

## License

The MIT License (MIT) Copyright (c) 2017 Bertrand Martel
