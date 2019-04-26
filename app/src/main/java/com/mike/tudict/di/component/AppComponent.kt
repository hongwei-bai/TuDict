package com.mike.tudict.di.component

import com.mike.tudict.App
import com.mike.tudict.di.builder.ActivityBuilder
import com.mike.tudict.di.module.AppModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/26
 * Description:
 */
@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ActivityBuilder::class, AppModule::class])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}