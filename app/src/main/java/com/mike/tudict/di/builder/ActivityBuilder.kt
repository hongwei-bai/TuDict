package com.mike.tudict.di.builder

import com.mike.tudict.di.module.MainActivityModule
import com.mike.tudict.di.provider.DictFragmentProvider
import com.mike.tudict.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/26
 * Description:
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            DictFragmentProvider::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}