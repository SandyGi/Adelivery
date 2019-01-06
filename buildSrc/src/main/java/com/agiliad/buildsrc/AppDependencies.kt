package com.agiliad.buildsrc

object Versions{

    // Android
    const val androidMinSdk = 18
    const val androidTargetSdk = 28
    const val androidCompileSdk = 28

    // App Libraries
    const val gson = "2.8.1"
    const val room = "1.0.0"
    const val moshi = "1.8.0"
    const val glide = "4.8.0"
    const val dagger = "2.19"
    const val timber = "4.7.1"
    const val rxJava = "2.2.4"
    const val stetho = "1.5.0"
    const val okHttp = "3.12.0"
    const val kotlin = "1.2.71"
    const val retrofit = "2.5.0"
    const val rxKotlin = "2.3.0"
    const val lifecycle = "1.1.1"
    const val rxAndroid = "2.1.0"
    const val javaxInject = "1"
    const val leakCanary = "1.6.2"
    const val supportLibrary = "28.0.0"
    const val javaxAnnotation = "1.0"
    const val constraintLayout = "1.1.3"
    const val lifecycleCompiler = "1.1.1"
    const val pageIndicatorView = "0.2.0"
    const val glassfishAnnotation = "10.0-b28"
    const val googleGmsPlayServices = "16.0.0"


    // Test Library
    const val jUnit = "4.12"
    const val mokito = "2.23.4"
    const val robolectricTest = "1.0.0"
    const val supportTestRunner = "1.0.1"
    const val supportTestEspresso = "3.0.2"

}

object AppDependencies{

    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val stetho = "com.facebook.fresco:stetho:${Versions.stetho}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val javaxInject = "javax.inject:javax.inject:${Versions.javaxInject}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val lifecycle = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:2.0.0-beta3"
    const val roomRxJava = "android.arch.persistence.room:rxjava2:${Versions.room}"
    const val roomRuntime = "android.arch.persistence.room:runtime:${Versions.room}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val roomCompiler = "android.arch.persistence.room:compiler:${Versions.room}"
    const val javaxAnnotation = "javax.annotation:jsr250-api:${Versions.javaxAnnotation}"
    const val okHttpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    const val kotlinSupport = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val androidSupportDesign = "com.android.support:design:${Versions.supportLibrary}"
    const val retrofitAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val androidSupportV13 = "com.android.support:support-v13:${Versions.supportLibrary}"
    const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    const val lifecycleCompiler = "android.arch.lifecycle:compiler:${Versions.lifecycleCompiler}"
    const val leakCanaryDebug = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    const val androidSupportCardView = "com.android.support:cardview-v7:${Versions.supportLibrary}"
    const val glassfishAnnotation = "org.glassfish:javax.annotation:${Versions.glassfishAnnotation}"
    const val androidSupportAppCompat7 = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
    const val androidSupportAppCompat4 = "com.android.support:support-v4:${Versions.supportLibrary}"
    const val pageindicatorview = "com.romandanylyk:pageindicatorview:${Versions.pageIndicatorView}@aar"
    const val androidSupportRecyclerView = "com.android.support:recyclerview-v7:${Versions.supportLibrary}"
    const val leakCanaryRelease = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakCanary}"
    const val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
    const val googleGmsPlayService = "com.google.android.gms:play-services-plus:${Versions.googleGmsPlayServices}"
    const val androidSupportVectorDrawable = "com.android.support:support-vector-drawable:${Versions.supportLibrary}"

    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val mockito = "org.mockito:mockito-core:${Versions.mokito}"
    const val mockitoTest =  "org.mockito:mockito-android:${Versions.mokito}"
    const val robolectricTest = "androidx.test:core:${Versions.robolectricTest}"
    const val supportTestRunner = "com.android.support.test:runner:${Versions.supportTestRunner}"
    const val supportEspressoRunner = "com.android.support.test.espresso:espresso-core:${Versions.supportTestEspresso}"

//    const val androidRules = "com.android.support.test:rules:${Versions.runner}"
//    const val androidRunner = "com.android.support.test:runner:${Versions.runner}"
//    const val assertj = "org.assertj:assertj-core:${Versions.assertJ}"
//    const val dexmakerMockito = "com.linkedin.dexmaker:dexmaker-mockito:${Versions.dexmakerMockito}"
//    const val dexopener = "com.github.tmurakami:dexopener:${Versions.dexopener}"
//    const val espressoContrib = "com.android.support.test.espresso:espresso-contrib:${Versions.espresso}"
//    const val espressoCore = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
//    const val espressoIntents = "com.android.support.test.espresso:espresso-intents:${Versions.espresso}"
//    const val junit = "junit:junit:${Versions.jUnit}"
//    const val kotlinJUnit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
//    const val mockitoKotlin = "com.nhaarman:mockito-kotlin:${Versions.mockitoKotlin}"
//    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
//    const val roomTesting = "android.arch.persistence.room:testing:${Versions.room}"
//    const val supportRules = "com.android.support.test:rules:${Versions.androidSupportRules}"
//    const val supportRunner = "com.android.support.test:runner:${Versions.androidSupportRunner}"
}
