package com.mike.tudict.model.network.dto


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/29
 * Description:
 */
data class DictVerInfoDto(
    var version: Long,
    var upgradeType: Int,
    var filelist: List<String>
)
