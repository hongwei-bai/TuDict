package com.mike.tudict.di.module

import com.mike.tudict.view.widget.DictContentAdapter
import com.mike.tudict.view.widget.DictLookupAutoCompleteAdapter
import com.mike.tudict.viewmodel.DictionaryViewModel
import dagger.Module
import dagger.Provides


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/26
 * Description:
 */
@Module
class DictFragmentModule {
    @Provides
    internal fun dictionaryViewModel(): DictionaryViewModel {
        return DictionaryViewModel()
    }

    @Provides
    internal fun provideDictContentAdapter(): DictContentAdapter {
        return DictContentAdapter()
    }

    @Provides
    internal fun provideDictLookupAutoCompleteAdapter(): DictLookupAutoCompleteAdapter {
        return DictLookupAutoCompleteAdapter()
    }
}