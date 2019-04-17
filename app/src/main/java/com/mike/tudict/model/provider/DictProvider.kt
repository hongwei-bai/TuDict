package com.mike.tudict.model.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.mike.tudict.model.ada.DbAdaTable

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class DictProvider : ContentProvider() {

    private var dbHelper: DictDbHelper? = null

    override fun onCreate(): Boolean {
        dbHelper = DictDbHelper(context!!)
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        var selection = selection
        val db = dbHelper!!.writableDatabase
        val cursor: Cursor
        var tableNameString = ""
        when (uriMatcher!!.match(uri)) {
            URI_WORD -> tableNameString = DbAdaTable.TABLE_WORD.TABLE
            URI_PROPERTY -> tableNameString = DbAdaTable.TABLE_PROPERTY.TABLE
            URI_PROPERTY_ID -> {
                tableNameString = DbAdaTable.TABLE_PROPERTY.TABLE
                val propertyId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_PROPERTY._ID + "=" + propertyId
            }
            URI_ITEM -> tableNameString = DbAdaTable.TABLE_ITEM.TABLE
            URI_ITEM_ID -> {
                tableNameString = DbAdaTable.TABLE_ITEM.TABLE
                val itemId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_ITEM._ID + "=" + itemId
            }
            URI_EXAMPLE -> tableNameString = DbAdaTable.TABLE_EXAMPLE.TABLE
            URI_EXAMPLE_ID -> {
                tableNameString = DbAdaTable.TABLE_EXAMPLE.TABLE
                val exampleId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_EXAMPLE._ID + "=" + exampleId
            }
            else -> throw IllegalArgumentException("Unknown URI$uri")
        }
        cursor = db.query(tableNameString, projection, selection, selectionArgs, null, null, null)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper!!.writableDatabase
        val rowId: Long
        var tableNameString = ""
        when (uriMatcher!!.match(uri)) {
            URI_WORD -> tableNameString = DbAdaTable.TABLE_WORD.TABLE
            URI_PROPERTY -> tableNameString = DbAdaTable.TABLE_PROPERTY.TABLE
            URI_ITEM -> tableNameString = DbAdaTable.TABLE_ITEM.TABLE
            URI_EXAMPLE -> tableNameString = DbAdaTable.TABLE_EXAMPLE.TABLE
            else -> throw IllegalArgumentException("Unknown URI$uri")
        }
        rowId = db.insert(tableNameString, null, values)
        if (rowId > 0) {
            val noteUri = ContentUris.withAppendedId(uri, rowId)
            context!!.contentResolver.notifyChange(noteUri, null)
            return noteUri
        }
        throw IllegalArgumentException("Unknown URI$uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        val db = dbHelper!!.writableDatabase
        val rowId: Long
        var tableNameString = ""
        when (uriMatcher!!.match(uri)) {
            URI_WORD -> tableNameString = DbAdaTable.TABLE_WORD.TABLE
            URI_WORD_FILE -> {
                tableNameString = DbAdaTable.TABLE_WORD.TABLE
                val filename = parseString(uri)
                selection = DbAdaTable.TABLE_WORD.FILE + "='" + filename + "'"
            }
            URI_PROPERTY -> tableNameString = DbAdaTable.TABLE_PROPERTY.TABLE
            URI_PROPERTY_ID -> {
                tableNameString = DbAdaTable.TABLE_PROPERTY.TABLE
                val propertyId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_PROPERTY._ID + "=" + propertyId
            }
            URI_ITEM -> tableNameString = DbAdaTable.TABLE_ITEM.TABLE
            URI_ITEM_ID -> {
                tableNameString = DbAdaTable.TABLE_ITEM.TABLE
                val itemId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_ITEM._ID + "=" + itemId
            }
            URI_EXAMPLE -> tableNameString = DbAdaTable.TABLE_EXAMPLE.TABLE
            URI_EXAMPLE_ID -> {
                tableNameString = DbAdaTable.TABLE_EXAMPLE.TABLE
                val exampleId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_EXAMPLE._ID + "=" + exampleId
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        rowId = db.delete(tableNameString, selection, selectionArgs).toLong()
        if (rowId > 0) {
            val noteUri = ContentUris.withAppendedId(uri, rowId)
            context!!.contentResolver.notifyChange(noteUri, null)
        }
        return rowId.toInt()
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        val db = dbHelper!!.writableDatabase
        val rowId: Long
        var tableNameString = ""
        when (uriMatcher!!.match(uri)) {
            URI_WORD -> tableNameString = DbAdaTable.TABLE_WORD.TABLE
            URI_PROPERTY_ID -> {
                tableNameString = DbAdaTable.TABLE_PROPERTY.TABLE
                val propertyId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_PROPERTY._ID + "=" + propertyId
            }
            URI_ITEM_ID -> {
                tableNameString = DbAdaTable.TABLE_ITEM.TABLE
                val itemId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_ITEM._ID + "=" + itemId
            }
            URI_EXAMPLE_ID -> {
                tableNameString = DbAdaTable.TABLE_EXAMPLE.TABLE
                val exampleId = ContentUris.parseId(uri).toInt()
                selection = DbAdaTable.TABLE_EXAMPLE._ID + "=" + exampleId
            }
            else -> throw IllegalArgumentException("Unknown URI$uri")
        }
        rowId = db.update(tableNameString, values, selection, selectionArgs).toLong()
        if (rowId > 0) {
            val noteUri = ContentUris.withAppendedId(uri, rowId)
            context!!.contentResolver.notifyChange(noteUri, null)
            return rowId.toInt()
        }
        throw IllegalArgumentException("Unknown URI$uri")
    }

    private fun parseString(uri: Uri?): String {
        if (null == uri) {
            return ""
        }
        val string = uri.toString()
        val pos = string.lastIndexOf("/")
        return if (pos < 0) {
            ""
        } else string.substring(pos + 1)
    }

    companion object {
        private var uriMatcher: UriMatcher? = null
        private val URI_WORD = 1
        private val URI_WORD_FILE = 2
        private val URI_PROPERTY = 3
        private val URI_PROPERTY_ID = 4
        private val URI_ITEM = 5
        private val URI_ITEM_ID = 6
        private val URI_EXAMPLE = 7
        private val URI_EXAMPLE_ID = 8
        val AUTHORITIES = "tudict.dict.provider"
        val AUTHORITIES_URI = Uri.parse("content://$AUTHORITIES")
        val CONTENT_URI = Uri.withAppendedPath(AUTHORITIES_URI, "word")
        val PROPERTY_URI = Uri.withAppendedPath(AUTHORITIES_URI, "property")
        val ITEM_URI = Uri.withAppendedPath(AUTHORITIES_URI, "item")
        val EXAMPLE_URI = Uri.withAppendedPath(AUTHORITIES_URI, "example")

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher!!.addURI(
                AUTHORITIES, "word",
                URI_WORD
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "word/*",
                URI_WORD_FILE
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "property",
                URI_PROPERTY
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "property/#",
                URI_PROPERTY_ID
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "item",
                URI_ITEM
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "item/#",
                URI_ITEM_ID
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "example",
                URI_EXAMPLE
            )
            uriMatcher!!.addURI(
                AUTHORITIES, "example/#",
                URI_EXAMPLE_ID
            )
        }
    }
}
