package com.mike.tudict

import android.app.Application
import com.mike.tudict.component.CrashHandler
import com.squareup.leakcanary.LeakCanary

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this)

        CrashHandler().init(this)

        if (!DbInitialization.check(this)) {
            DbInitialization.initization(this)
        }
    }
}