package com.example.bhusabha

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.bhusabha.model.UserPersona
import com.example.bhusabha.ui.BhuSabhaViewModel
import com.example.bhusabha.ui.CitizenDashboardScreen
import com.example.bhusabha.ui.CitizenLoginSheet
import com.example.bhusabha.ui.OfficialDashboardScreen
import com.example.bhusabha.ui.OfficialLoginSheet
import com.example.bhusabha.ui.PersonaWelcomeScreen

import com.example.bhusabha.ui.CitizenProfileScreen
import com.example.bhusabha.ui.NewRegistrationScreen

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    viewModel: BhuSabhaViewModel = viewModel()
) {
    val backStack = rememberNavBackStack(WelcomeNavKey as NavKey)
    val activeLoginModal by viewModel.activeLoginModal.collectAsState()

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.size - 1)
            }
        },
        entryProvider = entryProvider {
            entry<WelcomeNavKey> {
                PersonaWelcomeScreen(
                    onCitizenLoginClick = { viewModel.openCitizenLogin() },
                    onOfficialLoginClick = { viewModel.openOfficialLogin() },
                    modifier = modifier
                )
            }
            entry<CitizenDashboardNavKey> {
                CitizenDashboardScreen(
                    viewModel = viewModel,
                    onNavigateProfile = {
                        backStack.add(CitizenProfileNavKey)
                    },
                    onNavigateNewRegistration = {
                        backStack.add(NewRegistrationNavKey)
                    },
                    onLogout = {
                        backStack.clear()
                        backStack.add(WelcomeNavKey)
                    },
                    modifier = modifier
                )
            }
            entry<CitizenProfileNavKey> {
                CitizenProfileScreen(
                    viewModel = viewModel,
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    },
                    onNavigateHome = {
                        if (backStack.size > 1 && backStack.last() == CitizenProfileNavKey) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    },
                    onNavigateNewRegistration = {
                        backStack.add(NewRegistrationNavKey)
                    },
                    onLogout = {
                        backStack.clear()
                        backStack.add(WelcomeNavKey)
                    },
                    modifier = modifier
                )
            }
            entry<NewRegistrationNavKey> {
                NewRegistrationScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    },
                    onComplete = {
                        if (backStack.size > 1 && backStack.last() == NewRegistrationNavKey) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    },
                    modifier = modifier
                )
            }
            entry<OfficialDashboardNavKey> {
                OfficialDashboardScreen(
                    viewModel = viewModel,
                    onLogout = {
                        backStack.clear()
                        backStack.add(WelcomeNavKey)
                    },
                    modifier = modifier
                )
            }
        }
    )

    // Dynamic Login Sheets
    when (activeLoginModal) {
        UserPersona.CITIZEN -> {
            CitizenLoginSheet(
                viewModel = viewModel,
                onSuccess = {
                    backStack.add(CitizenDashboardNavKey)
                },
                onDismiss = { viewModel.closeLoginModal() }
            )
        }
        UserPersona.OFFICIAL -> {
            OfficialLoginSheet(
                viewModel = viewModel,
                onSuccess = {
                    backStack.add(OfficialDashboardNavKey)
                },
                onDismiss = { viewModel.closeLoginModal() }
            )
        }
        else -> {}
    }
}
