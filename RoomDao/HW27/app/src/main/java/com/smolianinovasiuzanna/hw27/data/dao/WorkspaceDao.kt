package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskContract
import com.smolianinovasiuzanna.hw27.data.entities.workspace.Workspace
import com.smolianinovasiuzanna.hw27.data.entities.workspace.WorkspaceContract

@Dao
interface WorkspaceDao {

    @Query("SELECT * FROM ${WorkspaceContract.TABLE_NAME}")
    suspend fun getAllWorkspaces(): List<Workspace>

    @Query("SELECT * FROM ${WorkspaceContract.TABLE_NAME} WHERE ${WorkspaceContract.Columns.ID} =:workspaceId")
    suspend fun getWorkspaceById(workspaceId: Int): Workspace

    @Query("DELETE FROM ${WorkspaceContract.TABLE_NAME} WHERE ${WorkspaceContract.Columns.ID} =:workspaceId")
    suspend fun deleteWorkspace(workspaceId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkspaces(workspaces: List<Workspace>)

    @Update
    suspend fun updateWorkspace(workspace: Workspace)


}