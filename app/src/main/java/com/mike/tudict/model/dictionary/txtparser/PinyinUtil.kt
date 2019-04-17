package com.mike.tudict.model.dictionary.txtparser

object PinyinUtil {
    fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        // Log.d(" UnicodeBlock = " + ub);
        return if (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            // || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
        ) {
            true
        } else false
    }
}
