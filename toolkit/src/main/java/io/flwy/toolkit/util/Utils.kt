package com.cwsinformatica.photocloud.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

@SuppressLint("HardwareIds")
fun Context.getUid() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)