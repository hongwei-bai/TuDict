package com.mike.tudict.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mike.tudict.model.DisplayItem
import com.mike.tudict.model.DictSearchModel

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class DictionaryViewModel() : ViewModel() {
    var lookupResult: MutableLiveData<MutableList<String>> = MutableLiveData()
    var explanationResult: MutableLiveData<MutableList<DisplayItem>> = MutableLiveData()

    private lateinit var dictLookupAutoCompleteModel: DictLookupAutoCompleteModel
    private lateinit var dictSearchModel: DictSearchModel

    init {
        lookupResult.value = mutableListOf()
    }

    fun initializeDictDbConnection(context: Context) {
        dictLookupAutoCompleteModel = DictLookupAutoCompleteModel(context)
        dictSearchModel = DictSearchModel(context)
    }

    fun lookupKeywords(keyword: String) {
        if (keyword.trim().isEmpty()) {
            return
        }
        lookupResult.value = dictLookupAutoCompleteModel.getSuggestionWordList(keyword.replace("'", "\\'"))
    }

    fun loadWordDetail(word: String) {
        explanationResult.value = dictSearchModel.lookupDisplayItems(word.replace("'", "\\'"))
    }
}