package com.huagugu.timememorial2

import android.app.Application
import com.huagugu.timememorial2.data.AppDatabase

class TimeMemorialApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
