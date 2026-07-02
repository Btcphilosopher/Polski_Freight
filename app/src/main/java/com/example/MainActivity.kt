package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FireTruck
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JobEntity
import com.example.data.VehicleEntity
import com.example.ui.FreightViewModel
import com.example.ui.UserRole
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: FreightViewModel by viewModels()

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel)
            }
        }
    }
}

// --- Bilingual Localisation Provider ---
object Loc {
    fun get(key: String, isPl: Boolean): String {
        return if (isPl) PL[key] ?: key else EN[key] ?: key
    }

    private val EN = mapOf(
        "app_title" to "Polski Freight OS",
        "app_subtitle" to "Intelligent Freight Platform • PL & EU",
        "role_driver" to "Driver App",
        "role_dispatch" to "Dispatch Control",
        "role_customer" to "Customer Portal",
        "role_arch" to "Architecture Spec",
        "language" to "Bilingual (EN)",
        "reset_db" to "Reset Data",
        "driver_compliance" to "Driver Compliance & Rest Checks",
        "driving_hours" to "Driving Hours Today",
        "rest_hours" to "Rest Breaks Today",
        "pre_trip" to "Pre-Trip Inspection Safety",
        "inspected" to "Checked & Inspected",
        "not_inspected" to "Safety check missing",
        "perform_inspection" to "Execute Safety Pre-Trip Inspection",
        "fuel_logging" to "Active Fuel Log",
        "enter_liters" to "Liters (e.g. 150)",
        "log_fuel" to "Log Refueling Event",
        "assigned_jobs" to "Assigned Freight Jobs",
        "job_details" to "Cargo Shipping Details",
        "origin" to "Pickup Depot",
        "destination" to "Delivery Destination",
        "weight" to "Cargo Weight",
        "dimensions" to "Dimensions (LTL/FTL)",
        "hazard_class" to "EU ADR Hazard Class",
        "special_instr" to "Handling Protocols",
        "customer" to "Consignor Client",
        "contact" to "Ecosystem Phone",
        "status" to "Delivery Step",
        "price" to "Financials & Tolls",
        "pln" to "PLN",
        "ask_copilot" to "Consult Gemini AI Routing Copilot",
        "copilot_advice" to "Gemini AI Logistics Analysis",
        "copilot_loading" to "Querying secure Gemini-3.5-flash nodes...",
        "accept_job" to "Accept Assigned Route",
        "arrive_pickup" to "Mark Arrived at Pickup Depot",
        "confirm_load" to "Confirm Cargo Loaded (In Transit)",
        "transit_sim" to "Launch GPS Route Transit Simulator",
        "deliver_pod" to "Verify Delivery & Capture POD",
        "pod_rec" to "Proof of Delivery (POD)",
        "draw_sig" to "Draw consignee signature on screen:",
        "clear_sig" to "Clear Drawing",
        "qr_code" to "Scan barcode/QR cargo check:",
        "upload_pod" to "Verify Cargo & Upload Signed POD",
        "incident_report" to "Report Live Delay / Incident",
        "select_incident" to "Choose incident reason",
        "report_now" to "Broadcast emergency delay alert",
        "transit_sim_active" to "Telemetry GPS Stream Active",
        "fleet_registry" to "Ecosystem Fleet Registry",
        "manual_dispatch" to "Unassigned Client Bookings",
        "click_assign" to "Dispatch Cargo Job to vehicle",
        "booked" to "Booked",
        "tracking_title" to "B2C Live Shipment Tracker",
        "search" to "Track Code",
        "booking_title" to "Book EU & Polish Freight Cargo",
        "cargo_name" to "Cargo name / description",
        "is_express" to "Express Speed (+40% surcharge)",
        "instant_estimate" to "Instant Pricing Breakdown (PLN)",
        "book_btn" to "Book and Sync to Fleet",
        "base_price" to "Base Freight Cost",
        "fuel_surcharge" to "Fuel Price Indexing",
        "tolls" to "EU Highway Toll Fees",
        "total" to "Total Gross Price",
        "architecture_diagram" to "Platform Microservices Topology",
        "postgres_schema" to "PostgreSQL Enterprise Relational Tables",
        "api_endpoints" to "REST API Integration Spec",
        "gdpr_rules" to "EU GDPR Location Protection Laws",
        "deployment_topology" to "Cloud Deployment & High Availability"
    )

