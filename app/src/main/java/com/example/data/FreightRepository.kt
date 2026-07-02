package com.example.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FreightRepository(context: Context) {

    private val db: FreightDatabase = Room.databaseBuilder(
        context.applicationContext,
        FreightDatabase::class.java,
        "polski_freight_os_db"
    ).fallbackToDestructiveMigration().build()

    private val jobDao = db.jobDao()
    private val vehicleDao = db.vehicleDao()
    private val complianceDao = db.complianceDao()

    val allJobs: Flow<List<JobEntity>> = jobDao.getAllJobsFlow()
    val allVehicles: Flow<List<VehicleEntity>> = vehicleDao.getAllVehiclesFlow()
    
    fun getComplianceFlow(driverId: String): Flow<ComplianceEntity?> {
        return complianceDao.getComplianceFlow(driverId)
    }

    suspend fun getJobById(id: String): JobEntity? = jobDao.getJobById(id)

    suspend fun insertJob(job: JobEntity) = jobDao.insert(job)
    suspend fun updateJob(job: JobEntity) = jobDao.update(job)
    
    suspend fun updateVehicle(vehicle: VehicleEntity) = vehicleDao.update(vehicle)
    suspend fun updateCompliance(compliance: ComplianceEntity) = complianceDao.update(compliance)

    suspend fun initializeDefaultData(force: Boolean = false) {
        val existingJobs = jobDao.getAllJobsFlow().first()
        if (existingJobs.isNotEmpty() && !force) {
            return
        }

        jobDao.clearAll()

        // Default Freight Jobs (Poland & EU Cross Border)
        val defaultJobs = listOf(
            JobEntity(
                id = "PF-9082",
                cargoTypeEn = "Premium Automotive Parts",
                cargoTypePl = "Części samochodowe premium",
                origin = "Warszawa Depot, PL",
                destination = "Berlin Logistics Park, DE",
                weightKg = 18500.0,
                dimensions = "13.6m LTL (34 Euro Pallets)",
                pricePln = 5400.0,
                estimatedTollPln = 850.0,
                status = "SCHEDULED",
                isHazard = true,
                hazardClass = "ADR Class 9 (Lithium Batteries)",
                specialInstructionsEn = "Keep ventilation ON. Monitor battery cargo temps. EU toll transponder active.",
                specialInstructionsPl = "Wentylacja włączona. Monitoruj temperaturę baterii. Aktywny transponder opłat drogowych.",
                customerName = "AutoTech Polska Sp. z o.o.",
                customerContact = "+48 22 555 1234",
                currentStep = 0
            ),
            JobEntity(
                id = "PF-8441",
                cargoTypeEn = "Frozen Seafood Consignment",
                cargoTypePl = "Mrożone owoce morza",
                origin = "Gdańsk Deepwater Terminal, PL",
                destination = "Kraków Distribution Center, PL",
                weightKg = 12000.0,
                dimensions = "Reefer Box (24 Pallets)",
                pricePln = 3800.0,
                estimatedTollPln = 320.0,
                status = "SCHEDULED",
                isHazard = false,
                hazardClass = "None",
                specialInstructionsEn = "Keep reefer set to exactly -18°C. Report temperature spikes immediately.",
                specialInstructionsPl = "Utrzymuj temperaturę dokładnie -18°C. Natychmiast zgłaszaj skoki temperatury.",
                customerName = "Baltic Cold Chain S.A.",
                customerContact = "+48 58 777 9876",
                currentStep = 0
            ),
            JobEntity(
                id = "PF-7711",
                cargoTypeEn = "Solar Farm Inverters",
                cargoTypePl = "Falowniki do farm słonecznych",
                origin = "Poznań Manufacturing Hub, PL",
                destination = "Wrocław Green Energy, PL",
                weightKg = 8400.0,
                dimensions = "Tautliner (15 Pallets)",
                pricePln = 2900.0,
                estimatedTollPln = 150.0,
                status = "DELIVERED",
                isHazard = false,
                hazardClass = "None",
                specialInstructionsEn = "Fragile electronics. Double-strap required to prevent sliding.",
                specialInstructionsPl = "Delikatna elektronika. Wymagane podwójne pasy mocujące, aby zapobiec przesunięciom.",
                customerName = "EkoEnergia Grupa Sp. z o.o.",
                customerContact = "+48 61 222 5544",
                currentStep = 4,
                signatureData = "Simulated Signature - Jan Kowalski",
                podPhotoUri = "android.resource://com.example/drawable/ic_launcher_foreground",
                qrCodePayload = "QR-PF-7711-OK",
                deliveryTimestamp = System.currentTimeMillis() - 7200000,
                deliveryGeo = "51.107883, 17.038538"
            ),
            JobEntity(
                id = "PF-3420",
                cargoTypeEn = "Medical Vaccines (Express)",
                cargoTypePl = "Szczepionki medyczne (Ekspres)",
                origin = "Katowice Bio-Hub, PL",
                destination = "Warszawa Central Hospital, PL",
                weightKg = 1500.0,
                dimensions = "Sprinter Van (4 Pallets)",
                pricePln = 4500.0,
                estimatedTollPln = 120.0,
                status = "SCHEDULED",
                isHazard = false,
                hazardClass = "None",
                specialInstructionsEn = "Ultra-critical cooling. Real-time GPS logger attached. GPS geofence enabled.",
                specialInstructionsPl = "Chłodzenie krytyczne. Podłączony rejestrator GPS czasu rzeczywistego.",
                customerName = "MedPolska Distribution",
                customerContact = "+48 32 444 8811",
                currentStep = 0
            )
        )
        jobDao.insertAll(defaultJobs)

        // Default Vehicles
        val defaultVehicles = listOf(
            VehicleEntity(
                registration = "WA 9842A",
                model = "Scania R450 Streamline (Euro 6)",
                statusEn = "In Transit",
                statusPl = "W trasie",
                fuelLevel = 82,
                nextService = "2026-10-15",
                insuranceExpiry = "2027-02-14",
                currentKm = 342110,
                averageConsumption = 28.5
            ),
            VehicleEntity(
                registration = "GD 3215F",
                model = "Volvo FH16 (Euro 6)",
                statusEn = "Available",
                statusPl = "Dostępny",
                fuelLevel = 45,
                nextService = "2026-08-08",
                insuranceExpiry = "2026-11-20",
                currentKm = 189430,
                averageConsumption = 31.2
            ),
            VehicleEntity(
                registration = "KR 7721Y",
                model = "MAN TGX 18.500",
                statusEn = "In Service Shop",
                statusPl = "W serwisie",
                fuelLevel = 95,
                nextService = "2026-07-12",
                insuranceExpiry = "2026-12-05",
                currentKm = 422050,
                averageConsumption = 29.8
            )
        )
        vehicleDao.insertAll(defaultVehicles)

        // Default Driver Compliance for WA-DRIVER
        val compliance = ComplianceEntity(
            driverId = "WA-DRIVER",
            drivingHoursToday = 4.5,
            restHoursToday = 11.0,
            nextBreakDueMs = System.currentTimeMillis() + 10800000, // 3 hours from now
            lastInspectionTimestamp = System.currentTimeMillis() - 14400000, // 4 hours ago
            isVehicleInspected = true,
            currentFuelLogs = "28.5,29.1,28.2,30.5,28.7"
        )
        complianceDao.insert(compliance)
    }
}
