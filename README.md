# Jûrnl

Your one stop shop for all your bullet journal needs.  No longer will you be burdened with keeping up with a physical journal, Jûrnl will be with you wherever you go.  You can quickly and easily create new entries, track habits and mood, and set reminders for important events.  By prompting you with notifications, you will never miss an event or break your habit streak.  Conveniently organize and search through your notes by assigning entries with people, places and tags.  Jûrnl also give you access to useful analytics of your habits and mood to track your weekly and monthly progression.

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone https://github.com/jdaughd2/j-rnl.git
```

## Configuration
### Keystores:
Create `app/keystore.gradle` with the following info:
```gradle
ext.key_alias='...'
ext.key_password='...'
ext.store_password='...'
```
And place both keystores under `app/keystores/` directory:
- `playstore.keystore`
- `stage.keystore`


## Build variants
Use the Android Studio *Build Variants* button to choose between **production** and **staging** flavors combined with debug and release build types


## Generating signed APK
From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*

## Maintainers
This project is mantained by:
* [Joseph Daughdrill](http://github.com/jdaughd2)


## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Run the linter (ruby lint.rb').
5. Push your branch (git push origin my-new-feature)
6. Create a new Pull Request
