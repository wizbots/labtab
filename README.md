# labtab

Labtab will be an application for Wizbots mentors for Android tablets and smartphones to be used for managing kids at the Robotics labs, tracking their progress and promoting them and also to shoot videos for each project completed by kids during a lab. 

The videos recorded should be uploaded when good internet connectivity is detected, since most of the times there is none at Schools and centers where the labs take place. Thus, an offline first approach must be used right from the start.


## Setup

Basically all you need to get up and running is to download Android Studio and import the project.

Here are details on how to do this on Linux 64bits (Ubuntu)

### Installation instructions Ubuntu 64 bit

* Download [Android Studio for Ubuntu 64bit](https://developer.android.com/studio/index.html#linux-bundle)
* If not installed yet, install openjdk-8

```
sudo apt-get update
sudo apt-get install openjdk-8
```

* Unzip the folder containing Android Studio, probalby in your home
  directory and start Android Studio

```
cd
unzip android-studio*.zip
cd android-studio
sh bin/studio.sh
```

* This will start Android Studio, just follow through the general questions until you get a point where you get asked to import a project.

* You can choose to use Github directly or if you have cloned this project already, use the project browser.

* The project uses Gradle, that Android Studio will detect and install all that is required, although sometimes you might see some pending stuff
regarding the Android SDK version XX, just download them and restart Android Studio.

* Once Gradle finishies building you're good to go! Now time to create a virtual device.

### Using AVD to create a virtual device where Labtab will be executed

* Go to Tools-> Android -> AVD Manager
* Select Create Virtual Device
* Select Nexus 10 from tablets section
* Leave all paramenters as they are
* When the device is created click on Play icon to start it
* This will take some time, just wait until home screen is shown

### Launching LabTab in virtual device

* Now that the virtual device is running, simply click Run-> Run App and
Android Studio will ask you in what device it should be launched, select the one
we just created
* This will take some time, depending also in your machine specs, but hopefully you will
be able to use Labtab in this device! Hooray!


## Testing

This is still to be determined

## Releases

There a release-notes.txt files that contains the information about releases, please check it out. We do a release everyweek, generally on Friday.






