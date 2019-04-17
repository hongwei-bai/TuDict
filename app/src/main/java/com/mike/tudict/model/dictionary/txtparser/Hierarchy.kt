package com.mike.tudict.model.dictionary.txtparser

import java.util.ArrayList

class Hierarchy {
    var isleaf: Boolean = false
    var index: String? = null
    var indexStart: Int = 0
    var indexEnd: Int = 0
    var nextIndexStart: Int = 0
    var list: ArrayList<Hierarchy>? = null
    var item: WordItem? = null
    var level: Int = 0
    var sequence: Int = 0
    var containInfo: Boolean = false
}
