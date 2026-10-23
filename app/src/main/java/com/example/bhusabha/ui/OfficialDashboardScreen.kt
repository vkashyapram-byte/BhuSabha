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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhusabha.model.MutationApplication
import com.example.bhusabha.model.MutationStatus
import com.example.bhusabha.theme.GovAmber
import com.example.bhusabha.theme.GovBg
import com.example.bhusabha.theme.GovBlue
import com.example.bhusabha.theme.GovCardBorder
import com.example.bhusabha.theme.GovCrimson
import com.example.bhusabha.theme.GovEmerald
import com.example.bhusabha.theme.GovEmeraldDark
import com.example.bhusabha.theme.GovEmeraldLight
import com.example.bhusabha.theme.GovNavy
import com.example.bhusabha.theme.GovSaffron
import com.example.bhusabha.theme.GovSaffronLight
import com.example.bhusabha.theme.GovSlate400
import com.example.bhusabha.theme.GovSlate500
import com.example.bhusabha.theme.GovSlate600
import com.example.bhusabha.theme.GovSlate700
import com.example.bhusabha.theme.GovSlate800
import com.example.bhusabha.theme.GovSlate900
import com.example.bhusabha.theme.GovSurface

@Composable
fun OfficialDashboardScreen(
    viewModel: BhuSabhaViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val officialUser by viewModel.officialUser.collectAsState()
    val mutations by viewModel.mutations.collectAsState()

    var showObjectionDialogForApp by remember { mutableStateOf<MutationApplication?>(null) }
    var objectionReasonInput by remember { mutableStateOf("") }

    val pendingCount = mutations.count { it.status == MutationStatus.PENDING_INSPECTION }
    val flaggedCount = mutations.count { it.status == MutationStatus.DISCREPANCY_FLAGGED }
    val approvedCount = mutations.count { it.status == MutationStatus.APPROVED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GovBg)
            .statusBarsPadding()
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GovSurface)
                .border(width = 1.dp, color = GovCardBorder)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(GovSaffronLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Officer Profile",
                        tint = GovSaffron,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = officialUser?.name ?: "Revenue Officer",
                            color = GovSlate900,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GovSaffron)
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "AUTH",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "${officialUser?.designation ?: "Tahsildar"} • ${officialUser?.jurisdiction ?: "Pune"}",
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
                    imageVector = Icons.Default.Logout,
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
                Spacer(modifier = Modifier.height(4.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatWidget(
                        title = "PENDING MUTATIONS",
                        value = pendingCount.toString(),
                        color = GovAmber,
                        modifier = Modifier.weight(1f)
                    )
                    StatWidget(
                        title = "AI RISK FLAGGED",
                        value = flaggedCount.toString(),
                        color = GovCrimson,
                        modifier = Modifier.weight(1f)
                    )
                    StatWidget(
                        title = "APPROVED (RoR)",
                        value = approvedCount.toString(),
                        color = GovEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WORKFLOW AUDIT & INSPECTION QUEUE (${mutations.size})",
                        color = GovSlate500,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Real-time Cadastre",
                        color = GovSaffron,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Mutation Tasks List
            items(mutations, key = { it.id }) { mutation ->
                MutationTaskCard(
                    mutation = mutation,
                    onApprove = { viewModel.approveMutation(mutation.id) },
                    onRaiseObjection = {
                        showObjectionDialogForApp = mutation
                        objectionReasonInput = "Area discrepancy detected between deed and revenue map."
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Objection Dialog
    if (showObjectionDialogForApp != null) {
        val app = showObjectionDialogForApp!!
        AlertDialog(
            onDismissRequest = { showObjectionDialogForApp = null },
            title = {
                Text(
                    text = "Raise Discrepancy Objection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Application: ${app.id} (ULPIN: ${app.ulpin})",
                        fontSize = 12.sp,
                        color = GovSlate500
                    )
                    Text(
                        text = "Specify legal or spatial discrepancy reason:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    OutlinedTextField(
                        value = objectionReasonInput,
                        onValueChange = { objectionReasonInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.raiseObjection(app.id, objectionReasonInput)
                        showObjectionDialogForApp = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovCrimson)
                ) {
                    Text("Flag Objection")
                }
            },
            dismissButton = {
                TextButton(onClick = { showObjectionDialogForApp = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StatWidget(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GovSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = GovSlate400,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                color = color,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun MutationTaskCard(
    mutation: MutationApplication,
    onApprove: () -> Unit,
    onRaiseObjection: () -> Unit
) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: ID and Status Pill
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
                            .background(GovSaffronLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = mutation.id,
                            color = GovSaffron,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Status Badge
                val statusBg = when (mutation.status) {
                    MutationStatus.PENDING_INSPECTION -> Color(0xFFFEF3C7)
                    MutationStatus.APPROVED -> GovEmeraldLight
                    MutationStatus.DISCREPANCY_FLAGGED -> Color(0xFFFEE2E2)
                }
                val statusColor = when (mutation.status) {
                    MutationStatus.PENDING_INSPECTION -> Color(0xFFB45309)
                    MutationStatus.APPROVED -> GovEmeraldDark
                    MutationStatus.DISCREPANCY_FLAGGED -> GovCrimson
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(statusBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = mutation.status.label,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Deed & ULPIN
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = mutation.deedType,
                    color = GovSlate900,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ULPIN: ${mutation.ulpin} • Filed: ${mutation.submissionDate}",
                    color = GovSlate500,
                    fontSize = 11.sp
                )
            }

            // Transfer Flow (Current -> Proposed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GovBg)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "CURRENT OWNER", color = GovSlate400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = mutation.currentOwner, color = GovSlate800, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = GovSlate400,
                    modifier = Modifier.size(16.dp)
                )

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "PROPOSED OWNER", color = GovSlate400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = mutation.proposedOwner, color = GovBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Discrepancy Note banner if present
            if (mutation.discrepancyNote != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF2F2))
                        .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = GovCrimson,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = mutation.discrepancyNote,
                        color = GovCrimson,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Action Buttons
            if (mutation.status != MutationStatus.APPROVED) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GovEmerald)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text(text = "Approve RoR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedButton(
                        onClick = onRaiseObjection,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GovCrimson)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ReportProblem, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text(text = "Objection", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GovEmerald, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Order Dispatched & RoR Updated in State Cadastre",
                        color = GovEmeraldDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
