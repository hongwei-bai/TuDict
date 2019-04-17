package com.mike.tudict.model.dictionary.txtparser

class WordProperty {

    var _id: Int = 0
    var property: String? = null
    var pronunciationRaw: String? = null
    var pronunciation: String? = null
    var content: String? = null
    var items: Hierarchy? = null
    var idiom: WordIdiom? = null

    companion object {
        val noun = "n"
        val pronoun = "pron"
        val adjective = "adj"
        val adverb = "adv"
        val adverb_dot = "adv."
        val verb = "v"
        val numeral = "num"
        val article = "art"
        val indefinite_article = "indef art"
        val preposition = "prep"
        val conjunction = "conj"
        val interjection = "interj"
    }
}
