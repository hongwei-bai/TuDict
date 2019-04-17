package com.mike.tudict.model.impl

import android.content.ContentProviderOperation
import android.content.ContentValues
import android.content.Context
import android.content.OperationApplicationException
import android.database.Cursor
import android.net.Uri
import android.os.RemoteException
import android.util.Log
import com.mike.tudict.model.provider.DictProvider
import com.mike.tudict.model.ada.DbAdaInterface
import com.mike.tudict.model.ada.DbAdaTable

import java.util.ArrayList

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class SQLiteImpl(dbWrapper: Any) : DbAdaInterface {
    private val mContext: Context

    init {
        mContext = dbWrapper as Context
    }

    override fun applyBatch(list: ArrayList<Any>): Int {
        val operations = ArrayList<ContentProviderOperation>()
        for (`object` in list) {
            operations.add(`object` as ContentProviderOperation)
        }
        try {
            mContext.contentResolver.applyBatch(DictProvider.AUTHORITIES, operations)
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: OperationApplicationException) {
            e.printStackTrace()
        }

        return 0
    }

    override fun buildInsertOperation(table: String, keys: Array<String>, values: Array<String>): Any {
        val contentValues = ContentValues()
        for (i in keys.indices) {
            contentValues.put(keys[i], values[i])
        }
        return ContentProviderOperation.newInsert(getUri(table))
            .withValues(contentValues).build()
    }

    override fun buildDeleteOperation(table: String, condition: String): Any {
        return ContentProviderOperation.newDelete(getUri(table))
            .withSelection(condition, null).build()
    }

    private fun getUri(table: String): Uri? {
        when (table) {
            DbAdaTable.TABLE_WORD.TABLE -> return DictProvider.CONTENT_URI
            DbAdaTable.TABLE_PROPERTY.TABLE -> return DictProvider.PROPERTY_URI
            DbAdaTable.TABLE_ITEM.TABLE -> return DictProvider.ITEM_URI
            DbAdaTable.TABLE_EXAMPLE.TABLE -> return DictProvider.EXAMPLE_URI
            else -> {
            }
        }
        return null
    }

    override fun insert(table: String, keys: Array<String>, values: Array<String>): Int {
        val contentValues = ContentValues()
        for (i in keys.indices) {
            contentValues.put(keys[i], values[i])
        }
        mContext.contentResolver.insert(getUri(table)!!, contentValues)
        return 0
    }

    override fun getLastIndex(table: String): Int {
        val projection = arrayOf("MAX(_id)")
        var cursor: Cursor? = null
        try {
            cursor = mContext.contentResolver.query(getUri(table)!!, projection, null, null, null)
            if (null == cursor) {
                return -1
            }
            if (!cursor.isClosed && cursor.count > 0) {
                cursor.moveToFirst()
                return cursor.getInt(FIRST_COLUMN)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return -1
    }

    override fun queryId(table: String, condition: String): Int {
        val projection = arrayOf("_id")
        var cursor: Cursor? = null
        try {
            cursor = mContext.contentResolver.query(getUri(table)!!, projection, condition, null, null)
            if (null == cursor) {
                return -1
            }
            if (!cursor.isClosed && cursor.count > 0) {
                cursor.moveToFirst()
                return cursor.getInt(FIRST_COLUMN)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return -1
    }

    override fun queryField(table: String, field: String, condition: String): String? {
        val projection = arrayOf(field)
        var cursor: Cursor? = null
        try {
            cursor = mContext.contentResolver.query(getUri(table)!!, projection, condition, null, null)
            if (null == cursor) {
                return null
            }
            if (!cursor.isClosed && cursor.count > 0) {
                cursor.moveToFirst()
                return cursor.getString(FIRST_COLUMN)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
    }

    override fun queryIdList(table: String, condition: String): ArrayList<Int> {
        val list = ArrayList<Int>()
        val projection = arrayOf("_id")
        var cursor: Cursor? = null
        try {
            cursor = mContext.contentResolver.query(getUri(table)!!, projection, condition, null, null)
            if (null == cursor) {
                return list
            }
            if (!cursor.isClosed && cursor.count > 0) {
                cursor.moveToPosition(-1)
                while (cursor.moveToNext()) {
                    list.add(cursor.getInt(FIRST_COLUMN))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }

    override fun queryFieldList(table: String, field: String, condition: String): ArrayList<String> {
        val list = ArrayList<String>()
        val projection = arrayOf(field)
        var cursor: Cursor? = null
        try {
            cursor = mContext.contentResolver.query(getUri(table)!!, projection, condition, null, null)
            if (null == cursor) {
                return list
            }
            if (!cursor.isClosed && cursor.count > 0) {
                cursor.moveToPosition(-1)
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(FIRST_COLUMN))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }

    override fun queryFieldsList(
        table: String, fields: ArrayList<String>,
        condition: String
    ): ArrayList<ArrayList<String>> {
        var cursor: Cursor? = null
        val list = ArrayList<ArrayList<String>>()
        try {
            cursor = mContext.contentResolver
                .query(getUri(table)!!, null, condition, null, null)
            if (null == cursor) {
                return list
            }

            cursor.moveToPosition(-1)
            while (cursor.moveToNext()) {
                val wordData = ArrayList<String>()
                for (field in fields) {
                    wordData.add(cursor.getString(cursor.getColumnIndex(field)))
                }
                list.add(wordData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }

    override fun createTableIfNotExist(table: String, columns: Array<String>): Boolean {
        return false
    }

    override fun isTableExist(table: String): Boolean {
        return true
    }

    override fun executeSql(sql: String): Int {
        return 0
    }

    override fun delete(table: String, condition: String): Int {
        return mContext.contentResolver.delete(getUri(table)!!, condition, null)
    }

    override fun getConnection(dbWrapper: Any): Boolean {
        return true
    }

    companion object {
        private val FIRST_COLUMN = 0
    }
}
