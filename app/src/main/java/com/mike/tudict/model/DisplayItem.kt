package com.mike.tudict.model

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
class DisplayItem {
    var viewType: Int = 0
    var string: String = ""
    var selected = false

    constructor(type: Int) {
        viewType = type
    }

    constructor(type: Int, s: String) {
        viewType = type
        string = s
    }

    constructor(type: Int, sel: Boolean) {
        viewType = type
        selected = sel
    }
}
