package com.example.bhusabha.model

enum class UserPersona {
    GUEST,
    CITIZEN,
    OFFICIAL
}

data class CitizenUser(
    val citizenId: String = "BHU-MH-89210-C",
    val name: String = "Rameshwar Prasad Sharma",
    val rawAadhaar: String = "982341094892",
    val aadhaarMasked: String = "•••• •••• 4892",
    val mobileNumber: String = "+91 98234 •••••",
    val rawMobile: String = "+91 98234 10982",
    val email: String = "r.sharma.agri@govin.mock",
    val village: String = "Wagholi",
    val taluka: String = "Haveli",
    val district: String = "Pune Rural District",
    val state: String = "Maharashtra",
    val eKycVerified: Boolean = true,
    val biometricLockActive: Boolean = true,
    val offlineSyncEnabled: Boolean = true,
    val cachedDataSize: String = "2.4 MB cached",
    val preferredLanguage: String = "English",
    val passbookPinConfigured: Boolean = true,
    val passbookPin: String = "1234"
) {
    val initials: String
        get() {
            val parts = name.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
            return when {
                parts.size >= 2 -> "${parts.first().first().uppercase()}${parts.last().first().uppercase()}"
                parts.isNotEmpty() && parts.first().isNotEmpty() -> parts.first().take(2).uppercase()
                else -> "RS"
            }
        }
}

data class OfficialUser(
    val employeeId: String,
    val name: String,
    val designation: String,
    val jurisdiction: String,
    val circle: String,
    val tokenActive: Boolean = true
)

data class Parcel(
    val ulpin: String,
    val surveyNumber: String,
    val ownerName: String,
    val village: String,
    val taluka: String,
    val district: String,
    val state: String,
    val areaHectares: Double,
    val landType: String,
    val healthScore: Int,
    val encumbranceText: String,
    val taxAnnual: Double,
    val taxPaid: Boolean,
    val mutationStatus: String,
    val lastUpdated: String
)

data class MutationApplication(
    val id: String,
    val ulpin: String,
    val applicantName: String,
    val currentOwner: String,
    val proposedOwner: String,
    val deedType: String,
    val submissionDate: String,
    val status: MutationStatus,
    val riskScore: Int,
    val discrepancyNote: String? = null
)

enum class MutationStatus(val label: String) {
    PENDING_INSPECTION("Pending Field Inspection"),
    APPROVED("Approved & RoR Updated"),
    DISCREPANCY_FLAGGED("Discrepancy Objection Flagged")
}