    private val PL = mapOf(
        "app_title" to "Polski Freight OS",
        "app_subtitle" to "Inteligentna Platforma Logistyczna • PL i UE",
        "role_driver" to "Kierowca",
        "role_dispatch" to "Dyspozytor",
        "role_customer" to "Klient B2C",
        "role_arch" to "Spec. Architektury",
        "language" to "Dwujęzyczny (PL)",
        "reset_db" to "Reset Bazy",
        "driver_compliance" to "Zgodność kierowcy z czasem pracy",
        "driving_hours" to "Czas jazdy dzisiaj",
        "rest_hours" to "Odpoczynek dzisiaj",
        "pre_trip" to "Kontrola bezpieczeństwa pojazdu",
        "inspected" to "Pojazd skontrolowany dzisiaj",
        "not_inspected" to "Brak raportu bezpieczeństwa",
        "perform_inspection" to "Wykonaj inspekcję przed trasą",
        "fuel_logging" to "Rejestr tankowania pojazdu",
        "enter_liters" to "Pojemność w litrach (np. 150)",
        "log_fuel" to "Zapisz tankowanie w dzienniku",
        "assigned_jobs" to "Przypisane ładunki transportowe",
        "job_details" to "Szczegóły wysyłki towaru",
        "origin" to "Magazyn załadunkowy",
        "destination" to "Miejsce rozładunku",
        "weight" to "Waga ładunku towaru",
        "dimensions" to "Wymiary (LTL/FTL)",
        "hazard_class" to "Klasa niebezpieczeństwa ADR UE",
        "special_instr" to "Protokoły i zalecenia",
        "customer" to "Klient zlecający",
        "contact" to "Telefon alarmowy",
        "status" to "Krok dostawy",
        "price" to "Koszt frachtu i opłat",
        "pln" to "PLN",
        "ask_copilot" to "Skonsultuj z Gemini AI Copilot",
        "copilot_advice" to "Analiza logistyczna Gemini AI",
        "copilot_loading" to "Odpytywanie zabezpieczonych serwerów Gemini-3.5-flash...",
        "accept_job" to "Zaakceptuj zleconą trasę",
        "arrive_pickup" to "Zgłoś przybycie na załadunek",
        "confirm_load" to "Potwierdź załadunek (In Transit)",
        "transit_sim" to "Uruchom symulator GPS trasy",
        "deliver_pod" to "Dostarcz towar i podpisz POD",
        "pod_rec" to "Potwierdzenie Dostawy (POD)",
        "draw_sig" to "Złóż podpis odbiorcy na ekranie:",
        "clear_sig" to "Wyczyść rysunek",
        "qr_code" to "Skanuj kod QR ładunku:",
        "upload_pod" to "Zweryfikuj kod QR i prześlij POD",
        "incident_report" to "Zgłoś opóźnienie drogowe / incydent",
        "select_incident" to "Wybierz powód opóźnienia",
        "report_now" to "Rozgłoś alert bezpieczeństwa o opóźnieniu",
        "transit_sim_active" to "Aktywny strumień telemetryczny GPS",
        "fleet_registry" to "Ewidencja floty transportowej",
        "manual_dispatch" to "Nieprzypisane zlecenia klientów",
        "click_assign" to "Przypisz to zlecenie do pojazdu",
        "booked" to "Zarezerwowano",
        "tracking_title" to "Śledzenie przesyłki B2C live",
        "search" to "Szukaj kodu",
        "booking_title" to "Zarezerwuj transport krajowy/UE",
        "cargo_name" to "Nazwa i opis towaru",
        "is_express" to "Dostawa ekspresowa (+40% dopłaty)",
        "instant_estimate" to "Błyskawiczna wycena kosztów (PLN)",
        "book_btn" to "Zarezerwuj transport i zsynchronizuj",
        "base_price" to "Fracht podstawowy",
        "fuel_surcharge" to "Korekta paliwowa",
        "tolls" to "Opłaty drogowe UE (e-TOLL)",
        "total" to "Łączna cena brutto",
        "architecture_diagram" to "Topologia mikroserwisowa platformy",
        "postgres_schema" to "Tabele relacyjne PostgreSQL",
        "api_endpoints" to "Struktury integracyjne REST API",
        "gdpr_rules" to "Ochrona geolokalizacji i zgodność RODO",
        "deployment_topology" to "Wdrożenie chmurowe i wysoka dostępność"
    )
}

