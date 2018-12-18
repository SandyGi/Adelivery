package com.agiliad.buildsrc

object Versions{

    // Android
    const val androidCompileSdk = 28
    const val androidMinSdk = 16
    const val androidTargetSdk = 27

    // App Library
    const val constraintLayout = "1.1.3"
    const val supportLibrary = "28.0.0"
    const val kotlin = "1.2.71"
    const val javaxAnnotation = "1.0"

    // Test Library
    const val jUnit = "4.12"
    const val supportTestRunner = "1.0.1"
    const val supportTestEspresso = "3.0.2"
    const val robolectricTest = "1.0.0"
    const val mokito = "2.23.4"

}

object AppDependencies{
    const val androidSupportAppCompat7 = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
    const val kotlinSupport = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val robolectricTest = "androidx.test:core:${Versions.robolectricTest}"
    const val mockito = "org.mockito:mockito-core:${Versions.mokito}"
    const val supportTestRunner = "com.android.support.test:runner:${Versions.supportTestRunner}"
    const val supportEspressoRunner = "com.android.support.test.espresso:espresso-core:${Versions.supportTestEspresso}"
    const val mockitoTest =  "org.mockito:mockito-android:${Versions.mokito}"
    const val javaxAnnotation = "javax.annotation:jsr250-api:${Versions.javaxAnnotation}"
}
