package cn.shef.msc5.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cn.shef.msc5.todo.model.dto.SubTask
import cn.shef.msc5.todo.utilities.Constants.Companion.TABLE_TASK
import cn.shef.msc5.todo.utilities.DateConverter
import java.sql.Date
import java.time.OffsetDateTime

/**
 * @author Zhecheng Zhao
 * @email zzhao84@sheffield.ac.uk
 * @date Created in 04/11/2023 19:10
 */
@TypeConverters(DateConverter::class)
@Entity(tableName = TABLE_TASK)
data class Task(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var id: Int,

    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    var title: String,

    @ColumnInfo(name = "userId", typeAffinity = ColumnInfo.INTEGER)
    var userId: Int,

    @ColumnInfo(name = "description", typeAffinity = ColumnInfo.TEXT)
    var description: String,

    @ColumnInfo(name = "priority", typeAffinity = ColumnInfo.INTEGER)
    var priority: Int,

    @ColumnInfo(name = "longitude", typeAffinity = ColumnInfo.REAL)
    var longitude: Double,

    @ColumnInfo(name = "latitude", typeAffinity = ColumnInfo.REAL)
    var latitude: Double,

    @ColumnInfo(name = "imageUrl", typeAffinity = ColumnInfo.TEXT)
    var imageUrl: String,

    @ColumnInfo(name = "dueTime")
    var dueTime: Date,

    @ColumnInfo(name = "parentId", typeAffinity = ColumnInfo.INTEGER)
    var parentId: Int,

    @ColumnInfo(name = "gmtCreate")
    var gmtCreated: Date,

    @ColumnInfo(name = "gmtModified")
    var gmtModified: Date,

    @ColumnInfo(name = "isDeleted", typeAffinity = ColumnInfo.INTEGER)
    var isDeleted: Int,

    @ColumnInfo(name = "state")
    var state: Int,

    @ColumnInfo(name = "subTasks")
    var subTasks: List<SubTask>

){
    override fun toString(): String {
        return "Task(id=$id, title=$title, description=$description, priority=$priority, longitude=$longitude, latitude=$latitude, imageUrl=$imageUrl, dueTime=$dueTime, parentId=$parentId, gmtCreated=$gmtCreated, gmtModified=$gmtModified, isDeleted=$isDeleted, state=$state, subTasks=$subTasks)"
    }
}