@Composable
fun MainAppScreen(viewModel: FreightViewModel) {
    val isPl by viewModel.isPolish.collectAsState()
    val activeRole by viewModel.activeRole.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1E293B),
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = activeRole == UserRole.DRIVER,
                    onClick = { viewModel.setRole(UserRole.DRIVER) },
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = null) },
                    label = { Text(Loc.get("role_driver", isPl), fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF38BDF8),
                        selectedTextColor = Color(0xFF38BDF8),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF0F172A)
                    ),
                    modifier = Modifier.testTag("nav_driver")
                )
                NavigationBarItem(
                    selected = activeRole == UserRole.DISPATCHER,
                    onClick = { viewModel.setRole(UserRole.DISPATCHER) },
                    icon = { Icon(Icons.Default.Business, contentDescription = null) },
                    label = { Text(Loc.get("role_dispatch", isPl), fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF38BDF8),
                        selectedTextColor = Color(0xFF38BDF8),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF0F172A)
                    ),
                    modifier = Modifier.testTag("nav_dispatch")
                )
                NavigationBarItem(
                    selected = activeRole == UserRole.CUSTOMER,
                    onClick = { viewModel.setRole(UserRole.CUSTOMER) },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text(Loc.get("role_customer", isPl), fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF38BDF8),
                        selectedTextColor = Color(0xFF38BDF8),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF0F172A)
                    ),
                    modifier = Modifier.testTag("nav_customer")
                )
                NavigationBarItem(
                    selected = activeRole == UserRole.ARCHITECTURE,
                    onClick = { viewModel.setRole(UserRole.ARCHITECTURE) },
                    icon = { Icon(Icons.Default.Engineering, contentDescription = null) },
                    label = { Text(Loc.get("role_arch", isPl), fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF38BDF8),
                        selectedTextColor = Color(0xFF38BDF8),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF0F172A)
                    ),
                    modifier = Modifier.testTag("nav_arch")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .padding(innerPadding)
        ) {
            // --- HEADER TOOLBAR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E293B))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FireTruck,
                        contentDescription = "Logo",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = Loc.get("app_title", isPl),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = Loc.get("app_subtitle", isPl),
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.resetData() },
                        modifier = Modifier.testTag("reset_data_button")
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = Loc.get("reset_db", isPl),
                            tint = Color(0xFF94A3B8)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF334155))
                            .clickable { viewModel.toggleLanguage() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Translate,
                                contentDescription = "Lang",
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isPl) "🇵🇱 PL" else "🇬🇧 EN",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // --- MAIN VIEW CHANGER ---
            Box(modifier = Modifier.fillMaxSize()) {
                when (activeRole) {
                    UserRole.DRIVER -> DriverView(viewModel, isPl)
                    UserRole.DISPATCHER -> DispatcherView(viewModel, isPl)
                    UserRole.CUSTOMER -> CustomerView(viewModel, isPl)
                    UserRole.ARCHITECTURE -> ArchitectureView(isPl)
                }
            }
        }
    }
}

