package com.example.myapplication.data

import androidx.lifecycle.ViewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields

enum class Sort {
    PLZ,
    MONTH,
}

enum class Order {
    ASC,
}
/******************************* UserData ********************************/
@Entity
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    //Address
    var plz: Int = 12345,
    var place: String = "Musterhausen",
    var str: String = "Musterstraße",
    var nr: Int = 6,

    //Preferences
    var B: Boolean = false, //Biotonne
    var G: Boolean = false, //Gelbe Tonne
    var P: Boolean = false, //Papiertonne
    var R: Boolean = false, //Restmülltonne
)

@Dao
interface UserDataBase {
    @Upsert
    suspend fun upsertUser(dataModel: User)

    @Update
    suspend fun updateUser(dataModel: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user ORDER BY plz ASC")
    fun sortByPLZ(): Flow<List<User>>

    @Query("SELECT * FROM user ORDER BY str ASC")
    fun sortBySTR(): Flow<List<User>>

    @RawQuery(observedEntities = [User::class])
    fun query(query: SupportSQLiteQuery): Flow<List<User>>
}
@Database(entities = [User::class], version = 1)
abstract class UserModelDataBase: RoomDatabase() {
    abstract val dao: UserDataBase
}

class UserViewModel(
    private val dao: UserDataBase
): ViewModel() {
    suspend fun addUser(id: Int, place: String, str: String, nr: Int, prefs: Array<Boolean>) {
        val newUser = User(
            id = id,

            place = place,
            str = str,
            nr = nr,

            B = prefs[0],
            G = prefs[1],
            P = prefs[2],
            R = prefs[3],
        )
        dao.upsertUser(newUser)
    }
    suspend fun updateUserData(user: User, place: String, str: String, nr: Int, prefs: Array<Boolean>) {
        if(place != "" && str != "" && nr != 0) {
            user.place = place
            user.str = str
            user.nr = nr
        }

        user.B = prefs[0]
        user.G = prefs[1]
        user.P = prefs[2]
        user.R = prefs[3]

        dao.updateUser(user)
    }

    fun getAllUsers(): Flow<List<User>> {
        return dao.getAllUsers()
    }

    fun sortBy(sort: Sort): Flow<List<User>> {
        if (sort == Sort.PLZ) {
            return dao.sortByPLZ()
        }
        return dao.sortBySTR()
    }

    fun query(
        tableName: String = "userdata",
        filter: String? = null,
        sort: Sort? = null,
        order: Order = Order.ASC
    ): Flow<List<User>> {
        var queryText = "SELECT * FROM $tableName"

        if (filter != null) {
            queryText += " WHERE $filter"
        }

        if (sort != null) {
            queryText += " ORDER BY ${sort.name}"
            queryText += " ${order.name}"
        }

        return dao.query(SimpleSQLiteQuery(queryText))
    }
}
/******************************* UserData ********************************/

/****************************** TrashData ********************************/
@Entity
data class TrashEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    //type of trash
    var type: String,   //eg: G  -> Gelbe Tonne

    //disposal dates
    var month: Int,
    var week: Int,
    var day: Int,
    var year: Int
)

@Dao
interface TrashDataBase {
    @Upsert
    suspend fun upsertTrashEntity(dataModel: TrashEntity)

    @Update
    suspend fun updateTrashEntity(dataModel: TrashEntity)

    @Query("SELECT * FROM trashentity")
    fun getAllTrashData(): Flow<List<TrashEntity>>

    @Query("SELECT * FROM trashentity ORDER BY day ASC")
    fun sortByDay(): Flow<List<TrashEntity>>

    @Query("SELECT * FROM trashentity ORDER BY month ASC")
    fun sortByMonth(): Flow<List<TrashEntity>>

    @RawQuery(observedEntities = [TrashEntity::class])
    fun query(query: SupportSQLiteQuery): Flow<List<TrashEntity>>
}

@Database(entities = [TrashEntity::class], version = 1)
abstract class TrashEntityDatabase: RoomDatabase() {
    abstract val dao: TrashDataBase
}

class TrashViewModel(
    private val dao: TrashDataBase
): ViewModel() {
    suspend fun addTrashData(date: LocalDate, trashType: String) {
        val newTrashEntity = TrashEntity(
            type = trashType,
            month = date.monthValue-1,
            week = LocalDate.of(date.year, date.monthValue, date.dayOfMonth)
                .get(WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear()),
            day = date.dayOfMonth,
            year = date.year
        )
        dao.upsertTrashEntity(newTrashEntity)
    }
    suspend fun updateTrashData(trashEntity: TrashEntity, date: LocalDate, trashType: String){
        trashEntity.type = trashType
        trashEntity.month = date.monthValue-1
        trashEntity.week = LocalDate.of(date.year, date.monthValue, date.dayOfMonth)
            .get(WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear())
        trashEntity.day = date.dayOfMonth
        trashEntity.year = date.year

        dao.updateTrashEntity(trashEntity)
    }

    fun getAllTrashData(): Flow<List<TrashEntity>> {
        return dao.getAllTrashData()
    }

    fun sortBy(sort: Sort): Flow<List<TrashEntity>> {
        if (sort == Sort.MONTH) {
            return dao.sortByMonth()
        }
        return dao.sortByDay()
    }

    fun query(
        tableName: String = "trashdata",
        filter: String? = null,
        sort: Sort? = null,
        order: Order = Order.ASC
    ): Flow<List<TrashEntity>> {
        var queryText = "SELECT * FROM $tableName"

        if (filter != null) {
            queryText += " WHERE $filter"
        }

        if (sort != null) {
            queryText += " ORDER BY ${sort.name}"
            queryText += " ${order.name}"
        }

        return dao.query(SimpleSQLiteQuery(queryText))
    }
}
/****************************** TrashData ********************************/
