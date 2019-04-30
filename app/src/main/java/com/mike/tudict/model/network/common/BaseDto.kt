package com.mike.tudict.model.network.common


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/29
 * Description:
 */
data class BaseDto<T>(
    var code: Int,
    var msg: String,
    var data: T
)