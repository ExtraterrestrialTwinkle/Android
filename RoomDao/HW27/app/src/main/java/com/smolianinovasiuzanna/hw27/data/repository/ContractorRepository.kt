package com.smolianinovasiuzanna.hw27.data.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.contractor.Contractor
import com.smolianinovasiuzanna.hw27.data.entities.contractor.ContractorContract

class ContractorRepository {

    private val contractorDao = Database.instance.contractorDao()

    suspend fun getAllContractors(): List<Contractor>{
        return contractorDao.getAllContractors()
    }

    suspend fun getContractorById(contractorId: Int): Contractor{
        return contractorDao.getContractorById(contractorId)
    }

    suspend fun deleteContractor(contractorId: Int){
        return contractorDao.deleteContractor(contractorId)
    }

    suspend fun saveContractors(contractor: Contractor){
        contractorDao.insertContractors(listOf(contractor))
    }
}