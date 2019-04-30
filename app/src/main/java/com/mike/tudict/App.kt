package com.mike.tudict

import com.mike.tudict.component.CrashHandler
import com.mike.tudict.di.component.DaggerAppComponent
import com.mike.tudict.model.dbmodel.DbInitialization
import com.mike.tudict.model.dbmodel.DbUpgradeNetInterface
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.crashreport.CrashReport
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this)

        CrashHandler().init(this)

        CrashReport.initCrashReport(this, Constants.BUGLY.APP_ID, true)

        DbUpgradeNetInterface.getDictUpgradeInfo()

        if (!DbInitialization.check(this)) {
            DbInitialization.initization(this)
        }
    }
}