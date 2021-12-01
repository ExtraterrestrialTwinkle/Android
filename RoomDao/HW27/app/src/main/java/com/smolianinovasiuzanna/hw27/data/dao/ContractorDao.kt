package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractContract
import com.smolianinovasiuzanna.hw27.data.entities.contractor.Contractor
import com.smolianinovasiuzanna.hw27.data.entities.contractor.ContractorContract

@Dao
interface ContractorDao {

    @Query("SELECT * FROM ${ContractorContract.TABLE_NAME}")
    suspend fun getAllContractors(): List<Contractor>

    @Query("SELECT * FROM ${ContractorContract.TABLE_NAME} WHERE ${ContractorContract.Columns.ID} =:contractorId")
    suspend fun getContractorById(contractorId: Int): Contractor

    @Query("DELETE FROM ${ContractorContract.TABLE_NAME} WHERE ${ContractorContract.Columns.ID} =:contractorId")
    suspend fun deleteContractor(contractorId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContractors(contractors: List<Contractor>)

}