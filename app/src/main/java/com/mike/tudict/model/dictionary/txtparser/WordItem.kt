package com.mike.tudict.model.dictionary.txtparser

import java.util.ArrayList

class WordItem {
    var _id: Int = 0
    var original: String? = null
    var explaination: String? = null
    var explainationEng: String? = null
    var explainationChn: String? = null
    var pid: Int = 0
    var examples: ArrayList<String>? = null

    constructor() {
        _id = -1
        original = null
        explaination = null
        examples = null
        pid = -1
    }

    constructor(org: String) {
        _id = -1
        original = org
        explaination = null
        examples = null
        pid = -1
    }
}
