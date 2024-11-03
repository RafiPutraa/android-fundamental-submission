package com.dicoding.myandroidfundamentalsubmission.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_event")
data class FavoriteEventEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "ownerName")
    var ownerName: String? = null,

    @ColumnInfo(name = "mediaCover")
    var mediaCover: String? = null,

    @ColumnInfo(name = "beginTime")
    var beginTime: String? = null,

    @ColumnInfo(name = "quota")
    var quota: Int = 0,

    @ColumnInfo(name = "registrants")
    var registrants: Int = 0,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "link")
    var link: String? = null,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean
)
