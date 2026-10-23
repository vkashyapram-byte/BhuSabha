package com.example.bhusabha.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhusabha.theme.*

@Composable
fun CitizenProfileScreen(
    viewModel: BhuSabhaViewModel,
    onBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val citizenUser by viewModel.citizenUser.collectAsState()
    val parcels by viewModel.filteredParcels.collectAsState()
    val toastMessage by viewModel.syncToastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var showEditDialog by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }
    var showHoldingsDialog by remember { mutableStateOf(false) }
    var showAadhaarDialog by remember { mutableStateOf(false) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissToast()
        }
    }

    val primaryUlpin = parcels.firstOrNull()?.ulpin ?: "IN-MH-401102-8819"
    val holdingsCount = parcels.size

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            CitizenBottomNavigationBar(
                currentTab = CitizenTab.PROFILE,
                onTabSelected = { tab ->
                    if (tab == CitizenTab.HOME || tab == CitizenTab.PROPERTIES) {
                        onNavigateHome()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GovBg)
                .padding(innerPadding)
        ) {
            // 1. Header (Deep Blue government styling matching screenshot)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GovBlue)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Column {
                            Text(
                                text = "Citizen Profile",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Account & Digital Cadastre Identity",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = { viewModel.syncAccount() }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Sync",
                            tint = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // 2. Scrollable Body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Card 1: Citizen Identity Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = GovSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        // Edit button in top right of card
                        IconButton(
                            onClick = { showEditDialog = true },
                            modifier = Modifier.align(Alignment.TopEnd).size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = GovSlate400,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Avatar with double border ring (saffron outer ring + white inner line + navy circle)
                            Box(
                                modifier = Modifier
                                    .size(84.dp)
                                    .border(width = 2.dp, color = GovSaffron, shape = CircleShape)
                                    .padding(3.dp)
                                    .border(width = 1.5.dp, color = Color.White, shape = CircleShape)
                                    .clip(CircleShape)
                                    .background(GovNavy)
                                    .clickable { showEditDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = citizenUser.initials,
                                    color = Color.White,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Name
                            Text(
                                text = citizenUser.name,
                                color = GovSlate900,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.clickable { showEditDialog = true }
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            // Citizen ID
                            Text(
                                text = "Citizen ID: ${citizenUser.citizenId}",
                                color = GovSlate500,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Aadhaar e-KYC Verified Pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(GovEmeraldLight)
                                    .border(1.dp, GovEmeraldBorder, RoundedCornerShape(50.dp))
                                    .clickable { showAadhaarDialog = true }
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = GovEmeraldDark,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "✓ Aadhaar e-KYC Verified",
                                    color = GovEmeraldDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                // Main Card 2: Key Profile Details List
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = GovSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 1. Mobile Number
                        ProfileDetailRow(
                            icon = Icons.Default.PhoneAndroid,
                            iconBg = GovBlueLight,
                            iconColor = GovBlue,
                            label = "MOBILE NUMBER",
                            mainText = citizenUser.mobileNumber,
                            secondaryText = "(Linked to Aadhaar)",
                            secondaryColor = GovEmeraldDark,
                            onClick = { showEditDialog = true }
                        )
                        HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                        // 2. Email Address
                        ProfileDetailRow(
                            icon = Icons.Default.Mail,
                            iconBg = GovBlueLight,
                            iconColor = GovBlue,
                            label = "EMAIL ADDRESS",
                            mainText = citizenUser.email,
                            isMono = true,
                            onClick = { showEditDialog = true }
                        )
                        HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                        // 3. Linked Aadhaar
                        ProfileDetailRow(
                            icon = Icons.Default.Badge,
                            iconBg = GovBlueLight,
                            iconColor = GovBlue,
                            label = "LINKED AADHAAR",
                            mainText = citizenUser.aadhaarMasked,
                            secondaryText = "(Biometric Lock Active)",
                            secondaryColor = GovBlue,
                            isMono = true,
                            onClick = { showAadhaarDialog = true }
                        )
                        HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                        // 4. Registered Land Holdings
                        ProfileDetailRow(
                            icon = Icons.Default.Landscape,
                            iconBg = GovSaffronLight,
                            iconColor = GovSaffron,
                            label = "REGISTERED LAND HOLDINGS",
                            mainText = "$holdingsCount Parcels",
                            secondaryText = "(${citizenUser.district})",
                            onClick = { showHoldingsDialog = true }
                        )
                        HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                        // 5. Primary Village / Tahsil
                        ProfileDetailRow(
                            icon = Icons.Default.PinDrop,
                            iconBg = GovSlate100,
                            iconColor = GovSlate700,
                            label = "PRIMARY VILLAGE / TAHSIL",
                            mainText = "${citizenUser.taluka} / ${citizenUser.village}, ${citizenUser.state}",
                            onClick = { showEditDialog = true }
                        )
                        HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                        // 6. ULPIN Registered
                        ProfileDetailRow(
                            icon = Icons.Default.Fingerprint,
                            iconBg = GovBlueLight,
                            iconColor = GovBlue,
                            label = "ULPIN REGISTERED",
                            mainText = primaryUlpin,
                            secondaryText = "(+${if (holdingsCount > 1) holdingsCount - 1 else 0} more)",
                            isMono = true,
                            onClick = { showHoldingsDialog = true }
                        )
                    }
                }

                // Main Card 3: PREFERENCES & SECURITY
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = GovSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "PREFERENCES & SECURITY",
                            color = GovSlate500,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        // Offline Data Sync Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = null,
                                    tint = GovEmerald,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        text = "Offline Data Sync",
                                        color = GovSlate900,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Up to Date (${citizenUser.cachedDataSize})",
                                        color = GovEmeraldDark,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Switch(
                                checked = citizenUser.offlineSyncEnabled,
                                onCheckedChange = { viewModel.toggleOfflineSync(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = GovEmerald
                                )
                            )
                        }

                        HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                        // Language Selector (English, Marathi, Hindi)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("English", "मराठी", "हिंदी").forEach { lang ->
                                val isSelected = citizenUser.preferredLanguage == lang
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) GovNavy else GovSlate100)
                                        .clickable { viewModel.updateLanguage(lang) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = lang,
                                        color = if (isSelected) Color.White else GovSlate700,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                        // Digital Passbook Security PIN
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = GovSaffron,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "Digital Passbook Security PIN",
                                        color = GovSlate900,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Configured (••••)",
                                        color = GovSlate500,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            TextButton(onClick = { showPinDialog = true }) {
                                Text(
                                    text = "Change",
                                    color = GovBlue,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Logout Button
                OutlinedButton(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFFFF1F2),
                        contentColor = GovCrimson
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECDD3))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = GovCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Logout from BhuSabha",
                            color = GovCrimson,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Session Token Footer
                Text(
                    text = "Session token active via National Informatics Centre (NIC)",
                    color = GovSlate400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Dialog 1: Comprehensive Profile Details Editor
    if (showEditDialog) {
        var editName by remember { mutableStateOf(citizenUser.name) }
        var editEmail by remember { mutableStateOf(citizenUser.email) }
        var editMobile by remember { mutableStateOf(citizenUser.rawMobile) }
        var editTaluka by remember { mutableStateOf(citizenUser.taluka) }
        var editVillage by remember { mutableStateOf(citizenUser.village) }
        var editDistrict by remember { mutableStateOf(citizenUser.district) }
        var editState by remember { mutableStateOf(citizenUser.state) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "Edit Citizen Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = GovSlate900
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = editMobile,
                        onValueChange = { editMobile = it },
                        label = { Text("Mobile Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editTaluka,
                            onValueChange = { editTaluka = it },
                            label = { Text("Tahsil") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = editVillage,
                            onValueChange = { editVillage = it },
                            label = { Text("Village") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editDistrict,
                            onValueChange = { editDistrict = it },
                            label = { Text("District") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = editState,
                            onValueChange = { editState = it },
                            label = { Text("State") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(
                            name = editName,
                            email = editEmail,
                            mobile = editMobile,
                            village = editVillage,
                            taluka = editTaluka,
                            district = editDistrict,
                            state = editState
                        )
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavy)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = GovSlate500)
                }
            }
        )
    }

    // Dialog 2: Change Security PIN
    if (showPinDialog) {
        var newPin by remember { mutableStateOf("") }
        var pinError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = {
                Text(
                    text = "Change Security PIN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = GovSlate900
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Enter a new 4-digit PIN for your Digital Land Passbook:",
                        fontSize = 13.sp,
                        color = GovSlate600
                    )
                    OutlinedTextField(
                        value = newPin,
                        onValueChange = {
                            if (it.length <= 4 && it.all { c -> c.isDigit() }) {
                                newPin = it
                                pinError = false
                            }
                        },
                        placeholder = { Text("4 Digits (e.g. 1234)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = pinError
                    )
                    if (pinError) {
                        Text(text = "Please enter exactly 4 digits", color = GovCrimson, fontSize = 11.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPin.length == 4) {
                            viewModel.updateSecurityPin(newPin)
                            showPinDialog = false
                        } else {
                            pinError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavy)
                ) {
                    Text("Update PIN")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("Cancel", color = GovSlate500)
                }
            }
        )
    }

    // Dialog 3: Dynamic Land Holdings & Add Parcel
    if (showHoldingsDialog) {
        var showAddParcelFields by remember { mutableStateOf(false) }
        var newSurvey by remember { mutableStateOf("") }
        var newArea by remember { mutableStateOf("1.50") }

        AlertDialog(
            onDismissRequest = { showHoldingsDialog = false },
            title = {
                Text(
                    text = "Registered Land Holdings (${parcels.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = GovSlate900
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "ULPIN Cadastre Registry under ${citizenUser.name}:",
                        fontSize = 12.sp,
                        color = GovSlate600
                    )

                    parcels.forEach { parcel ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(GovSlate100)
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "Survey: ${parcel.surveyNumber} • ${parcel.areaHectares} Ha",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = GovSlate900
                                )
                                Text(
                                    text = parcel.ulpin,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = GovBlue
                                )
                                Text(
                                    text = "${parcel.village}, ${parcel.taluka}",
                                    fontSize = 11.sp,
                                    color = GovSlate600
                                )
                            }
                        }
                    }

                    if (showAddParcelFields) {
                        OutlinedTextField(
                            value = newSurvey,
                            onValueChange = { newSurvey = it },
                            label = { Text("Survey Number (e.g. 112/3)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = newArea,
                            onValueChange = { newArea = it },
                            label = { Text("Area in Hectares") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    } else {
                        OutlinedButton(
                            onClick = { showAddParcelFields = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Register New Land Parcel")
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (showAddParcelFields) {
                            val areaVal = newArea.toDoubleOrNull() ?: 1.25
                            viewModel.addDemoParcel(
                                surveyNum = newSurvey.ifBlank { "112/3A" },
                                village = citizenUser.village,
                                area = areaVal
                            )
                        }
                        showHoldingsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavy)
                ) {
                    Text(if (showAddParcelFields) "Save Parcel" else "Done")
                }
            },
            dismissButton = {
                TextButton(onClick = { showHoldingsDialog = false }) {
                    Text("Close", color = GovSlate500)
                }
            }
        )
    }

    // Dialog 4: Aadhaar Verification & Biometric Lock Info
    if (showAadhaarDialog) {
        AlertDialog(
            onDismissRequest = { showAadhaarDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GovEmeraldDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Aadhaar e-KYC Identity",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = GovSlate900
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Linked Aadhaar: ${citizenUser.aadhaarMasked}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = GovSlate900
                    )
                    Text(
                        text = "Status: ✓ Biometric Lock Active (UIDAI Security Vault)",
                        fontSize = 13.sp,
                        color = GovBlue,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Your land holdings are securely anchored to your verified Aadhaar credential.",
                        fontSize = 12.sp,
                        color = GovSlate600
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAadhaarDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavy)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ProfileDetailRow(
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    label: String,
    mainText: String,
    secondaryText: String? = null,
    secondaryColor: Color = GovSlate500,
    isMono: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                color = GovSlate400,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = mainText,
                    color = GovSlate900,
                    fontSize = 13.sp,
                    fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default,
                    fontWeight = FontWeight.SemiBold
                )

                if (secondaryText != null) {
                    Text(
                        text = secondaryText,
                        color = secondaryColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

enum class CitizenTab {
    HOME,
    PROPERTIES,
    SERVICES,
    PROFILE
}

@Composable
fun CitizenBottomNavigationBar(
    currentTab: CitizenTab,
    onTabSelected: (CitizenTab) -> Unit
) {
    NavigationBar(
        containerColor = GovSurface,
        tonalElevation = 6.dp,
        modifier = Modifier.border(1.dp, GovCardBorder)
    ) {
        NavigationBarItem(
            selected = currentTab == CitizenTab.HOME,
            onClick = { onTabSelected(CitizenTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GovNavy,
                selectedTextColor = GovNavy,
                indicatorColor = GovBlueLight
            )
        )

        NavigationBarItem(
            selected = currentTab == CitizenTab.PROPERTIES,
            onClick = { onTabSelected(CitizenTab.PROPERTIES) },
            icon = { Icon(Icons.Default.Folder, contentDescription = "Properties") },
            label = { Text("Properties", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GovNavy,
                selectedTextColor = GovNavy,
                indicatorColor = GovBlueLight
            )
        )

        NavigationBarItem(
            selected = currentTab == CitizenTab.SERVICES,
            onClick = { onTabSelected(CitizenTab.SERVICES) },
            icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = "Services") },
            label = { Text("Services", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GovNavy,
                selectedTextColor = GovNavy,
                indicatorColor = GovBlueLight
            )
        )

        NavigationBarItem(
            selected = currentTab == CitizenTab.PROFILE,
            onClick = { onTabSelected(CitizenTab.PROFILE) },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GovSaffron,
                selectedTextColor = GovSaffron,
                indicatorColor = GovSaffronLight
            )
        )
    }
}
