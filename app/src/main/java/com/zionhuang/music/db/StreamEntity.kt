package com.zionhuang.music.db

import androidx.room.Entity

@Entity(tableName = "stream")
data class StreamEntity(
        val id: String,
        val mimeType: String,
        val ext: String,
        val abr: Int,
)
