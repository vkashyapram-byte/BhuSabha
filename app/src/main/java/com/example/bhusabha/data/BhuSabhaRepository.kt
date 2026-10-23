package com.example.bhusabha.data

import com.example.bhusabha.model.CitizenUser
import com.example.bhusabha.model.MutationApplication
import com.example.bhusabha.model.MutationStatus
import com.example.bhusabha.model.OfficialUser
import com.example.bhusabha.model.Parcel
import com.example.bhusabha.model.UserPersona
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BhuSabhaRepository {

    private val _currentPersona = MutableStateFlow(UserPersona.GUEST)
    val currentPersona: StateFlow<UserPersona> = _currentPersona.asStateFlow()

    private val _citizenUser = MutableStateFlow<CitizenUser?>(null)
    val citizenUser: StateFlow<CitizenUser?> = _citizenUser.asStateFlow()

    private val _officialUser = MutableStateFlow<OfficialUser?>(null)
    val officialUser: StateFlow<OfficialUser?> = _officialUser.asStateFlow()

    private val _isOfflineReady = MutableStateFlow(true)
    val isOfflineReady: StateFlow<Boolean> = _isOfflineReady.asStateFlow()

    private val _parcels = MutableStateFlow(
        listOf(
            Parcel(
                ulpin = "MH-PUN-2024-009182",
                surveyNumber = "142/2A",
                ownerName = "Rameshwar Prasad Sharma",
                village = "Wagholi",
                taluka = "Haveli",
                district = "Pune",
                state = "Maharashtra",
                areaHectares = 2.45,
                landType = "Agricultural (Irrigated)",
                healthScore = 94,
                encumbranceText = "No active mortgages or stays (Clean Title)",
                taxAnnual = 4250.0,
                taxPaid = true,
                mutationStatus = "Clear",
                lastUpdated = "14-Aug-2026"
            ),
            Parcel(
                ulpin = "MH-PUN-2024-004312",
                surveyNumber = "89/1B",
                ownerName = "Rameshwar Prasad Sharma",
                village = "Lohegaon",
                taluka = "Haveli",
                district = "Pune",
                state = "Maharashtra",
                areaHectares = 0.85,
                landType = "Residential / Gaothan",
                healthScore = 88,
                encumbranceText = "NOC granted by Local Gram Panchayat",
                taxAnnual = 8900.0,
                taxPaid = true,
                mutationStatus = "Clear",
                lastUpdated = "02-Jul-2026"
            ),
            Parcel(
                ulpin = "MH-PUN-2024-007741",
                surveyNumber = "204/5",
                ownerName = "Kailash Nath",
                village = "Kharadi",
                taluka = "Haveli",
                district = "Pune",
                state = "Maharashtra",
                areaHectares = 1.20,
                landType = "Commercial Zone",
                healthScore = 58,
                encumbranceText = "Active Boundary Demarcation Dispute",
                taxAnnual = 14200.0,
                taxPaid = false,
                mutationStatus = "Discrepancy Flagged",
                lastUpdated = "28-Sep-2026"
            ),
            Parcel(
                ulpin = "MH-PUN-2024-003119",
                surveyNumber = "77/3",
                ownerName = "Sunita Patil",
                village = "Hadapsar",
                taluka = "Haveli",
                district = "Pune",
                state = "Maharashtra",
                areaHectares = 3.10,
                landType = "Agricultural",
                healthScore = 91,
                encumbranceText = "Clear Title - Bank Lien Discharged",
                taxAnnual = 3800.0,
                taxPaid = true,
                mutationStatus = "Under Review",
                lastUpdated = "10-Oct-2026"
            )
        )
    )
    val parcels: StateFlow<List<Parcel>> = _parcels.asStateFlow()

    private val _mutations = MutableStateFlow(
        listOf(
            MutationApplication(
                id = "MUT-2026-9041",
                ulpin = "MH-PUN-2024-009182",
                applicantName = "Ananya Sharma",
                currentOwner = "Rameshwar Prasad Sharma",
                proposedOwner = "Ananya Sharma",
                deedType = "Registered Sale Deed #4019",
                submissionDate = "20-Oct-2026",
                status = MutationStatus.PENDING_INSPECTION,
                riskScore = 12,
                discrepancyNote = null
            ),
            MutationApplication(
                id = "MUT-2026-8820",
                ulpin = "MH-PUN-2024-007741",
                applicantName = "Rajesh Nath",
                currentOwner = "Kailash Nath",
                proposedOwner = "Rajesh Nath",
                deedType = "Family Partition Deed",
                submissionDate = "16-Oct-2026",
                status = MutationStatus.DISCREPANCY_FLAGGED,
                riskScore = 78,
                discrepancyNote = "Declared cadastral area (1.35 Ha) exceeds revenue map survey record (1.20 Ha)"
            ),
            MutationApplication(
                id = "MUT-2026-7611",
                ulpin = "MH-PUN-2024-003119",
                applicantName = "Rohan Patil",
                currentOwner = "Sunita Patil",
                proposedOwner = "Rohan Patil",
                deedType = "Gift Deed (Khatedar)",
                submissionDate = "12-Oct-2026",
                status = MutationStatus.PENDING_INSPECTION,
                riskScore = 22,
                discrepancyNote = null
            )
        )
    )
    val mutations: StateFlow<List<MutationApplication>> = _mutations.asStateFlow()

    fun authenticateCitizen(aadhaarOrMobile: String, otp: String): Boolean {
        if (otp.length == 6) {
            val user = CitizenUser(
                aadhaarMasked = if (aadhaarOrMobile.length >= 4) "XXXX-XXXX-" + aadhaarOrMobile.takeLast(4) else "XXXX-XXXX-4892",
                name = "Rameshwar Prasad Sharma",
                mobileNumber = "+91 98234 10982",
                village = "Wagholi",
                taluka = "Haveli",
                district = "Pune",
                state = "Maharashtra"
            )
            _citizenUser.value = user
            _currentPersona.value = UserPersona.CITIZEN
            return true
        }
        return false
    }

    fun authenticateOfficial(employeeId: String, role: String, pin: String): Boolean {
        if (pin.isNotEmpty() && employeeId.isNotEmpty()) {
            val officer = OfficialUser(
                employeeId = employeeId,
                name = "Suresh Deshmukh",
                designation = role,
                jurisdiction = "Haveli Sub-division, Pune East",
                circle = "Circle No. 4"
            )
            _officialUser.value = officer
            _currentPersona.value = UserPersona.OFFICIAL
            return true
        }
        return false
    }

    fun logout() {
        _currentPersona.value = UserPersona.GUEST
        _citizenUser.value = null
        _officialUser.value = null
    }

    fun approveMutation(applicationId: String): Boolean {
        val app = _mutations.value.find { it.id == applicationId } ?: return false
        _mutations.update { list ->
            list.map {
                if (it.id == applicationId) it.copy(status = MutationStatus.APPROVED) else it
            }
        }
        // Dynamically update parcel owner in the parcels registry!
        _parcels.update { list ->
            list.map {
                if (it.ulpin == app.ulpin) {
                    it.copy(
                        ownerName = app.proposedOwner,
                        mutationStatus = "Updated via ${app.id}",
                        lastUpdated = "Today"
                    )
                } else it
            }
        }
        return true
    }

    fun raiseObjection(applicationId: String, reason: String): Boolean {
        _mutations.update { list ->
            list.map {
                if (it.id == applicationId) {
                    it.copy(
                        status = MutationStatus.DISCREPANCY_FLAGGED,
                        discrepancyNote = reason
                    )
                } else it
            }
        }
        return true
    }

    fun searchParcels(query: String): List<Parcel> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return _parcels.value
        return _parcels.value.filter {
            it.ulpin.lowercase().contains(q) ||
            it.surveyNumber.lowercase().contains(q) ||
            it.ownerName.lowercase().contains(q) ||
            it.village.lowercase().contains(q)
        }
    }
}
