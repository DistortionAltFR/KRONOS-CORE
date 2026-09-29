package com.kronos.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kronos.core.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Query("UPDATE alerts SET resolved = 1 WHERE id = :id")
    suspend fun resolveAlert(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alerts: List<AlertEntity>)

    @Query("SELECT COUNT(*) FROM alerts")
    suspend fun getAlertCount(): Int
}
