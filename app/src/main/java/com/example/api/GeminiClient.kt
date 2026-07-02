package com.example.api

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

// --- API Request/Response Models ---

@JsonClass(generateAdapter = true)
data class Part(val text: String? = null)

@JsonClass(generateAdapter = true)
data class Content(val parts: List<Part>)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(val content: Content)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(val candidates: List<Candidate>?)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun getLogisticsCopilotAdvice(
        cargoName: String,
        origin: String,
        destination: String,
        weightKg: Double,
        isHazard: Boolean,
        langPl: Boolean
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext if (langPl) {
                "⚠️ Klucz API Gemini nie jest skonfigurowany w panelu Secrets. Copilot działa w trybie offline / demonstracyjnym.\n\nSugerowana trasa: Trasa główna przez autostradę A2 / S7. Czas pracy kierowcy w normie (zalecana przerwa za 4,5h). Koszty opłat drogowych w normie EU. Brak zagrożeń ADR."
            } else {
                "⚠️ Gemini API Key is not configured in the Secrets panel. Copilot is running in offline/demo mode.\n\nSuggested Routing: Main expressway via A2 / S7. Driver compliance hours: OK (suggested break in 4.5h). EU road toll fees estimated normally. No ADR hazard alerts."
            }
        }

        val prompt = if (langPl) {
            """
            Jesteś inteligentnym asystentem logistycznym (Polski Freight OS Copilot) dla kierowców i dyspozytorów w Polsce i UE.
            Przeanalizuj następujący ładunek i trasę:
            - Ładunek: $cargoName
            - Waga: $weightKg kg
            - Pochodzenie: $origin
            - Cel: $destination
            - Klasa ADR: ${if (isHazard) "Wymagana klasyfikacja (Zagrożenie)" else "Brak"}
            
            Przekaż zwięzłe porady (maksymalnie 4 punkty, po polsku):
            1. Optymalna trasa (np. autostrady w Polsce/UE, opłaty drogowe)
            2. Wytyczne zgodności z czasem pracy kierowców UE (rozporządzenie 561/2006)
            3. Bezpieczeństwo ładunku i ewentualne zalecenia ADR
            4. Inteligentna prognoza opłat drogowych i zalecany postój
            """.trimIndent()
        } else {
            """
            You are an intelligent logistics assistant (Polski Freight OS Copilot) for drivers and dispatchers in Poland and the EU.
            Analyze the following load and route:
            - Cargo: $cargoName
            - Weight: $weightKg kg
            - Origin: $origin
            - Destination: $destination
            - ADR Class: ${if (isHazard) "Required classification (Hazardous)" else "None"}
            
            Provide concise expert advice (maximum 4 points, in English):
            1. Optimal routing (e.g. key highways in PL/EU, tolls)
            2. EU Driver Compliance hours guidelines (Regulation 561/2006)
            3. Cargo safety & potential ADR hazardous material recommendations
            4. Smart road toll prediction & recommended layover point
            """.trimIndent()
        }

        val systemInstructionText = "You are a professional logistics and transport planner. Be direct, factual, and strictly focused on logistics, EU driving regulations, and Polish transport rules. Do not include introductory conversational filler."

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = systemInstructionText)))
        )

        try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "No advice available / Brak dostępnych porad"
        } catch (e: Exception) {
            if (langPl) {
                "Błąd połączenia z asystentem AI: ${e.localizedMessage}. Proszę sprawdzić połączenie internetowe lub klucz API."
            } else {
                "AI Assistant connection error: ${e.localizedMessage}. Please verify internet or API key configurations."
            }
        }
    }
}
