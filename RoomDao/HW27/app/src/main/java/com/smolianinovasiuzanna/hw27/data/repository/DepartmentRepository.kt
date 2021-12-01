package com.smolianinovasiuzanna.hw27.data.repository

import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.department.Department

class DepartmentRepository {

    private val departmentDao = Database.instance.departmentDao()
    private val projectCrossReferenceDao = Database.instance.projectCrossReferenceDao()

    suspend fun getAllDepartments(): List<Department>{
        return departmentDao.getAllDepartments()
    }

    suspend fun getDepartmentById(departmentId: Int): Department{
        return departmentDao.getDepartmentById(departmentId)
    }

    suspend fun deleteDepartment(departmentId: Int){
        return departmentDao.deleteDepartment(departmentId)
    }

    suspend fun saveDepartments(department: Department){
        departmentDao.insertDepartments(listOf(department))
    }
//    suspend fun getDepartmentWithProjects(departmentId: Int): List<ProjectsWithDepartments>{
//        return projectCrossReferenceDao.getDepartmentWithProjects(departmentId)
//    }

}