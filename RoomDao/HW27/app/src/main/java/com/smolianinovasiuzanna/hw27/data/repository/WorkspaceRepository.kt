package com.smolianinovasiuzanna.hw27.data.repository

import android.util.Patterns
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smolianinovasiuzanna.hw27.data.IncorrectFormException
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.workspace.Workspace
import com.smolianinovasiuzanna.hw27.data.entities.workspace.WorkspaceContract

class WorkspaceRepository {

    private val workspaceDao = Database.instance.workspaceDao()

    suspend fun getAllWorkspaces(): List<Workspace>{
        return workspaceDao.getAllWorkspaces()
    }

    suspend fun getWorkspaceById(workspaceId: Int): Workspace{
        return workspaceDao.getWorkspaceById(workspaceId)
    }

    suspend fun deleteWorkspace(workspaceId: Int){
        return workspaceDao.deleteWorkspace(workspaceId)

    }
    suspend fun saveWorkspace(workspace: Workspace){
        workspaceDao.insertWorkspaces(listOf(workspace))
    }

    suspend fun updateWorkspace(workspace: Workspace){
        workspaceDao.updateWorkspace(workspace)
    }

}