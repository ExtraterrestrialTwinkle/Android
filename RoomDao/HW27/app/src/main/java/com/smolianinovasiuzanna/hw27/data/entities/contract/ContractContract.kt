package com.smolianinovasiuzanna.hw27.data.entities.contract

import org.threeten.bp.Instant

object ContractContract {
    const val TABLE_NAME = "contracts"

    object Columns{
        const val ID = "id"
        const val TITLE = "title"
        const val DURATION = "duration"
        const val PRICE = "price"
        const val STATUS = "status"
        const val CONTRACTOR_ID = "contractor_id"
        const val FACTORY_ID = "factory_id"
    }
}