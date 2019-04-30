package com.mike.tudict.model.network

import com.mike.tudict.model.network.service.DictSystemService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/29
 * Description:
 */
class TuRetrofitClient {
    companion object {
        val instance = TuRetrofitClient()
    }

    // simple
    val retrofit: Retrofit
    val systemService: DictSystemService

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://47.52.40.23/tudictserver/dict/system/")
            .client(TuOkHttpClient.instance.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

        systemService = retrofit.create(DictSystemService::class.java)
    }
}