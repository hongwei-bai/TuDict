package com.mike.tudict.model.dictionary.txtparser

import java.util.ArrayList

class WordData {
    var _id: Int = 0
    var originallist: ArrayList<String>? = null
    var english: String? = null
    var reference: String? = null
    var phonetic: String? = null
    var chinese: String? = null
    var file: String? = null
    var properties: ArrayList<WordProperty>? = null

    constructor() {
        this._id = -1
        this.originallist = null
        this.english = null
        this.reference = null
        this.phonetic = null
        this.chinese = null
        this.properties = null
    }

    constructor(orginal: WordData) {
        this._id = orginal._id
        this.originallist = orginal.originallist
        this.english = orginal.english
        this.reference = orginal.reference
        this.phonetic = orginal.phonetic
        this.chinese = orginal.chinese
        this.properties = orginal.properties
    }
}
