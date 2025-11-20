package uk.ac.tees.mad.freshcheck.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.freshcheck.data.local.FreshCheckDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): FreshCheckDatabase =
        Room.databaseBuilder(
            context,
            FreshCheckDatabase::class.java,
            "freshcheck.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideFoodDao(db: FreshCheckDatabase) = db.foodDao()
}
