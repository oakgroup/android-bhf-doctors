# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.samsung.android.**
-dontnote com.samsung.android.**

#noinspection ShrinkerUnresolvedReference
-keep class com.samsung.** { *; }
-keep class android.hardware.** { *; }

## Begin | proguard configuration for Gson

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Gson classes
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type
-keepattributes AnnotationDefault,RuntimeVisibleAnnotations

## End | proguard configuration for Gson

## Begin | proguard configuration for Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
## End | proguard configuration for Parcelable

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Pretty Time
-keep class org.ocpsoft.prettytime.i18n.**

## Begin | proguard configuration for Glide
-dontwarn com.bumptech.glide.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
## End | proguard configuration for Glide

## Begin | proguard configuration for SQLChiper
-keep class net.sqlcipher.** { *; }
## End | proguard configuration for SQLChiper

## Begin | proguard configuration for project database models
-keep class com.active.orbit.baseapp.core.database.models.** { *; }
## End | proguard configuration for project database models

## Begin | proguard configuration for project serialization models
-keep class com.active.orbit.baseapp.core.serialization.** { *; }
## End | proguard configuration for project serialization models

## Begin | proguard configuration for project deserialization models
-keep class com.active.orbit.baseapp.core.deserialization.** { *; }
## End | proguard configuration for project deserialization models