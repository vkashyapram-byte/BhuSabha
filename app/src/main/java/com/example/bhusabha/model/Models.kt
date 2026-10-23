package com.example.bhusabha.model

enum class UserPersona {
    GUEST,
    CITIZEN,
    OFFICIAL
}

data class CitizenUser(
    val aadhaarMasked: String,
    val name: String,
    val mobileNumber: String,
    val village: String,
    val taluka: String,
    val district: String,
    val state: String,
    val eKycVerified: Boolean = true
)

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
