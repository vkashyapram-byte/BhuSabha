package com.example.bhusabha.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhusabha.model.Parcel
import com.example.bhusabha.theme.GovBg
import com.example.bhusabha.theme.GovBlue
import com.example.bhusabha.theme.GovBlueLight
import com.example.bhusabha.theme.GovCardBorder
import com.example.bhusabha.theme.GovEmerald
import com.example.bhusabha.theme.GovEmeraldDark
import com.example.bhusabha.theme.GovEmeraldLight
import com.example.bhusabha.theme.GovNavy
import com.example.bhusabha.theme.GovSaffron
import com.example.bhusabha.theme.GovSlate400
import com.example.bhusabha.theme.GovSlate500
import com.example.bhusabha.theme.GovSlate600
import com.example.bhusabha.theme.GovSlate700
import com.example.bhusabha.theme.GovSlate900
import com.example.bhusabha.theme.GovSurface

import androidx.compose.foundation.clickable
import androidx.compose.material3.Scaffold

@Composable
fun CitizenDashboardScreen(
    viewModel: BhuSabhaViewModel,
    onLogout: () -> Unit,
    onNavigateProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val citizenUser by viewModel.citizenUser.collectAsState()
    val parcels by viewModel.filteredParcels.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            CitizenBottomNavigationBar(
                currentTab = CitizenTab.HOME,
                onTabSelected = { tab ->
                    if (tab == CitizenTab.PROFILE) {
                        onNavigateProfile()
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
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GovSurface)
                    .border(width = 1.dp, color = GovCardBorder)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { onNavigateProfile() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(GovNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = citizenUser.initials,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = citizenUser.name,
                                color = GovSlate900,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "e-KYC Verified",
                                tint = GovBlue,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Text(
                            text = "Aadhaar: ${citizenUser.aadhaarMasked} • ${citizenUser.village}",
                            color = GovSlate500,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = {
                    viewModel.logout()
                    onLogout()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Sign Out",
                        tint = GovSlate500
                    )
                }
            }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                // e-Passbook Digital Card Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        GovNavy,
                                        Color(0xFF1E3A8A),
                                        Color(0xFF172554)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "BHUSABHA e-PASSBOOK",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(GovEmeraldLight.copy(alpha = 0.2f))
                                        .border(1.dp, GovEmerald.copy(alpha = 0.5f), RoundedCornerShape(50.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Active Titles: ${parcels.size}",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "3.30 Hectares",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "Total Registered Land Holding across Maharashtra",
                                    color = Color.White.copy(alpha = 0.75f),
                                    fontSize = 11.sp
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = GovEmerald,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Cadastre & RoR Synchronized",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Text(
                                    text = "Gov. of India",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    placeholder = { Text("Search by ULPIN, Survey No, or Village...", color = GovSlate400, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = GovSlate400,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GovBlue,
                        unfocusedBorderColor = GovCardBorder,
                        focusedContainerColor = GovSurface,
                        unfocusedContainerColor = GovSurface
                    )
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REGISTERED LAND PARCELS (${parcels.size})",
                        color = GovSlate500,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Live Cadastre",
                        color = GovEmeraldDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Parcels List
            items(parcels, key = { it.ulpin }) { parcel ->
                ParcelCard(parcel = parcel)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
}

@Composable
fun ParcelCard(parcel: Parcel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GovSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: ULPIN & Health Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GovBlueLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = parcel.ulpin,
                            color = GovBlue,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Health Score Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (parcel.healthScore >= 80) GovEmeraldLight else Color(0xFFFEF3C7))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Health Score: ${parcel.healthScore}/100",
                        color = if (parcel.healthScore >= 80) GovEmeraldDark else Color(0xFFB45309),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Details Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "SURVEY NUMBER", color = GovSlate400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = parcel.surveyNumber, color = GovSlate900, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "AREA", color = GovSlate400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${parcel.areaHectares} Ha", color = GovSlate900, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "LAND TYPE", color = GovSlate400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = parcel.landType, color = GovSlate900, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = GovSlate400,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${parcel.village}, Taluka ${parcel.taluka}, ${parcel.district}, ${parcel.state}",
                    color = GovSlate600,
                    fontSize = 12.sp
                )
            }

            // Encumbrance & Tax Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GovBg)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = if (parcel.encumbranceText.contains("dispute", ignoreCase = true)) GovSaffron else GovEmerald,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = parcel.encumbranceText,
                            color = GovSlate700,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = if (parcel.taxPaid) "Tax Paid" else "Tax Due: ₹${parcel.taxAnnual.toInt()}",
                        color = if (parcel.taxPaid) GovEmeraldDark else GovSaffron,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
