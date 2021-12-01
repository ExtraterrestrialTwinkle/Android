package com.smolianinovasiuzanna.hw27.ui.project_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectStatus
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectWithDepartments
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectsCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.repository.DepartmentRepository
import com.smolianinovasiuzanna.hw27.data.repository.ProjectRepository
import com.smolianinovasiuzanna.hw27.data.repository.TaskRepository
import kotlinx.coroutines.launch

class ProjectInfoViewModel : ViewModel() {

    private val departmentRepository = DepartmentRepository()
    private val projectRepository = ProjectRepository()
    private val taskRepository = TaskRepository()

    private val departmentsLiveData = MutableLiveData<List<Department>>()
    private val departmentsSearchLiveData = MutableLiveData<List<Department>>()
    private val tasksLiveData = MutableLiveData<List<Task>>()
    private val statusLiveData = MutableLiveData<ProjectStatus>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val departments: LiveData<List<Department>>
        get() = departmentsLiveData

    val tasks: LiveData<List<Task>>
        get() = tasksLiveData

    val departmentsSearch: LiveData<List<Department>>
        get() = departmentsSearchLiveData

    val status: LiveData<ProjectStatus>
        get() = statusLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun loadDepartments(projectId: Int) {
        try {
            viewModelScope.launch {
                Log.d("ProjectInfoVM", "loadDepartments(project)")
                departmentsLiveData.postValue(projectRepository.getProjectWithDepartments(projectId))
            }
        } catch (t: Throwable) {
            errorLiveData.postValue(t)
            departmentsLiveData.postValue(emptyList())
        }
    }

    fun loadTasks(projectId: Int) {
        try {
            Log.d("ProjectInfoVM", "loadTasks")
            viewModelScope.launch {
                tasksLiveData.postValue(projectRepository.getProjectWithTasks(projectId))
            }
        } catch (t: Throwable) {
            errorLiveData.postValue(t)
            tasksLiveData.postValue(emptyList())
        }
    }

    fun getDepartmentsListToChoose() {
        try {
            viewModelScope.launch {
                Log.d("ProjectInfoVM", "loadDepartments(searchList)")
                departmentsSearchLiveData.postValue(departmentRepository.getAllDepartments())
            }
        } catch (t: Throwable) {
            errorLiveData.postValue(t)
            departmentsSearchLiveData.postValue(emptyList())
        }
    }

    fun addDepartmentRelationToProject(projectId: Int, departmentId: Int) {
        Log.d("ProjectInfoViewModel", "addDepartmentRelationToProject")
        try {
            viewModelScope.launch {
                Database.instance.withTransaction {
                    val project = projectRepository.getProjectById(projectId)
                    val department = departmentRepository.getDepartmentById(departmentId)
                    Log.d("ProjectInfoViewModel", "project = $project, department = $department")
                    if(project !=null && department !=null){
                        Log.d("ProjectInfoViewModel", "reference")
                        if(projectRepository.getProjectWithDepartments(projectId).contains(department)) return@withTransaction
                        val reference = ProjectsCrossReferences(departmentId, projectId)
                        projectRepository.saveProjectWithDepartment(reference)
                        loadDepartments(projectId)
                    } else errorLiveData.postValue(Throwable("Invalid data"))
                }
            }
        } catch (t: Throwable) {
            errorLiveData.postValue(t)
            loadDepartments(projectId)
        }
    }

    fun removeRelation(projectId: Int, departmentId: Int){
        Log.d("ProjectInfo", "Remove Relation")
        try {
            viewModelScope.launch {
                Database.instance.withTransaction {
                    val department = departmentRepository.getDepartmentById(departmentId)
                    if (department != null) {
                        projectRepository.removeRelationBetweenProjectAndDepartment(
                            projectId,
                            departmentId
                        )
                    }
                    loadDepartments(projectId)
                }
            }
        }catch(t: Throwable){
            errorLiveData.postValue(t)
        }
    }

    fun loadStatus(projectId: Int) {
        try {
            viewModelScope.launch {
                val project = projectRepository.getProjectById(projectId)
                statusLiveData.postValue(project.status)
            }
        } catch (t: Throwable) {
            errorLiveData.postValue(t)
            statusLiveData.postValue(ProjectStatus.INPROGRESS)
        }
    }

    fun deleteProject(projectId: Int){
        try{
            viewModelScope.launch {
                Database.instance.withTransaction {
                    val tasksList = projectRepository.getProjectWithTasks(projectId)
                    tasksList.map{task ->
                        taskRepository.deleteTask(task.id)
                    }
                    projectRepository.removeAllRelationsOfProject(projectId)
                    projectRepository.deleteProject(projectId)
                }
            }
        }catch(t: Throwable) {
            errorLiveData.postValue(t)
        }
    }

    fun deleteTask(taskId: Int, projectId: Int){
        try{
            viewModelScope.launch {
                taskRepository.deleteTask(taskId)
                loadTasks(projectId)
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
        }
    }
}