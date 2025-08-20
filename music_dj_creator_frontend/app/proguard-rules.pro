# Keep parcelable creators if any get introduced inadvertently
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Gson model classes and fields
-keep class org.example.app.model.** { *; }
-keepclassmembers class org.example.app.model.** { *; }

# Keep ExoPlayer classes
-keep class androidx.media3.** { *; }
-dontwarn org.checkerframework.**
