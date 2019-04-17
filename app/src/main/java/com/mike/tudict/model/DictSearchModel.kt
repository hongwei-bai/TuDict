package com.mike.tudict.model

import com.mike.tudict.model.ada.DbAdaInterface
import com.mike.tudict.model.ada.DbAdaTable
import com.mike.tudict.model.dictionary.txtparser.Hierarchy
import com.mike.tudict.model.dictionary.txtparser.WordItem
import com.mike.tudict.model.dictionary.txtparser.WordProperty
import com.mike.tudict.model.impl.SQLiteImpl
import java.util.*

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class DictSearchModel(dbWrapper: Any) {
    private val mDbAdaInterface: DbAdaInterface

    init {
        mDbAdaInterface = SQLiteImpl(dbWrapper)
    }

    fun lookupDisplayItems(english: String?): ArrayList<DisplayItem> {
        val list = ArrayList<DisplayItem>()
        if (null == english) {
            return list
        }

        val wordProperties = lookupWordProperties(english) ?: return list
        val wordItems = ArrayList<Hierarchy>()
        for (wp in wordProperties) {
            val wordItemPerProperty = lookupWordItems(wp._id)
            for (hierarchy in wordItemPerProperty) {
                hierarchy.item!!.pid = wp._id
                wordItems.add(hierarchy)
            }
        }

        if (null == wordProperties || 0 == wordProperties.size) {
            return list
        }

        for (property in wordProperties) {
            if (property.pronunciation != null && !null.equals(property.pronunciation)) {
                list.add(DisplayItem(Constants.VIEW_TYPE_PHONTIC, property.pronunciation!!))
                break
            }
        }
        list.add(DisplayItem(Constants.VIEW_TYPE_RATING))

        for (property in wordProperties) {
            var propertyInfo = ""
            if (property.property != null && !null.equals(property.property)) {
                propertyInfo = property.property!!
                if (!propertyInfo.endsWith(".") && !propertyInfo.endsWith(":")) {
                    propertyInfo += "."
                }
            }
            if (property.content != null && !null.equals(property.content)) {
                list.add(DisplayItem(Constants.VIEW_TYPE_EXPLAIN, property.content!!))
            }
            val pid = property._id
            var upperLevelTag: String? = null
            for (wordItem in wordItems) {
                if (wordItem.item!!.pid == pid) {
                    if (wordItem.containInfo) {
                        var infomation = ""
                        infomation += "$propertyInfo "
                        if (upperLevelTag != null && !null.equals(upperLevelTag)) {
                            infomation += upperLevelTag
                            upperLevelTag = null
                        }
                        if (wordItem.index != null && !null.equals(wordItem.index)) {
                            infomation += wordItem.index!! + " "
                        }
                        infomation += wordItem.item!!.explaination
                        list.add(DisplayItem(Constants.VIEW_TYPE_EXPLAIN, infomation))
                    } else {
                        if (1 == wordItem.level) {
                            upperLevelTag = wordItem.index
                        }
                    }
                }
            }
        }
        return list
    }

    private fun lookupWordProperties(word: String): ArrayList<WordProperty>? {
        val id = getWordId(word)
        return if (-1 == id) {
            null
        } else getWordProperty(id)
    }

    private fun lookupWordItems(propertyId: Int): ArrayList<Hierarchy> {
        return getWordItem(propertyId)
    }

    private fun lookupWordExamples(itemId: Int): ArrayList<String> {
        return getExamples(itemId)
    }

    private fun getWordId(english: String): Int {
        val selection = DbAdaTable.TABLE_WORD.ENGLISH + "='" + english + "'"
        return mDbAdaInterface.queryId(DbAdaTable.TABLE_WORD.TABLE, selection)
    }

    private fun getWordProperty(wordId: Int): ArrayList<WordProperty> {
        val result = ArrayList<WordProperty>()
        val condition = DbAdaTable.TABLE_PROPERTY.PARENT_ID + "=" + wordId
        val fieldlist = ArrayList<String>()
        fieldlist.add(DbAdaTable.TABLE_PROPERTY._ID)
        fieldlist.add(DbAdaTable.TABLE_PROPERTY.PROPERTY)
        fieldlist.add(DbAdaTable.TABLE_PROPERTY.PRONUNCIATION)
        fieldlist.add(DbAdaTable.TABLE_PROPERTY.CONTENT)

        val list2d = mDbAdaInterface.queryFieldsList(
            DbAdaTable.TABLE_PROPERTY.TABLE, fieldlist, condition
        )
        for (data in list2d) {
            val property = WordProperty()
            property._id = Integer.valueOf(data[0])
            property.property = data[1]
            property.pronunciation = data[2]
            property.content = data[3]
            result.add(property)
        }
        return result
    }

    private fun getWordItem(propertyId: Int): ArrayList<Hierarchy> {
        val result = ArrayList<Hierarchy>()
        val condition = DbAdaTable.TABLE_ITEM.PARENT_ID + "=" + propertyId
        val fieldlist = ArrayList<String>()
        fieldlist.add(DbAdaTable.TABLE_ITEM._ID)
        fieldlist.add(DbAdaTable.TABLE_ITEM.ITEM)
        fieldlist.add(DbAdaTable.TABLE_ITEM.LEVELNO)
        fieldlist.add(DbAdaTable.TABLE_ITEM.SEQUENCE)
        fieldlist.add(DbAdaTable.TABLE_ITEM.CONTAIN_INFORMATION)
        fieldlist.add(DbAdaTable.TABLE_ITEM.EXPLAINATION)
        fieldlist.add(DbAdaTable.TABLE_ITEM.EXPLAINATION_ENG)
        fieldlist.add(DbAdaTable.TABLE_ITEM.EXPLAINATION_CHN)

        val list2d = mDbAdaInterface.queryFieldsList(
            DbAdaTable.TABLE_ITEM.TABLE, fieldlist, condition
        )

        for (data in list2d) {
            val hierarchy = Hierarchy()
            hierarchy.item = WordItem()
            hierarchy.item!!._id = Integer.valueOf(data[0])
            hierarchy.index = data[1]
            hierarchy.level = Integer.valueOf(data[2])
            hierarchy.sequence = Integer.valueOf(data[3])
            hierarchy.containInfo = data[4] == "1"
            hierarchy.item!!.explaination = data[5]
            hierarchy.item!!.explainationEng = data[6]
            hierarchy.item!!.explainationChn = data[7]
            result.add(hierarchy)
        }

        return result
    }

    private fun getExamples(itemId: Int): ArrayList<String> {
        val condition = DbAdaTable.TABLE_EXAMPLE.PARENT_ID + "=" + itemId
        return mDbAdaInterface.queryFieldList(
            DbAdaTable.TABLE_EXAMPLE.TABLE, DbAdaTable.TABLE_EXAMPLE.EXAMPLE, condition
        )
    }
}
