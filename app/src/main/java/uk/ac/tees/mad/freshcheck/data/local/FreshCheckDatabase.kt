package uk.ac.tees.mad.freshcheck.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.tees.mad.freshcheck.data.local.converters.DateConverters
import uk.ac.tees.mad.freshcheck.data.local.dao.FoodDao
import uk.ac.tees.mad.freshcheck.data.local.entity.FoodItemEntity

@Database(
    entities = [FoodItemEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class FreshCheckDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}