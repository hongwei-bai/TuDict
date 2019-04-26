package com.mike.tudict.viewmodel

import com.mike.tudict.Constants
import com.mike.tudict.model.ada.DbAdaInterface
import com.mike.tudict.model.ada.DbAdaTable
import com.mike.tudict.model.impl.SQLiteImpl

import java.util.ArrayList

class DictLookupAutoCompleteModel(dbWrapper: Any) {
    private val mLimit = Constants.SEARCH_MAX_LIMIT
    private val mDbAdaInterface: DbAdaInterface

    val stubWordList: ArrayList<String>
        get() {
            val list = ArrayList<String>()
            for (i in 0 until MAX_HINT) {
                list.add(" ")
            }
            mSize = 0
            return list
        }

    val size: Int
        get() = mSize

    init {
        mDbAdaInterface = SQLiteImpl(dbWrapper)
    }

    fun getSuggestionWordList(keyword: String): ArrayList<String> {
        val accurateList = getAccurateMatchWordList(keyword)
        val prefixList = getPrefixMatchWordList(keyword)
        val list = ArrayList(accurateList)
        for (prefix in prefixList) {
            if (list.size >= MAX_HINT) {
                break
            }
            if (!list.contains(prefix)) {
                list.add(prefix)
            }
        }

        mSize = list.size
        return list
    }

    private fun getAccurateMatchWordList(keyword: String): ArrayList<String> {
        var limitClause = ""
        if (mLimit > 0) {
            limitClause = " LIMIT $mLimit"
        }
        val condition = DbAdaTable.TABLE_WORD.ENGLISH + " = '" + keyword + "' " + limitClause
        return mDbAdaInterface.queryFieldList(
            DbAdaTable.TABLE_WORD.TABLE,
            DbAdaTable.TABLE_WORD.ENGLISH, condition
        )
    }

    private fun getPhraseMatchWordList(keyword: String): ArrayList<String> {
        var limitClause = ""
        if (mLimit > 0) {
            limitClause = " LIMIT $mLimit"
        }
        val condition = (DbAdaTable.TABLE_WORD.ENGLISH + " LIKE '" + keyword + " %' "
                + limitClause)
        return mDbAdaInterface.queryFieldList(
            DbAdaTable.TABLE_WORD.TABLE,
            DbAdaTable.TABLE_WORD.ENGLISH, condition
        )
    }

    private fun getPrefixMatchWordList(keyword: String): ArrayList<String> {
        var limitClause = ""
        if (mLimit > 0) {
            limitClause = " LIMIT $mLimit"
        }
        val condition = (DbAdaTable.TABLE_WORD.ENGLISH + " LIKE '" + keyword + "%' "
                + limitClause)
        return mDbAdaInterface.queryFieldList(
            DbAdaTable.TABLE_WORD.TABLE,
            DbAdaTable.TABLE_WORD.ENGLISH, condition
        )
    }

    companion object {
        private var mSize = 0
        val MAX_HINT = 20
    }
}
