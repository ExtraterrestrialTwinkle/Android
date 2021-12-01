package com.smolianinovasiuzanna.hw27.data.entities.contract

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.contractor.Contractor
import com.smolianinovasiuzanna.hw27.data.entities.contractor.ContractorContract
import com.smolianinovasiuzanna.hw27.data.entities.factory.Factory
import com.smolianinovasiuzanna.hw27.data.entities.factory.FactoryContract
import org.threeten.bp.Instant

@Entity(
    tableName = ContractContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Factory::class,
            parentColumns = [FactoryContract.Columns.ID],
            childColumns = [ContractContract.Columns.FACTORY_ID]
        ),
        ForeignKey(
            entity = Contractor::class,
            parentColumns = [ContractorContract.Columns.ID],
            childColumns = [ContractContract.Columns.CONTRACTOR_ID]
        )
    ]
)
@TypeConverters(ContractStatusConverter::class)
data class Contract(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ContractContract.Columns.ID) val id: Int,
    @ColumnInfo(name = ContractContract.Columns.TITLE) val title: String,
    @ColumnInfo(name = ContractContract.Columns.DURATION) val duration: Int, //количество дней
    @ColumnInfo(name = ContractContract.Columns.PRICE) val price: Float,
    @ColumnInfo(name = ContractContract.Columns.STATUS) val status: ContractStatus,
    @ColumnInfo(name = ContractContract.Columns.FACTORY_ID) val factoryId: Int = 1,
    @ColumnInfo(name = ContractContract.Columns.CONTRACTOR_ID) val contractorId: Int
){
    override fun toString(): String {
        return this.title
    }
}

