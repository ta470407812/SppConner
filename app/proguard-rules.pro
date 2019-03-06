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
-optimizationpasses 5                                             #指定代码压缩级别
-dontusemixedcaseclassnames                                 #混淆时不会产生形形色色的类名
-dontskipnonpubliclibraryclasses                            #指定不忽略非公共类库
-dontpreverify                                              #不预校验，如果需要预校验，是-dontoptimize
-ignorewarnings                                             #屏蔽警告
-verbose                                                    #混淆时记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #优化

-keep class com.google.android.**{*;}
-keep class com.google.android.gms.**{*;}
-keep class com.google.**
-keep class android.**
-keep class androidx.**
-keep class io.realm.**
-keep class com.tang.**
-keep class pl.tajchert.nammu.**{*;}

-dontwarn com.google.android.gms.**
-dontwarn com.google.android.**
-dontwarn com.google.**
-dontwarn android.**
-dontwarn androidx.**

-dontwarn android.support.**
-dontwarn com.android.support.appcompat-v7.**
-dontwarn com.android.support.support-v4.**
-dontwarn io.realm.**
-dontwarn com.tang.**