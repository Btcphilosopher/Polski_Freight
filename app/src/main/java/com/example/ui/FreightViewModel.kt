package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiClient
import com.example.data.ComplianceEntity
import com.example.data.FreightRepository
import com.example.data.JobEntity
import com.example.data.VehicleEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole {
    DRIVER, DISPATCHER, CUSTOMER, ARCHITECTURE
}

class FreightViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FreightRepository(application)

    // Language state: true = Polish, false = English
    private val _isPolish = MutableStateFlow(true)
    val isPolish: StateFlow<Boolean> = _isPolish.asStateFlow()

    // Active Role in Ecosystem Simulator
    private val _activeRole = MutableStateFlow(UserRole.DRIVER)
    val activeRole: StateFlow<UserRole> = _activeRole.asStateFlow()

    // Database flows
    val jobs: StateFlow<List<JobEntity>> = repository.allJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vehicles: StateFlow<List<VehicleEntity>> = repository.allVehicles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val compliance: StateFlow<ComplianceEntity?> = repository.getComplianceFlow("WA-DRIVER")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Selected Driver Job
    private val _selectedJobId = MutableStateFlow<String?>("PF-9082")
    val selectedJobId: StateFlow<String?> = _selectedJobId.asStateFlow()

    // AI Copilot Advice state
    private val _aiAdvice = MutableStateFlow<String?>(null)
    val aiAdvice: StateFlow<String?> = _aiAdvice.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Dynamic Live Tracking Simulator coordinates (progress percent: 0.0 to 1.0)
    private val _transitProgress = MutableStateFlow(0.0)
    val transitProgress: StateFlow<Double> = _transitProgress.asStateFlow()

    // B2C Instant Booking State
    private val _bookingOrigin = MutableStateFlow("Poznań, PL")
    val bookingOrigin: StateFlow<String> = _bookingOrigin.asStateFlow()

    private val _bookingDestination = MutableStateFlow("Warszawa, PL")
    val bookingDestination: StateFlow<String> = _bookingDestination.asStateFlow()

    private val _bookingWeight = MutableStateFlow(5000.0)
    val bookingWeight: StateFlow<Double> = _bookingWeight.asStateFlow()

    private val _bookingCargo = MutableStateFlow("Consumer Electronics")
    val bookingCargo: StateFlow<String> = _bookingCargo.asStateFlow()

    private val _bookingIsExpress = MutableStateFlow(false)
    val bookingIsExpress: StateFlow<Boolean> = _bookingIsExpress.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDefaultData(force = false)
        }
    }

    fun toggleLanguage() {
        _isPolish.value = !_isPolish.value
        // Clear old AI advice to get translated advice if queried again
        _aiAdvice.value = null
    }

    fun setRole(role: UserRole) {
        _activeRole.value = role
    }

    fun selectJob(jobId: String?) {
        _selectedJobId.value = jobId
        _aiAdvice.value = null
    }

    fun setBookingOrigin(origin: String) { _bookingOrigin.value = origin }
    fun setBookingDestination(dest: String) { _bookingDestination.value = dest }
    fun setBookingWeight(weight: Double) { _bookingWeight.value = weight }
    fun setBookingCargo(cargo: String) { _bookingCargo.value = cargo }
    fun setBookingIsExpress(express: Boolean) { _bookingIsExpress.value = express }

    // --- Pricing Engine ---
    fun calculateEstimatedPrice(origin: String, dest: String, weightKg: Double, isExpress: Boolean): PricingBreakdown {
        // Simple mock calculations based on polish distances
        val baseDistanceKm = when {
            origin.contains("Warszawa", true) && dest.contains("Berlin", true) -> 570
            origin.contains("Gdańsk", true) && dest.contains("Kraków", true) -> 600
            origin.contains("Poznań", true) && dest.contains("Wrocław", true) -> 180
            origin.contains("Poznań", true) && dest.contains("Warszawa", true) -> 310
            else -> 250
        }

        val ratePerKm = 3.5 // PLN per km for heavy freight
        val weightSurcharge = (weightKg / 1000.0) * 120.0 // 120 PLN per ton
        val fuelSurchargePercent = 0.22 // 22% fuel cost indexing
        val tollCostEst = if (baseDistanceKm > 400) 450.0 else 180.0
        val speedMultiplier = if (isExpress) 1.4 else 1.0

        val basePrice = (baseDistanceKm * ratePerKm + weightSurcharge) * speedMultiplier
        val fuelSurcharge = basePrice * fuelSurchargePercent
        val totalPln = basePrice + fuelSurcharge + tollCostEst

        return PricingBreakdown(
            distanceKm = baseDistanceKm,
            basePricePln = basePrice,
            fuelSurchargePln = fuelSurcharge,
            tollPln = tollCostEst,
            totalPln = totalPln
        )
    }

    // --- Route Optimisation Engine & Live Tracking Sim ---
    fun startTransitSimulation() {
        viewModelScope.launch {
            _transitProgress.value = 0.0
            for (i in 1..10) {
                kotlinx.coroutines.delay(1000)
                _transitProgress.value = i * 0.1
            }
        }
    }

    // --- Action Methods ---

    fun resetData() {
        viewModelScope.launch {
            repository.initializeDefaultData(force = true)
            _selectedJobId.value = "PF-9082"
            _aiAdvice.value = null
            _transitProgress.value = 0.0
        }
    }

    fun queryAiCopilot(job: JobEntity) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiAdvice.value = null
            try {
                val advice = GeminiClient.getLogisticsCopilotAdvice(
                    cargoName = job.cargoTypeEn,
                    origin = job.origin,
                    destination = job.destination,
                    weightKg = job.weightKg,
                    isHazard = job.isHazard,
                    langPl = _isPolish.value
                )
                _aiAdvice.value = advice
            } catch (e: Exception) {
                _aiAdvice.value = "Failed to query AI copilot: ${e.localizedMessage}"
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun advanceDriverStep(job: JobEntity) {
        viewModelScope.launch {
            val nextStep = job.currentStep + 1
            val newStatus = when (nextStep) {
                0 -> "SCHEDULED"
                1 -> "ACCEPTED"
                2 -> "ARRIVED_PICKUP"
                3 -> "IN_TRANSIT"
                4 -> "DELIVERED"
                else -> "DELIVERED"
            }

            val updatedJob = job.copy(
                currentStep = if (nextStep <= 4) nextStep else 4,
                status = newStatus,
                deliveryTimestamp = if (nextStep == 4) System.currentTimeMillis() else null,
                deliveryGeo = if (nextStep == 4) "52.229770, 21.011780 (Warszawa Depot)" else null
            )
            repository.updateJob(updatedJob)

            // Simulate driving hours and compliance updates
            val currentCompliance = compliance.value
            if (currentCompliance != null) {
                val updatedHours = currentCompliance.drivingHoursToday + 1.5
                val updatedComp = currentCompliance.copy(
                    drivingHoursToday = if (updatedHours <= 9.0) updatedHours else 9.0,
                    restHoursToday = if (updatedHours > 9.0) 9.0 else 11.0
                )
                repository.updateCompliance(updatedComp)
            }

            if (newStatus == "IN_TRANSIT") {
                startTransitSimulation()
            }
        }
    }

    fun performVehicleInspection() {
        viewModelScope.launch {
            val currentCompliance = compliance.value
            if (currentCompliance != null) {
                val updated = currentCompliance.copy(
                    isVehicleInspected = true,
                    lastInspectionTimestamp = System.currentTimeMillis()
                )
                repository.updateCompliance(updated)
            }
        }
    }

    fun logFuel(amountLiters: Int, plnCost: Double) {
        viewModelScope.launch {
            val currentCompliance = compliance.value
            if (currentCompliance != null) {
                val updatedLogs = currentCompliance.currentFuelLogs + ",$amountLiters"
                val updated = currentCompliance.copy(currentFuelLogs = updatedLogs)
                repository.updateCompliance(updated)
            }
        }
    }

    fun reportIncident(reasonEn: String, reasonPl: String) {
        viewModelScope.launch {
            val activeId = _selectedJobId.value ?: return@launch
            val job = repository.getJobById(activeId) ?: return@launch
            val updated = job.copy(
                delayNoteEn = "[INCIDENT REPORTED] $reasonEn",
                delayNotePl = "[ZGŁOSZONO INCYDENT] $reasonPl"
            )
            repository.updateJob(updated)
        }
    }

    fun uploadPod(job: JobEntity, signature: String, qrCode: String) {
        viewModelScope.launch {
            val updated = job.copy(
                signatureData = signature,
                qrCodePayload = qrCode,
                podPhotoUri = "android.resource://com.example/drawable/ic_launcher_foreground",
                status = "DELIVERED",
                currentStep = 4,
                deliveryTimestamp = System.currentTimeMillis(),
                deliveryGeo = "52.520008, 13.404954 (Berlin Hub)"
            )
            repository.updateJob(updated)
        }
    }

    fun bookShipment(cargo: String, origin: String, dest: String, weightKg: Double, isExpress: Boolean) {
        viewModelScope.launch {
            val breakdown = calculateEstimatedPrice(origin, dest, weightKg, isExpress)
            val newId = "PF-${(1000..9999).random()}"
            val newJob = JobEntity(
                id = newId,
                cargoTypeEn = cargo,
                cargoTypePl = cargo,
                origin = origin,
                destination = dest,
                weightKg = weightKg,
                dimensions = "Custom Cargo Dimensions (LTL)",
                pricePln = breakdown.totalPln,
                estimatedTollPln = breakdown.tollPln,
                status = "SCHEDULED",
                isHazard = false,
                hazardClass = "None",
                specialInstructionsEn = "B2C Booked Client Shipment. Deliver safely.",
                specialInstructionsPl = "Przesyłka zarezerwowana przez portal B2C. Dostarczyć bezpiecznie.",
                customerName = "B2C Web Customer",
                customerContact = "+48 500 200 100",
                currentStep = 0
            )
            repository.insertJob(newJob)
            _selectedJobId.value = newId
        }
    }

    fun dispatcherAssignJob(jobId: String, regNo: String) {
        viewModelScope.launch {
            val job = repository.getJobById(jobId) ?: return@launch
            val updated = job.copy(
                specialInstructionsEn = job.specialInstructionsEn + " (Assigned to fleet vehicle $regNo)",
                specialInstructionsPl = job.specialInstructionsPl + " (Przypisano do pojazdu $regNo)"
            )
            repository.updateJob(updated)

            // Update Vehicle status
            val fleet = vehicles.value
            val v = fleet.find { it.registration == regNo }
            if (v != null) {
                repository.updateVehicle(v.copy(
                    statusEn = "Assigned / On Job $jobId",
                    statusPl = "Przypisany / Trasa $jobId"
                ))
            }
        }
    }
}

data class PricingBreakdown(
    val distanceKm: Int,
    val basePricePln: Double,
    val fuelSurchargePln: Double,
    val tollPln: Double,
    val totalPln: Double
)
