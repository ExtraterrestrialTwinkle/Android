package com.smolianinovasiuzanna.hw27.data.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smolianinovasiuzanna.hw27.data.IncorrectFormException
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee

class ContractRepository {

    private val contractDao = Database.instance.contractDao()

    suspend fun getAllContracts(): List<Contract>{
        return contractDao.getAllContracts()
    }

    suspend fun getContractById(contractId: Int): Contract{
        return contractDao.getContractById(contractId)
    }

    suspend fun deleteContract(contractId: Int){
        return contractDao.deleteContract(contractId)
    }

    suspend fun insertContract(contract: Contract){
        contractDao.insertContracts(listOf(contract))
    }

    suspend fun getContractsOfContractor(contractorId: Int): List<Contract>{
        return contractDao.showContractsOfContractor(contractorId)
    }

    suspend fun updateContract(contract: Contract){
        contractDao.updateContract(contract)
    }

}