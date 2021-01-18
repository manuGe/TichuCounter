package com.manuel.tichucounter.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
data class Preference(
    @PrimaryKey val preference: String,
    @ColumnInfo(name = "preference_value") val value: String
)