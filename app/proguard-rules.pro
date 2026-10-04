# BCrypt
-keep class org.mindrot.jbcrypt.** { *; }

# Room entities
-keep class com.example.kopkarrsui.data.local.entity.** { *; }

# Enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Parcelable
-keep class * implements android.os.Parcelable { *; }

# Keep data classes for serialization
-keep class com.example.kopkarrsui.data.local.** { *; }
