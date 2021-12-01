package com.smolianinovasiuzanna.hw27.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smolianinovasiuzanna.hw27.data.dao.*
import com.smolianinovasiuzanna.hw27.data.database.FactoryDatabase.Companion.DB_VERSION
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractStatusConverter
import com.smolianinovasiuzanna.hw27.data.entities.contractor.Contractor
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeeStatusConverter
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.factory.Factory
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectStatusConverter
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectsCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskStatusConverter
import com.smolianinovasiuzanna.hw27.data.entities.workspace.Workspace

@TypeConverters(EmployeeStatusConverter::class,ContractStatusConverter::class, ProjectStatusConverter::class, TaskStatusConverter::class )
@Database(entities = [
    Employee::class,
    Contract::class,
    Contractor::class,
    Department::class,
    Factory::class,
    Project::class,
    Task::class,
    Workspace::class,
    ProjectsCrossReferences::class,
    EmployeesCrossReferences::class
], version = DB_VERSION)
abstract class FactoryDatabase: RoomDatabase() {

    abstract fun contractDao(): ContractDao
    abstract fun contractorDao(): ContractorDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun factoryDao(): FactoryDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun workspaceDao(): WorkspaceDao
    abstract fun projectCrossReferenceDao(): ProjectsCrossReferenceDao
    abstract fun employeeCrossReferenceDao(): EmployeeCrossReferenceDao

    companion object{
        const val DB_VERSION = 2
        const val DB_NAME = "factory_database"
    }
}