package com.mike.tudict.model.ada

import java.util.ArrayList

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
interface DbAdaInterface {
    fun getConnection(dbWrapper: Any): Boolean

    fun applyBatch(list: ArrayList<Any>): Int

    fun insert(table: String, keys: Array<String>, values: Array<String>): Int

    fun buildInsertOperation(table: String, keys: Array<String>, values: Array<String>): Any

    fun buildDeleteOperation(table: String, condition: String): Any

    fun getLastIndex(table: String): Int

    fun queryId(table: String, condition: String): Int

    fun queryField(table: String, field: String, condition: String): String?

    fun queryIdList(table: String, condition: String): ArrayList<Int>

    fun queryFieldList(table: String, field: String, condition: String): ArrayList<String>

    fun queryFieldsList(
        table: String, fields: ArrayList<String>,
        condition: String
    ): ArrayList<ArrayList<String>>

    fun isTableExist(table: String): Boolean

    fun createTableIfNotExist(table: String, columns: Array<String>): Boolean

    fun executeSql(sql: String): Int

    fun delete(table: String, condition: String): Int
}
