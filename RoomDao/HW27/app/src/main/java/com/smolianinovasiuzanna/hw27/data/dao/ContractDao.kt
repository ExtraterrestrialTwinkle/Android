package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractContract
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesContract

@Dao
interface ContractDao {

    @Query("SELECT * FROM ${ContractContract.TABLE_NAME}")
    suspend fun getAllContracts(): List<Contract>

    @Query("SELECT * FROM ${ContractContract.TABLE_NAME} WHERE ${ContractContract.Columns.ID} =:contractId")
    suspend fun getContractById(contractId: Int): Contract

    @Query("DELETE FROM ${ContractContract.TABLE_NAME} WHERE ${ContractContract.Columns.ID} =:contractId")
    suspend fun deleteContract(contractId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContracts(contracts: List<Contract>)

    @Query("SELECT * FROM ${ContractContract.TABLE_NAME} WHERE ${ContractContract.Columns.CONTRACTOR_ID} =:contractorId")
    suspend fun showContractsOfContractor(contractorId: Int): List<Contract>

    @Update
    suspend fun updateContract(contract: Contract)
}