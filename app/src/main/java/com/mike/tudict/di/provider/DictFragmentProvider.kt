package com.mike.tudict.di.provider

import com.mike.tudict.di.module.DictFragmentModule
import com.mike.tudict.view.DictFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/26
 * Description:
 */
@Module
abstract class DictFragmentProvider {
    @ContributesAndroidInjector(modules = [DictFragmentModule::class])
    internal abstract fun provideDictFragmentFactory(): DictFragment
}