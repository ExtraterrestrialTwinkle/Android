package com.smolianinovasiuzanna.hw27.data.repository

import android.util.Log
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectsCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.task.Task

class ProjectRepository {

    private val projectDao = Database.instance.projectDao()
    private val projectWithReferenceDao = Database.instance.projectCrossReferenceDao()

    suspend fun getAllProjects(): List<Project>{
        return projectDao.getAllProjects()
    }

    suspend fun getProjectById(projectId: Int): Project{
        return projectDao.getProjectById(projectId)
    }

    suspend fun deleteProject(projectId: Int){
        return projectDao.deleteProject(projectId)
    }

    suspend fun saveProject(project: Project){
        projectDao.insertProjects(listOf(project))
    }

    suspend fun updateProject(project: Project){
        projectDao.updateProject(project)
    }

    suspend fun saveProjectWithDepartment(reference: ProjectsCrossReferences){
        Log.d("ProjectRepository", "saveProjectWithDepartment")
        val i = projectWithReferenceDao.insertProjectWithDepartment(listOf(reference))
        Log.d("ProjectRepository", "saveProjectWithDepartment after insertion. id = $i ")
    }

    suspend fun getProjectWithDepartments(projectId: Int): List<Department> {
        Log.d("ProjectRepo", "getProjectWithDepartments projectID = $projectId")
        val listProjectWithRelations = projectWithReferenceDao.getProjectWithDepartments(projectId)
        Log.d("Repo getProjectWithDeps", "projectWithRelations = $listProjectWithRelations")
        val deps = listProjectWithRelations.departments
        Log.d("Repo getProjectWithDeps", "departments = $deps")
        return deps
    }

    suspend fun getProjectWithTasks(projectId: Int): List<Task>{
        val listProjectWithTasks = projectDao.getProjectsTasks(projectId)
        return listProjectWithTasks.task
    }
    suspend fun removeRelationBetweenProjectAndDepartment(projectId: Int, departmentId: Int){
        projectWithReferenceDao.removeProjectWithDepartment(projectId, departmentId)
        Log.d("ProjectRepo", "Remove Relation")
    }
    suspend fun removeAllRelationsOfProject(projectId: Int){
        projectWithReferenceDao.removeAllRelationsOfProject(projectId)

    }}