// --- 1. DRIVER VIEW ---
@Composable
fun DriverView(viewModel: FreightViewModel, isPl: Boolean) {
    val jobs by viewModel.jobs.collectAsState()
    val compliance by viewModel.compliance.collectAsState()
    val selectedId by viewModel.selectedJobId.collectAsState()
    val aiAdvice by viewModel.aiAdvice.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val transitProgress by viewModel.transitProgress.collectAsState()

    val activeJob = jobs.find { it.id == selectedId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("driver_view_root"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Driver Compliance Indicators
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = Loc.get("driver_compliance", isPl),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    compliance?.let { comp ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(Loc.get("driving_hours", isPl), color = Color(0xFF94A3B8), fontSize = 11.sp)
                                Text("${comp.drivingHoursToday}h / 9.0h", color = if (comp.drivingHoursToday > 8.0) Color(0xFFEF4444) else Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Column {
                                Text(Loc.get("rest_hours", isPl), color = Color(0xFF94A3B8), fontSize = 11.sp)
                                Text("${comp.restHoursToday}h / 11.0h", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (comp.isVehicleInspected) Icons.Default.CheckCircle else Icons.Default.ReportProblem,
                                contentDescription = null,
                                tint = if (comp.isVehicleInspected) Color(0xFF10B981) else Color(0xFFEF4444)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (comp.isVehicleInspected) Loc.get("inspected", isPl) else Loc.get("not_inspected", isPl),
                                color = if (comp.isVehicleInspected) Color(0xFF10B981) else Color(0xFFEF4444),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (!comp.isVehicleInspected) {
                                OutlinedButton(
                                    onClick = { viewModel.performVehicleInspection() },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8)),
                                    border = BorderStroke(1.dp, Color(0xFF38BDF8)),
                                    modifier = Modifier.testTag("inspect_button")
                                ) {
                                    Text(Loc.get("perform_inspection", isPl), fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF334155), modifier = Modifier.padding(vertical = 12.dp))

                    // Fuel Logging Widget
                    var fuelText by remember { mutableStateOf("") }
                    Text(Loc.get("fuel_logging", isPl), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = fuelText,
                            onValueChange = { fuelText = it },
                            placeholder = { Text(Loc.get("enter_liters", isPl), fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF0F172A),
                                unfocusedContainerColor = Color(0xFF0F172A),
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = Color(0xFF334155)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("fuel_input"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val l = fuelText.toIntOrNull()
                                if (l != null) {
                                    viewModel.logFuel(l, l * 6.5)
                                    fuelText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8)),
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("fuel_submit_btn")
                        ) {
                            Text(Loc.get("log_fuel", isPl), fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Assigned Jobs Horizontal List
        item {
            Text(
                text = Loc.get("assigned_jobs", isPl),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        items(jobs.filter { it.status != "DELIVERED" }) { job ->
            val isSelected = job.id == selectedId
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectJob(job.id) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF111E2E)
                ),
                border = BorderStroke(1.dp, if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(job.id, color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            if (job.isHazard) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFF59E0B))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("ADR", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(if (isPl) job.cargoTypePl else job.cargoTypeEn, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("${job.origin} ➔ ${job.destination}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when (job.status) {
                                    "SCHEDULED" -> Color(0xFF1E293B)
                                    "ACCEPTED" -> Color(0xFF334155)
                                    else -> Color(0xFF10B981)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(job.status, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active Job Detail & Execution Cab
        activeJob?.let { job ->
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = Loc.get("job_details", isPl) + ": ${job.id}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        DetailRow(Loc.get("origin", isPl), job.origin)
                        DetailRow(Loc.get("destination", isPl), job.destination)
                        DetailRow(Loc.get("weight", isPl), "${job.weightKg} kg")
                        DetailRow(Loc.get("dimensions", isPl), job.dimensions)
                        DetailRow(Loc.get("hazard_class", isPl), job.hazardClass)
                        DetailRow(Loc.get("special_instr", isPl), if (isPl) job.specialInstructionsPl else job.specialInstructionsEn)
                        DetailRow(Loc.get("customer", isPl), job.customerName)
                        DetailRow(Loc.get("contact", isPl), job.customerContact)

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(Loc.get("price", isPl), color = Color(0xFF94A3B8), fontSize = 12.sp)
                            Text("${job.pricePln} PLN (Tolls: ${job.estimatedTollPln} PLN)", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        // Delay alerts if any
                        job.delayNotePl?.let { note ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEF4444).copy(alpha = 0.2f))
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ReportProblem, contentDescription = null, tint = Color(0xFFEF4444))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isPl) note else job.delayNoteEn ?: "",
                                        color = Color(0xFFEF4444),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Transit simulation progress visualizer
                        if (job.status == "IN_TRANSIT" && transitProgress > 0.0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(Loc.get("transit_sim_active", isPl), color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("${(transitProgress * 100).toInt()}%", color = Color(0xFF38BDF8), fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Color(0xFF334155))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(transitProgress.toFloat())
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(Color(0xFF38BDF8), Color(0xFF10B981))
                                                )
                                            )
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = Color(0xFF334155), modifier = Modifier.padding(vertical = 12.dp))

                        // ASK COPILOT ACTION
                        Button(
                            onClick = { viewModel.queryAiCopilot(job) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copilot_ask_btn")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SupportAgent, contentDescription = null, tint = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(Loc.get("ask_copilot", isPl), color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Copilot output
                        AnimatedVisibility(visible = isAiLoading || aiAdvice != null) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF0F172A))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = Loc.get("copilot_advice", isPl),
                                    color = Color(0xFFF59E0B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                if (isAiLoading) {
                                    Text(Loc.get("copilot_loading", isPl), color = Color(0xFF94A3B8), fontSize = 11.sp)
                                } else {
                                    Text(aiAdvice ?: "", color = Color.White, fontSize = 11.sp, lineHeight = 16.sp)
                                }
                            }
                        }

                        HorizontalDivider(color = Color(0xFF334155), modifier = Modifier.padding(vertical = 12.dp))

                        // PROGRESS BUTTON
                        if (job.currentStep < 4) {
                            Button(
                                onClick = { viewModel.advanceDriverStep(job) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("driver_advance_btn")
                            ) {
                                val btnText = when (job.currentStep) {
                                    0 -> Loc.get("accept_job", isPl)
                                    1 -> Loc.get("arrive_pickup", isPl)
                                    2 -> Loc.get("confirm_load", isPl)
                                    3 -> Loc.get("transit_sim", isPl)
                                    else -> ""
                                }
                                Text(btnText, fontWeight = FontWeight.Bold)
                            }
                        }

                        // POD capture sub-window (visible during final delivery step)
                        if (job.currentStep == 3) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF111E2E))
                                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = Loc.get("pod_rec", isPl),
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(Loc.get("draw_sig", isPl), color = Color.White, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(4.dp))

                                // Interactive Signature Pad
                                val points = remember { mutableStateListOf<Offset>() }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF0F172A))
                                        .border(1.dp, Color(0xFF334155))
                                ) {
                                    Canvas(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .pointerInput(Unit) {
                                                detectDragGestures { change, _ ->
                                                    points.add(change.position)
                                                }
                                            }
                                    ) {
                                        if (points.size > 1) {
                                            for (i in 0 until points.size - 1) {
                                                drawLine(
                                                    color = Color(0xFF38BDF8),
                                                    start = points[i],
                                                    end = points[i + 1],
                                                    strokeWidth = 4f,
                                                    cap = StrokeCap.Round
                                                )
                                            }
                                        }
                                    }
                                    if (points.isNotEmpty()) {
                                        Text(
                                            text = Loc.get("clear_sig", isPl),
                                            color = Color(0xFFEF4444),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .clickable { points.clear() }
                                                .padding(6.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.QrCode, contentDescription = null, tint = Color(0xFF94A3B8))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(Loc.get("qr_code", isPl) + " QR-${job.id}-CONFIRMED", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.uploadPod(job, "Signature Captured - EU Gatekeeper", "QR-${job.id}-CONFIRMED")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("upload_pod_btn")
                                ) {
                                    Text(Loc.get("upload_pod", isPl), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Incident delayed reporting
                        if (job.status != "DELIVERED") {
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFF334155))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(Loc.get("incident_report", isPl), color = Color(0xFFEF4444), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.reportIncident("Border Toll Delay (A2)", "Opóźnienie na bramce A2") },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                                    border = BorderStroke(1.dp, Color(0xFFEF4444)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Border/Bramki", fontSize = 10.sp)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.reportIncident("Traffic Accident Congestion", "Wypadek drogowy zator") },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                                    border = BorderStroke(1.dp, Color(0xFFEF4444)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Accident/Wypadek", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 2. DISPATCHER CONTROL VIEW ---
@Composable
fun DispatcherView(viewModel: FreightViewModel, isPl: Boolean) {
    val vehicles by viewModel.vehicles.collectAsState()
    val jobs by viewModel.jobs.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dispatch_view_root"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Fleet Registry
        item {
            Text(
                text = Loc.get("fleet_registry", isPl),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        items(vehicles) { vehicle ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            vehicle.statusEn.contains("In Transit", true) || vehicle.statusEn.contains("On Job", true) -> Color(0xFF38BDF8)
                                            vehicle.statusEn.contains("Available", true) -> Color(0xFF10B981)
                                            else -> Color(0xFFEF4444)
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(vehicle.registration, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Text(
                            text = if (isPl) vehicle.statusPl else vehicle.statusEn,
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(vehicle.model, color = Color(0xFF94A3B8), fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Fuel: ${vehicle.fuelLevel}%", color = Color.White, fontSize = 11.sp)
                        Text("Usage: ${vehicle.averageConsumption} L/100km", color = Color.White, fontSize = 11.sp)
                        Text("Milage: ${vehicle.currentKm} km", color = Color.White, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Next service: ${vehicle.nextService}", color = Color(0xFF94A3B8), fontSize = 10.sp)
                        Text("Insurance: ${vehicle.insuranceExpiry}", color = Color(0xFF94A3B8), fontSize = 10.sp)
                    }
                }
            }
        }

        // Manual Dispatch System
        item {
            Text(
                text = Loc.get("manual_dispatch", isPl),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        val unassignedJobs = jobs.filter { it.status == "SCHEDULED" }
        if (unassignedJobs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF111E2E))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No pending unassigned bookings / Brak oczekujących zleceń", color = Color(0xFF94A3B8), fontSize = 12.sp)
                }
            }
        }

        items(unassignedJobs) { job ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF111E2E)),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(job.id, color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("${job.weightKg} kg", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(if (isPl) job.cargoTypePl else job.cargoTypeEn, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("${job.origin} ➔ ${job.destination}", color = Color(0xFF94A3B8), fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(Loc.get("click_assign", isPl), color = Color(0xFF94A3B8), fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        vehicles.filter { it.statusEn.contains("Available", true) }.forEach { avVehicle ->
                            Button(
                                onClick = { viewModel.dispatcherAssignJob(job.id, avVehicle.registration) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8)),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("assign_btn_${avVehicle.registration}")
                            ) {
                                Text(avVehicle.registration, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 3. CUSTOMER PORTAL VIEW ---
@Composable
fun CustomerView(viewModel: FreightViewModel, isPl: Boolean) {
    val origin by viewModel.bookingOrigin.collectAsState()
    val dest by viewModel.bookingDestination.collectAsState()
    val weight by viewModel.bookingWeight.collectAsState()
    val cargo by viewModel.bookingCargo.collectAsState()
    val isExpress by viewModel.bookingIsExpress.collectAsState()
    val jobs by viewModel.jobs.collectAsState()

    var trackInputId by remember { mutableStateOf("PF-9082") }
    val trackedJob = jobs.find { it.id == trackInputId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("customer_view_root"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Live Tracker section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = Loc.get("tracking_title", isPl),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = trackInputId,
                            onValueChange = { trackInputId = it },
                            placeholder = { Text("ID: PF-9082", fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF0F172A),
                                unfocusedContainerColor = Color(0xFF0F172A),
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = Color(0xFF334155)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("tracker_id_input"),
                            singleLine = true
                        )
                    }

                    trackedJob?.let { job ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${Loc.get("job_details", isPl)}: ${job.id}",
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(if (isPl) job.cargoTypePl else job.cargoTypeEn, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${job.origin} ➔ ${job.destination}", color = Color(0xFF94A3B8), fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Tracking Milestones Progress
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            MilestoneRow("SCHEDULED", isPl, job.currentStep >= 0, job.currentStep == 0)
                            MilestoneRow("ACCEPTED", isPl, job.currentStep >= 1, job.currentStep == 1)
                            MilestoneRow("ARRIVED_PICKUP", isPl, job.currentStep >= 2, job.currentStep == 2)
                            MilestoneRow("IN_TRANSIT", isPl, job.currentStep >= 3, job.currentStep == 3)
                            MilestoneRow("DELIVERED", isPl, job.currentStep >= 4, job.currentStep == 4)
                        }

                        // Display POD sign info if delivered
                        if (job.status == "DELIVERED") {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF10B981).copy(alpha = 0.1f))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text("✓ Delivered successfully", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("POD Sign: ${job.signatureData}", color = Color.White, fontSize = 10.sp)
                                    Text("Geo log: ${job.deliveryGeo}", color = Color.White, fontSize = 10.sp)
                                    Text("QR Confirm: ${job.qrCodePayload}", color = Color.White, fontSize = 10.sp)
                                }
                            }
                        }
                    } ?: run {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No active tracker code found / Brak aktywnej przesyłki", color = Color(0xFFEF4444), fontSize = 11.sp)
                    }
                }
            }
        }

        // 2. Booking Engine Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = Loc.get("booking_title", isPl),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = cargo,
                        onValueChange = { viewModel.setBookingCargo(it) },
                        label = { Text(Loc.get("cargo_name", isPl)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF334155)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_cargo_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = origin,
                        onValueChange = { viewModel.setBookingOrigin(it) },
                        label = { Text(Loc.get("origin", isPl)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF334155)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_origin_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = dest,
                        onValueChange = { viewModel.setBookingDestination(it) },
                        label = { Text(Loc.get("destination", isPl)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF334155)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_dest_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("${Loc.get("weight", isPl)}: ${weight.toInt()} kg", color = Color.White, fontSize = 12.sp)
                    Slider(
                        value = weight.toFloat(),
                        onValueChange = { viewModel.setBookingWeight(it.toDouble()) },
                        valueRange = 500f..24000f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF38BDF8),
                            activeTrackColor = Color(0xFF38BDF8),
                            inactiveTrackColor = Color(0xFF334155)
                        ),
                        modifier = Modifier.testTag("booking_weight_slider")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Loc.get("is_express", isPl), color = Color.White, fontSize = 12.sp)
                        Switch(
                            checked = isExpress,
                            onCheckedChange = { viewModel.setBookingIsExpress(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF38BDF8),
                                checkedTrackColor = Color(0xFF1E293B)
                            ),
                            modifier = Modifier.testTag("booking_express_switch")
                        )
                    }

                    HorizontalDivider(color = Color(0xFF334155), modifier = Modifier.padding(vertical = 12.dp))

                    // Instant pricing breakdown
                    val breakdown = viewModel.calculateEstimatedPrice(origin, dest, weight, isExpress)
                    Text(
                        text = Loc.get("instant_estimate", isPl),
                        color = Color(0xFFF59E0B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    DetailRow(Loc.get("base_price", isPl), "${breakdown.basePricePln.toInt()} PLN")
                    DetailRow(Loc.get("fuel_surcharge", isPl), "${breakdown.fuelSurchargePln.toInt()} PLN")
                    DetailRow(Loc.get("tolls", isPl), "${breakdown.tollPln.toInt()} PLN")

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(Loc.get("total", isPl), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${breakdown.totalPln.toInt()} PLN", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.bookShipment(cargo, origin, dest, weight, isExpress)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("booking_submit_btn")
                    ) {
                        Text(Loc.get("book_btn", isPl), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MilestoneRow(statusKey: String, isPl: Boolean, isPassed: Boolean, isActive: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isActive -> Color(0xFFF59E0B)
                        isPassed -> Color(0xFF10B981)
                        else -> Color(0xFF334155)
                    }
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = when (statusKey) {
                "SCHEDULED" -> if (isPl) "Zarezerwowano transport (Scheduled)" else "Shipment Scheduled"
                "ACCEPTED" -> if (isPl) "Przyjęto przez kierowcę (Accepted)" else "Accepted by Driver"
                "ARRIVED_PICKUP" -> if (isPl) "Pojazd na załadunku (Arrived Pickup)" else "Arrived at Pickup"
                "IN_TRANSIT" -> if (isPl) "W drodze do celu (In Transit)" else "In Transit across EU"
                "DELIVERED" -> if (isPl) "Dostarczono towar (Delivered & Signed)" else "Delivered & POD Uploaded"
                else -> statusKey
            },
            color = if (isActive) Color(0xFFF59E0B) else if (isPassed) Color.White else Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// --- 4. SYSTEM ARCHITECTURE & TECHNICAL DOCUMENTATION VIEW ---
@Composable
fun ArchitectureView(isPl: Boolean) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("architecture_view_root"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Platform Topology Section
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Engineering, contentDescription = null, tint = Color(0xFF38BDF8))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Loc.get("architecture_diagram", isPl),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = """
                    [Client Devices: Driver App (Kotlin) / Customer Web (TypeScript)]
                                       │ (WebSockets / HTTPS via TLS 1.3)
                                       ▼
                    [Enterprise API Gateway (Kong / Spring Cloud Gateway)]
                                       │ (OAuth2 / JWT Authorization)
                       ┌───────────────┼───────────────┬───────────────┐
                       ▼               ▼               ▼               ▼
                 [User Service] [Driver Service] [Fleet Service] [Tracking Service]
                       │               │               │               │
                       └───────────────┼───────────────┴───────────────┘
                                       ▼
                    [Message Broker: Apache Kafka Event Bus]
                       │ (Topics: job-lifecycle, driver-telemetry, compliance)
                       ▼
                    [Core PostgreSQL Database] ──► [Redis Cache (Geo-Location Logs)]
                    """.trimIndent(),
                    color = Color(0xFF38BDF8),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }

        // Database SQL Schema Section
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Assignment, contentDescription = null, tint = Color(0xFF38BDF8))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Loc.get("postgres_schema", isPl),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = """
                    CREATE TABLE jobs (
                        id VARCHAR(20) PRIMARY KEY,
                        cargo_type_en VARCHAR(255) NOT NULL,
                        cargo_type_pl VARCHAR(255) NOT NULL,
                        origin VARCHAR(255) NOT NULL,
                        destination VARCHAR(255) NOT NULL,
                        weight_kg DECIMAL(10,2) NOT NULL,
                        price_pln DECIMAL(10,2) NOT NULL,
                        status VARCHAR(50) DEFAULT 'SCHEDULED',
                        is_hazard BOOLEAN DEFAULT FALSE,
                        signature_data TEXT,
                        pod_photo_uri VARCHAR(512),
                        delivery_timestamp BIGINT
                    );
                    
                    CREATE TABLE vehicles (
                        registration VARCHAR(15) PRIMARY KEY,
                        model VARCHAR(100) NOT NULL,
                        fuel_level INT DEFAULT 100,
                        next_service DATE NOT NULL,
                        insurance_expiry DATE NOT NULL
                    );
                    
                    CREATE TABLE compliance_logs (
                        driver_id VARCHAR(50) PRIMARY KEY,
                        driving_hours_today DECIMAL(4,2),
                        rest_hours_today DECIMAL(4,2),
                        last_inspection TIMESTAMP
                    );
                    """.trimIndent(),
                    color = Color(0xFF94A3B8),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }

        // REST API Reference
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Route, contentDescription = null, tint = Color(0xFF38BDF8))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Loc.get("api_endpoints", isPl),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = """
                    // 1. POST /api/v1/shipments/book
                    Request Payload:
                    {
                      "cargoDescription": "Automotive Lithium Batteries",
                      "weightKg": 18500.0,
                      "origin": "Warszawa, PL",
                      "destination": "Berlin, DE",
                      "isExpress": true
                    }
                    
                    Response Payload (201 Created):
                    {
                      "jobId": "PF-9082",
                      "estimatedPricePln": 6250.00,
                      "estimatedTollPln": 850.00,
                      "status": "SCHEDULED"
                    }
                    
                    // 2. GET /api/v1/tracking/PF-9082
                    Response Payload (200 OK):
                    {
                      "jobId": "PF-9082",
                      "status": "IN_TRANSIT",
                      "currentLatLong": [52.2297, 21.0122],
                      "etaMs": 1794285000000,
                      "driverCompliant": true
                    }
                    """.trimIndent(),
                    color = Color(0xFF38BDF8),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    lineHeight = 13.sp
                )
            }
        }

        // GDPR Protocols
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF10B981))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Loc.get("gdpr_rules", isPl),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (isPl) {
                        "1. Minimalizacja Danych: Gromadzimy dane geolokalizacyjne GPS wyłącznie wtedy, gdy kierowca jest aktywny na przypisanej trasie.\n2. Przechowywanie Danych: Historia lokalizacji GPS jest trwale usuwana z bazy Redis po upływie 14 dni od momentu dostarczenia (POD).\n3. Szyfrowanie: Wszystkie logi POD, w tym obrazy i podpisy odbiorców, są w pełni szyfrowane algorytmem AES-256 w spoczynku."
                    } else {
                        "1. Data Minimization: We track live GPS geolocations only when the driver is actively clocked-in on an active cargo route.\n2. Data Retention Period: Historic tracking routes are purged from hot Redis caches within 14 days of delivery confirmation.\n3. Encryption at Rest: Driver signatures, client contact details, and POD attachments are encrypted using AES-256."
                    },
                    color = Color.White,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        // Deployment Topology
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Map, contentDescription = null, tint = Color(0xFF10B981))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Loc.get("deployment_topology", isPl),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (isPl) {
                        "• Region Wdrożenia: AWS eu-central-1 (Frankfurt) z zapasowym regionem eu-north-1 (Sztokholm).\n• Elastyczność: Kubernetes (EKS) z Auto-Scalerami dla tysięcy równoległych strumieni GPS kierowców.\n• Baza: Multi-AZ PostgreSQL z replikami odczytu i automatycznym tworzeniem kopii zapasowych."
                    } else {
                        "• Deployment Region: AWS eu-central-1 (Frankfurt) with secondary failover cluster in Warsaw local zones.\n• Scalability: Managed Kubernetes (EKS) configured with cluster autoscalers for high concurrent driver streams.\n• Database: Multi-AZ PostgreSQL architecture with local read replicas and hot standby databases."
                    },
                    color = Color.White,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// --- SHARED UI COMPONENTS ---

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF94A3B8), fontSize = 12.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, textAlign = TextAlign.End, modifier = Modifier.widthIn(max = 200.dp))
    }
}
