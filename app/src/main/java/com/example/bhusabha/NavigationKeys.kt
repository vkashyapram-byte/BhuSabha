package com.example.bhusabha

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeNavKey : NavKey

@Serializable
data object CitizenDashboardNavKey : NavKey

@Serializable
data object OfficialDashboardNavKey : NavKey

@Serializable
data object CitizenProfileNavKey : NavKey
