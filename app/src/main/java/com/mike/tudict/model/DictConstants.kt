package com.mike.tudict.model

/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/14
 * Description:
 */
interface DictConstants {

    interface RegexConstants {
        interface DEFINE {
            companion object {
                val UNIT = "[\\w,;:\\-\\s`@\\?\\^\\[\\\\-强读式]"
                val PHONETIC_UINT = "/$UNIT*(\\(r\\))*$UNIT*?/"
            }
        }

        companion object {

            val PRONUNCIATION_REGEX = DEFINE.PHONETIC_UINT
            val PRONUNCIATION_REGEX_TWINS = (DEFINE.PHONETIC_UINT + "*" + "/\\s*\\([\\s\\w]*"
                    + DEFINE.PHONETIC_UINT + "*" + "/\\)")

            val NUMBLE_TILE_APATTERN = "[0-9]+\\)"
        }
    }

    companion object {
        val NOTE_ON_USAGE = "NOTE ON USAGE 用法:"
        val prefix = "pref 前缀"
        val abbrevation = "abbr 缩写"
        val symbol = "symb 符号"
        val IDIOM = "(idm 习语)"

        val SEPARATOR_EXPLAINATION = ": "
        val SEPARATOR_EXAMPLE = "\\s\\*\\s"

        // public final static String EXPLAINATION_SKIP_CHINESE_WORDS[] = { "口)",
        // "习语)", "古或修辞)",
        // "文, 尤用於法律)", "文)", "用以加强语气)", "与名词结合构成形容词)", "与名词, 形容词, 副词结合)" };
        val EXPLAINATION_SKIP_PATTERN = ") "
    }
}
