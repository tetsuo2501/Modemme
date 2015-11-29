# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\tetsu\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-dontwarn org.apache.**
-dontwarn android.**
-dontwarn com.google.**
-dontwarn com.android.volley.toolbox.**
-dontwarn javax.annotations.**
-dontwarn org.w3c.dom.**

# will keep line numbers and file name obfuscation
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-optimizations !code/allocation/variable

-dontwarn javax.mail.**
-dontwarn javax.management.**
-dontwarn javax.rmi.**
-dontwarn javax.naming.**
-dontwarn javax.transaction.**
-dontwarn javax.persistence.**
-dontwarn java.lang.management.**
-dontwarn java.lang.instrument.**
-dontwarn org.slf4j.**
-dontwarn org.json.**

-keep class org.apache.**
-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-dontwarn org.springframework.**

-dontwarn sun.misc.Unsafe
-dontwarn com.google.gwt.**


# Allow obfuscation of android.support.v7.internal.view.menu.**
# to avoid problem on Samsung 4.2.2 devices with appcompat v21
# see https://code.google.com/p/android/issues/detail?id=78377
-keep class !android.support.v7.internal.view.menu.*MenuBuilder*, android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# Configuration for Fabric Twitter Kit
# See: https://dev.twitter.com/twitter-kit/android/integrate

-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn rx.**
-dontwarn retrofit.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* *;
}


# For using GSON @Expose annotation
-keepattributes *Annotation*
-keepattributes EnclosingMethod


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }


# Branch
-keep class com.google.android.gms.ads.identifier.** { *; }


# OkHttp
-dontwarn rx.**

-dontwarn okio.**

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn retrofit.**
-dontwarn retrofit.appengine.UrlFetchClient
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}


# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-dontwarn com.squareup.leakcanary.DisplayLeakService


#Intercom
-dontwarn intercom.**
-dontwarn io.intercom.**


#Eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}


-keep class com.google.appengine.api.datastore.Text { *; }