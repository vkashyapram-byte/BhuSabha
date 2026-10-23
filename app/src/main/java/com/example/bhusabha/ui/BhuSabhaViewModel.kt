package com.example.bhusabha.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bhusabha.data.BhuSabhaRepository
import com.example.bhusabha.model.CitizenUser
import com.example.bhusabha.model.MutationApplication
import com.example.bhusabha.model.OfficialUser
import com.example.bhusabha.model.Parcel
import com.example.bhusabha.model.UserPersona
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BhuSabhaViewModel(
    private val repository: BhuSabhaRepository = BhuSabhaRepository()
) : ViewModel() {

    val currentPersona: StateFlow<UserPersona> = repository.currentPersona
    val citizenUser: StateFlow<CitizenUser?> = repository.citizenUser
    val officialUser: StateFlow<OfficialUser?> = repository.officialUser
    val isOfflineReady: StateFlow<Boolean> = repository.isOfflineReady
    val mutations: StateFlow<List<MutationApplication>> = repository.mutations

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredParcels: StateFlow<List<Parcel>> =
        combine(repository.parcels, _searchQuery) { parcels, query ->
            if (query.isBlank()) {
                parcels
            } else {
                val q = query.lowercase().trim()
                parcels.filter {
                    it.ulpin.lowercase().contains(q) ||
                    it.surveyNumber.lowercase().contains(q) ||
                    it.ownerName.lowercase().contains(q) ||
                    it.village.lowercase().contains(q)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Citizen Login State
    var citizenAadhaarInput = MutableStateFlow("9823 4109 4892")
    var citizenOtpInput = MutableStateFlow("123456")
    var citizenLoginError = MutableStateFlow<String?>(null)

    // Official Login State
    var officialIdInput = MutableStateFlow("REV-MH-PUN-4012")
    var officialRoleInput = MutableStateFlow("Tahsildar & Executive Magistrate")
    var officialPinInput = MutableStateFlow("officer@123")
    var officialLoginError = MutableStateFlow<String?>(null)

    // Active Login Sheet
    val activeLoginModal = MutableStateFlow<UserPersona?>(null)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun openCitizenLogin() {
        citizenLoginError.value = null
        activeLoginModal.value = UserPersona.CITIZEN
    }

    fun openOfficialLogin() {
        officialLoginError.value = null
        activeLoginModal.value = UserPersona.OFFICIAL
    }

    fun closeLoginModal() {
        activeLoginModal.value = null
    }

    fun submitCitizenLogin(onSuccess: () -> Unit) {
        val aadhaar = citizenAadhaarInput.value.replace(" ", "")
        val otp = citizenOtpInput.value
        if (otp.length != 6) {
            citizenLoginError.value = "Please enter valid 6-digit Aadhaar OTP"
            return
        }
        val success = repository.authenticateCitizen(aadhaar, otp)
        if (success) {
            activeLoginModal.value = null
            onSuccess()
        } else {
            citizenLoginError.value = "Authentication failed. Check credentials."
        }
    }

    fun submitOfficialLogin(onSuccess: () -> Unit) {
        val id = officialIdInput.value.trim()
        val role = officialRoleInput.value
        val pin = officialPinInput.value
        if (id.isEmpty() || pin.isEmpty()) {
            officialLoginError.value = "Employee ID and PIN are required"
            return
        }
        val success = repository.authenticateOfficial(id, role, pin)
        if (success) {
            activeLoginModal.value = null
            onSuccess()
        } else {
            officialLoginError.value = "Authorization failed for this circle."
        }
    }

    fun approveMutation(applicationId: String) {
        repository.approveMutation(applicationId)
    }

    fun raiseObjection(applicationId: String, reason: String) {
        repository.raiseObjection(applicationId, reason)
    }

    fun logout() {
        repository.logout()
        activeLoginModal.value = null
    }
}
