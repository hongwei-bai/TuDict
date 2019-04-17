package com.mike.tudict.viewmodel

import android.content.Context
import com.mike.tudict.R


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/17
 * Description:
 */

object AboutInfo {
    fun appendAppVersionInfo(context: Context): String {
        return "\n\n\n" +
                context.getString(R.string.app_name) + " v" +
                context.packageManager.getPackageInfo(context.getPackageName(), 0).versionName
    }
}