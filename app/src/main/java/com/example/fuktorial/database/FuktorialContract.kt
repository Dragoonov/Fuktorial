package com.example.fuktorial.database

import android.provider.BaseColumns

object FuktorialContract {

    object Fuquotes: BaseColumns {
        const val TABLE_NAME = "fuquotes"
        const val COLUMN_NAME_TEXT = "text"
        const val COLUMN_NAME_DISCOVERED = "discovered"
    }
    object Fucktivities: BaseColumns {
        const val TABLE_NAME = "fucktivities"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_DISCOVERED = "discovered"
        const val COLUMN_NAME_MASTERED = "mastered"
    }
}