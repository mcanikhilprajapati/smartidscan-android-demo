# SmartID Scan Demo Android

SmartID Scan is a real-time video stream decoder, allowing to scan various type of content.

Depending on the version you downloaded, you'll be able to use it to decode any combination of the following codes :

* 1D/2D barcodes (EAN8, EAN13, UPC, Code39, Code128, Datamatrix, databars, QRCodes, Aztec codes, ... )
* Multiple OCR types :
    * OCR-B MRZ used on official papers (Swiss ID Cards, Swiss Passports, Swiss Driving license)
    * Swiss, German, French and Italian car plates
    * European Health Insurance Card
    * Swiss Inpayment Slips
* Our decoder never stops growing, ask us about what's new in SmartID Scan !

## Requirements

This project is a demo Android app to show how to integrate our decoding library in an Android Project. To make it work, just follow the few steps below.

* Import this demo project into Android Studio
* Download the decoding library from http://scan.smartidlab.com 
* Put the decoding library files you received under app/src/main/jnlibs in your project

```app/src/main/jniLibs
app/src/main/jniLibs/armeabi/libicaredecoders.so
app/src/main/jniLibs/armeabi-v7a/libicaredecoders.so
app/src/main/jniLibs/x86/libicaredecorders.so
```
* Set the decoding type according to the decoding library you downloaded, into the MainActivityFragment (see below in Implementation) :

````mSmartScanner.setDecodingType(SmartScanner.CODE_1D2D_ALL);````

Voilà! This is all you need to launch this project on a real device for testing purposes.

Don't forget to give camera permission to the app if you're using it on a device with API 23 or higher. This is not managed in this example code. See https://developer.android.com/training/permissions/requesting.html for help on this topic.


### SmartScan Decoder Lib

This lib manages the decoding process.

Simply copy paste the downloaded jniLibs folder into your app/src/main/ :

```app/src/main/jniLibs
app/src/main/jniLibs/armeabi/libicaredecoders.so
app/src/main/jniLibs/armeabi-v7a/libicaredecoders.so
app/src/main/jniLibs/x86/libicaredecorders.so
```

## Implementation

### SmartScanner

Interaction with the SmartID Scan library is done with a SmartScanner object. Instantiate it inside onCreate() method in your Activity and pass it a reference to your context.

    mSmartScanner = new SmartScanner(this);

Set the decoding type according to the decoding library you downloaded, using setDecodingType() method call on the SmartScanner object. This method allows use of bitwise operators if you have multiple decoding options :

    mSmartScanner.setDecodingType(CODE_INSURANCE_CARDS | CODE_OCR_B_BVR | CODE_OCR_B_SWISS_PASSPORTS | CODE_QR_CODES);

As SmartScanner involves calls to the Android Camera API, it is tightly coupled with the Activity lifecycle.

* Call init() method of SmartScanner object in onResume() :

```java
@Override
public void onResume() {
    super.onResume();
    mSmartScanner.init();
}
```

* To ensure resources are released when not needed anymore, call release() on SmartScanner object in onPause().  

```java
@Override
public void onPause() {
    mSmartScanner.release();
    super.onPause();
}
```

### Layout and Views

Define a layout, with one CanvasDrawer for target display and one SurfaceView for camera preview, laying on top of each other :

```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    android:id="@+id/activity_main"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:custom="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

	<!-- Displays the camera preview -->
    <SurfaceView
        android:id="@+id/surfaceview"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent" />

    <!-- Displays targeting help. You can set target window color and width here using custom attributes -->
    <ch.icare.smartscan.CanvasDrawer
        android:id="@+id/canvasdrawer"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        android:background="@color/colorPrimaryDark"
        custom:targetWidth="4dp"
        custom:targetColor="@color/colorAccent"/>

</FrameLayout>
```

Pass those two views to SmartScanner inside Activity or Fragment. Ensure you are fullscreen to prevent distorted preview from camera :


``` 
mSmartScanner.setCanvasDrawer((CanvasDrawer)findViewById(R.id.canvasdrawer));   
mSmartScanner.setSurfaceView((SurfaceView)findViewById(R.id.surfaceview));
```
    
### Permissions

Set needed features in app manifest :

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.smartscan.myapplication">
    
    <uses-feature android:name="android.hardware.camera"/>
    <uses-feature
        android:name="android.hardware.camera.autofocus"
        android:required="false"/>
    <uses-feature
        android:name="android.hardware.camera.flash"
        android:required="false"/>
    
    ...


</manifest>
```
Depending on your targeted API level (23 and +), you should also manage app permission for the camera into your code.

## Android support

SmartID Scan is designed to work from Android 2.2 onwards. If your project supports lower versions of the Android framework, SmartID Scan may not work as expected and could show unexpected behaviour in your application.

Note that some low cost or old devices with poor camera hardware may have sub-optimal decoding capabilities. 

## Links
* Download our decoding libraries (1) on :
http://scan.smartidlab.com

* Download our demo app source code on :
https://github.com/SmartIDLab/smartidscan-android-demo


(1) The demonstration decoding libraries obfuscate some characters in the returned result string.


Institut Icare, SmartIdLab, 2017-05-19





 