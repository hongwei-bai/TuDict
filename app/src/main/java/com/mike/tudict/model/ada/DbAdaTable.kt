package com.mike.tudict.model.ada

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
object DbAdaTable {
    object TABLE_WORD {
        const val TABLE = "tbl_word"
        const val _ID = "_id"
        const val ENGLISH = "english"
        const val FILE = "file"
        val CREATER = arrayOf("$ENGLISH varchar(200) not null", "$FILE varchar(100) not null")
    }

    object TABLE_PROPERTY {
        const val TABLE = "tbl_property"
        const val _ID = "_id"
        const val PROPERTY = "property"
        const val PRONUNCIATION_RAW = "pronunciation_raw"
        const val PRONUNCIATION = "pronunciation"
        const val CONTENT = "content"
        const val PARENT_ID = "pid"
        val CREATER = arrayOf(
            "$PROPERTY varchar(200)",
            "$PRONUNCIATION_RAW varchar(200)",
            "$PRONUNCIATION varchar(200)",
            "$CONTENT varchar(400)",
            "$PARENT_ID int not null"
        )
    }

    object TABLE_ITEM {
        const val TABLE = "tbl_item"
        const val _ID = "_id"
        const val ITEM = "item"
        const val LEVELNO = "levelno"
        const val SEQUENCE = "sequence"
        const val CONTAIN_INFORMATION = "info"
        const val EXPLAINATION = "explaination"
        const val EXPLAINATION_ENG = "explaination_eng"
        const val EXPLAINATION_CHN = "explaination_chn"
        const val PARENT_ID = "pid"
        val CREATER = arrayOf(
            "$ITEM varchar(100)",
            "$LEVELNO int",
            "$SEQUENCE int",
            "$CONTAIN_INFORMATION int not null",
            "$EXPLAINATION varchar(2000)",
            "$EXPLAINATION_ENG varchar(2000)",
            "$EXPLAINATION_CHN varchar(2000)",
            "$PARENT_ID int not null"
        )
    }

    object TABLE_EXAMPLE {
        const val TABLE = "tbl_example"
        const val _ID = "_id"
        const val EXAMPLE = "example"
        const val PARENT_ID = "pid"
        val CREATER = arrayOf("$EXAMPLE varchar(600) not null", "$PARENT_ID int not null")
    }

    object TABLE_IDIOM {
        const val TABLE = "tbl_idiom"
        const val _ID = "_id"
        const val ORIGINAL = "original"
        const val PARENT_ID = "pid"
        val CREATER = arrayOf("$ORIGINAL varchar(6000) not null", "$PARENT_ID int not null")
    }

    object INDEX {
        val ARRAY = arrayOf(
            IndexStru(
                "",
                TABLE_WORD.TABLE,
                TABLE_WORD.ENGLISH
            ),
            IndexStru(
                "",
                DbAdaTable.TABLE_PROPERTY.TABLE,
                DbAdaTable.TABLE_PROPERTY.PARENT_ID
            ),
            IndexStru(
                "",
                DbAdaTable.TABLE_ITEM.TABLE,
                DbAdaTable.TABLE_ITEM.PARENT_ID
            ),
            IndexStru(
                "",
                DbAdaTable.TABLE_EXAMPLE.TABLE,
                DbAdaTable.TABLE_EXAMPLE.PARENT_ID
            )
        )
    }

    class IndexStru(var type: String, var table: String, var field: String)
}
