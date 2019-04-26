package com.mike.tudict

import java.io.File

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
object Constants {
    object Debug {
        val DB_DEBUG = true
    }

    object APP {
        val EXE_VERSION = "0.1"
        val PLATFORM = "Android"
        val TITLE = ("TuDict" + " v" + EXE_VERSION + "(" + PLATFORM + ")")
    }

    object PHONE_DIR {
        val USER_FOLDER: String
        val USER_PATH1 = "storage/emulated/0"
        val USER_PATH2 = "mnt/sdcard"
        val FOLDER = "bhwword"
        val IMPORT_FILE = "import.txt"
        val EXPORT_FILE = "export.txt"

        init {
            val dir1 = File(USER_PATH1, FOLDER)
            val dir2 = File(USER_PATH2, FOLDER)
            if (dir1 != null && dir1.exists() && dir1.isDirectory) {
                USER_FOLDER = USER_PATH1 + File.separator + FOLDER
            } else if (dir2 != null && dir2.exists() && dir2.isDirectory) {
                USER_FOLDER = USER_PATH2 + File.separator + FOLDER
            } else {
                USER_FOLDER = USER_PATH1 + File.separator + FOLDER
            }
        }
    }

    object DB {
        val DICT_DIR = "dict"
        val DICT_PATH = Constants.PHONE_DIR.USER_FOLDER + File.separator + DICT_DIR
        val DICT_TXT_EXTENSION = "txt"
        val DICT_TXT_ENCODE = "GBK"

        val DB_URL_ROOT = "127.0.0.1"
        val DB_PORT_NO = "3306"
        val DB_NAME = "dict01"
        val DB_USER = "user"
        val DB_PASSWORD = "Td123456"

        val NULL = "null"
        val VOICE = "[Voice]"
    }

    object KEYCODE {

        object MOUSE {
            val LEFT = 1
            val MIDDLE = 2
            val RIGHT = 3
        }

        val ENTER = 10
        val ESC = 27
        val CTRL = 17

        val HOME = 35
        val END = 36
        val LEFT = 37
        val DOWN = 38
        val RIGHT = 39
        val UP = 40
    }

    val TAG = "TuDict"
    const val SEARCH_MAX_LIMIT = 10

    const val VIEW_TYPE_RATING = 0
    const val VIEW_TYPE_PHONTIC = 1
    const val VIEW_TYPE_EXPLAIN = 2
    val VIEW_TYPE_COUNT = VIEW_TYPE_EXPLAIN + 1

    object BUGLY {
        val APP_ID = "c51611bceb"
        val APP_KEY = "621905f5-ec2a-41a5-9f08-b54c0e216e3a"
    }
}
