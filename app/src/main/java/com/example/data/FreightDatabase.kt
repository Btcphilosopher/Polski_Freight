package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// --- Room Entities ---

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String,
    val cargoTypeEn: String,
    val cargoTypePl: String,
    val origin: String,
    val destination: String,
    val weightKg: Double,
    val dimensions: String,
    val pricePln: Double,
    val estimatedTollPln: Double,
    val status: String, // SCHEDULED, PICKED_UP, IN_TRANSIT, DELIVERED
    val isHazard: Boolean,
    val hazardClass: String, // ADR 3, etc.
    val specialInstructionsEn: String,
    val specialInstructionsPl: String,
    val customerName: String,
    val customerContact: String,
    val currentStep: Int = 0, // 0 = Accepted, 1 = Arrived at Pickup, 2 = Loaded, 3 = In Transit, 4 = Delivered
    val signatureData: String? = null, // Base64 signature lines
    val podPhotoUri: String? = null, // Local simulated image
    val qrCodePayload: String? = null,
    val deliveryTimestamp: Long? = null,
    val deliveryGeo: String? = null,
    val delayNoteEn: String? = null,
    val delayNotePl: String? = null
)

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val registration: String,
    val model: String,
    val statusEn: String,
    val statusPl: String,
    val fuelLevel: Int, // Percentage
    val nextService: String,
    val insuranceExpiry: String,
    val currentKm: Int,
    val averageConsumption: Double // L/100km
)

@Entity(tableName = "compliance")
data class ComplianceEntity(
    @PrimaryKey val driverId: String,
    val drivingHoursToday: Double, // Hours
    val restHoursToday: Double,
    val nextBreakDueMs: Long,
    val lastInspectionTimestamp: Long,
    val isVehicleInspected: Boolean,
    val currentFuelLogs: String // JSON string or simple comma list of values
)

// --- DAOs ---

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs ORDER BY id ASC")
    fun getAllJobsFlow(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :id LIMIT 1")
    suspend fun getJobById(id: String): JobEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jobs: List<JobEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: JobEntity)

    @Update
    suspend fun update(job: JobEntity)

    @Query("DELETE FROM jobs")
    suspend fun clearAll()
}

@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehicles")
    fun getAllVehiclesFlow(): Flow<List<VehicleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vehicles: List<VehicleEntity>)

    @Update
    suspend fun update(vehicle: VehicleEntity)
}

@Dao
interface ComplianceDao {
    @Query("SELECT * FROM compliance WHERE driverId = :driverId LIMIT 1")
    fun getComplianceFlow(driverId: String): Flow<ComplianceEntity?>

    @Query("SELECT * FROM compliance WHERE driverId = :driverId LIMIT 1")
    suspend fun getComplianceById(driverId: String): ComplianceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(compliance: ComplianceEntity)

    @Update
    suspend fun update(compliance: ComplianceEntity)
}

// --- Database Holder ---

@Database(
    entities = [JobEntity::class, VehicleEntity::class, ComplianceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FreightDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun complianceDao(): ComplianceDao
}
