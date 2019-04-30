package com.mike.tudict.model.network.service

import com.mike.tudict.model.network.dto.DictVerInfoDto
import com.mike.tudict.model.network.common.BaseDto
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/30
 * Description:
 */
interface DictSystemService {

    @GET("latestdictver")
    fun getLatestDictVer(): Observable<BaseDto<Long>>

    @GET("dictver/{dictver}")
    fun getDictVerInfo(@Path("dictver") dictver: Long): Observable<BaseDto<DictVerInfoDto>>
}