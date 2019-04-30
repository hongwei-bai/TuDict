package com.mike.tudict.model.dbmodel

import android.util.Log
import com.mike.tudict.model.network.TuRetrofitClient
import com.mike.tudict.model.network.dto.DictVerInfoDto
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/30
 * Description:
 */
object DbUpgradeNetInterface {
    val TAG = "aaaa"

    fun getDictUpgradeInfo(): DictVerInfoDto? {
        TuRetrofitClient.instance.systemService.getLatestDictVer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { n -> Log.i(TAG, "Next: ${n.toString()}") },
                { e -> Log.i(TAG, "Error: $e") },
                { Log.i(TAG, "Completed") })

        TuRetrofitClient.instance.systemService.getDictVerInfo(1L)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { n -> Log.i(TAG, "Next: ${n.toString()}") },
                { e -> Log.i(TAG, "Error: $e") },
                { Log.i(TAG, "Completed") })

        return null
    }


}