package com.example.bhusabha.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhusabha.model.DeedType
import com.example.bhusabha.model.MutationApplication
import com.example.bhusabha.model.RegistrationFormData
import com.example.bhusabha.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun NewRegistrationScreen(
    viewModel: BhuSabhaViewModel,
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val citizenUser by viewModel.citizenUser.collectAsState()
    val formData by viewModel.registrationDraft.collectAsState()
    val toastMessage by viewModel.syncToastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var currentStep by remember { mutableIntStateOf(1) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var submittedApp by remember { mutableStateOf<MutationApplication?>(null) }
    var isConsentChecked by remember { mutableStateOf(true) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissToast()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            // Persistent Bottom Bar for Step Actions matching screenshots
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GovSurface)
                    .border(1.dp, GovCardBorder)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep == 1) {
                        OutlinedButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .weight(0.35f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GovSlate200)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                tint = GovSlate600,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Cancel", color = GovSlate700, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { currentStep = 2 },
                            modifier = Modifier
                                .weight(0.65f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GovSaffron)
                        ) {
                            Text(text = "Proceed to Step 2: Deed Details", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else if (currentStep < 4) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier
                                .weight(0.35f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GovSlate200)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = GovSlate600,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Back", color = GovSlate700, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { currentStep++ },
                            modifier = Modifier
                                .weight(0.65f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GovSaffron)
                        ) {
                            Text(
                                text = if (currentStep == 2) "Next: Geo-Parcel Details" else "Next: Payment & Submission",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        // Step 4 Review & E-Sign
                        OutlinedButton(
                            onClick = { currentStep = 3 },
                            modifier = Modifier
                                .weight(0.35f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GovSlate200)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = GovSlate600,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Back", color = GovSlate700, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                if (isConsentChecked) {
                                    viewModel.submitRegistration { app ->
                                        submittedApp = app
                                        showSuccessDialog = true
                                    }
                                }
                            },
                            enabled = isConsentChecked,
                            modifier = Modifier
                                .weight(0.65f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GovEmerald,
                                disabledContainerColor = GovSlate400
                            )
                        ) {
                            Text(text = "Pay & Submit Application", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GovBg)
                .padding(innerPadding)
        ) {
            // 1. Top Government Header
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
                        IconButton(onClick = {
                            if (currentStep > 1) currentStep-- else onNavigateBack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Column {
                            Text(
                                text = "New Registration",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (currentStep) {
                                    1 -> "Step 1 of 4 • Document Upload & OCR"
                                    2 -> "Step 2 of 4 • Deed & Parties"
                                    3 -> "Step 3 of 4 • Geo-Parcel & Demarcation"
                                    else -> "Step 4 of 4 • Review & E-Sign"
                                },
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // User Initials Badge
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GovSaffron),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = citizenUser.initials,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. Step Progress Indicator Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = GovSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(GovNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$currentStep",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = when (currentStep) {
                                    1 -> "Document & OCR"
                                    2 -> "Deed & Parties"
                                    3 -> "Geo-Parcel Boundary"
                                    else -> "Review & E-Sign"
                                },
                                color = GovSlate900,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Step $currentStep/4 (${currentStep * 25}%)",
                            color = if (currentStep == 4) GovEmeraldDark else GovSaffron,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 4-step indicator bars
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (step in 1..4) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (step <= currentStep) GovNavy else GovSlate200)
                            )
                        }
                    }
                }
            }

            // 3. Step Content (Scrollable)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (currentStep) {
                    1 -> Step1DocumentOcr(
                        formData = formData,
                        buyerName = citizenUser.name,
                        onReuploadClick = {
                            viewModel.updateRegistrationDraft {
                                it.copy(ocrParsedPercent = 99.8)
                            }
                        }
                    )
                    2 -> Step2DeedAndParties(
                        formData = formData,
                        buyerUser = citizenUser,
                        onDeedTypeSelected = { deed ->
                            viewModel.updateRegistrationDraft { it.copy(deedType = deed) }
                        }
                    )
                    3 -> Step3GeoParcelBoundary(
                        formData = formData
                    )
                    4 -> Step4ReviewAndESign(
                        formData = formData,
                        isConsentChecked = isConsentChecked,
                        onConsentToggled = { isConsentChecked = it }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    // Success Dialog on Application Submission
    if (showSuccessDialog && submittedApp != null) {
        val app = submittedApp!!
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onComplete()
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GovEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Mutation Filed Successfully!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = GovSlate900
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Application Token: ${app.id}",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = GovBlue
                    )
                    Text(
                        text = "ULPIN: ${app.ulpin}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = GovSlate600
                    )
                    Text(
                        text = "Status: ${app.status.label}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = GovEmeraldDark
                    )
                    Text(
                        text = "Your application has been routed to Haveli Sub-division, Pune Circle No. 4 for official survey inspection and digital RoR record endorsement.",
                        fontSize = 12.sp,
                        color = GovSlate700
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onComplete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavy)
                ) {
                    Text("Return to Dashboard")
                }
            }
        )
    }
}

// -------------------------------------------------------------------------------------------------
// STEP 1: Document Upload & AI OCR
// -------------------------------------------------------------------------------------------------
@Composable
fun Step1DocumentOcr(
    formData: RegistrationFormData,
    buyerName: String,
    onReuploadClick: () -> Unit
) {
    // 1. Dashed Upload & Scan Deed Box
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF0F7FF))
            .border(
                width = 1.5.dp,
                color = Color(0xFF60A5FA),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onReuploadClick() }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(GovBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DocumentScanner,
                    contentDescription = "Scan Deed",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Text(
                text = "Upload & Scan Deed (AI OCR)",
                color = GovNavy,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "PDF, JPG or TIFF up to 25MB. AI auto-extracts parties, ULPIN, stamp duty and Khata.",
                color = GovSlate500,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(50.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GovEmerald,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "Deed Parsed (99.8% Match)",
                    color = GovNavy,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // 2. OCR INGESTION SUMMARY Card
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GovSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = GovSaffron,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "OCR INGESTION SUMMARY",
                        color = GovNavy,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(GovEmeraldLight)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "● 99.8% Parsed",
                        color = GovEmeraldDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(color = GovSlate100, thickness = 1.dp)

            OcrSummaryRow("Registered Document No:", formData.regDocNumber, isMono = true)
            OcrSummaryRow("Sub-Registrar Office:", formData.sroOffice)
            OcrSummaryRow("Execution & Reg. Date:", formData.executionDate)
            OcrSummaryRow("Transferor (Seller):", formData.sellerName)
            OcrSummaryRow("Transferee (Buyer):", buyerName, valueColor = GovBlue, isBold = true)
            OcrSummaryRow(
                "Deed Consideration:",
                "₹${formatIndianCurrency(formData.deedConsideration)}",
                valueColor = GovEmeraldDark,
                isMono = true,
                isBold = true
            )
        }
    }

    // 3. Security Cross-Verification Notice
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GovSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = GovNavy,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Deed hash cryptographically cross-verified against National Generic Document Registration System (NGDRS).",
                color = GovSlate600,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun OcrSummaryRow(
    label: String,
    value: String,
    valueColor: Color = GovSlate900,
    isMono: Boolean = false,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = GovSlate500,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 12.sp,
            fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}

// -------------------------------------------------------------------------------------------------
// STEP 2: Deed & Parties
// -------------------------------------------------------------------------------------------------
@Composable
fun Step2DeedAndParties(
    formData: RegistrationFormData,
    buyerUser: com.example.bhusabha.model.CitizenUser,
    onDeedTypeSelected: (DeedType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Section: Mutation / Transaction Type
        Text(
            text = "MUTATION / TRANSACTION TYPE",
            color = GovSlate500,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        // 2x2 Grid for Deed Types
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DeedTypeCard(
                    deedType = DeedType.SALE_DEED,
                    icon = Icons.Default.Home,
                    isSelected = formData.deedType == DeedType.SALE_DEED,
                    onClick = { onDeedTypeSelected(DeedType.SALE_DEED) },
                    modifier = Modifier.weight(1f)
                )
                DeedTypeCard(
                    deedType = DeedType.INHERITANCE,
                    icon = Icons.Default.Groups,
                    isSelected = formData.deedType == DeedType.INHERITANCE,
                    onClick = { onDeedTypeSelected(DeedType.INHERITANCE) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DeedTypeCard(
                    deedType = DeedType.GIFT_DEED,
                    icon = Icons.Default.CardGiftcard,
                    isSelected = formData.deedType == DeedType.GIFT_DEED,
                    onClick = { onDeedTypeSelected(DeedType.GIFT_DEED) },
                    modifier = Modifier.weight(1f)
                )
                DeedTypeCard(
                    deedType = DeedType.PARTITION,
                    icon = Icons.AutoMirrored.Filled.CallSplit,
                    isSelected = formData.deedType == DeedType.PARTITION,
                    onClick = { onDeedTypeSelected(DeedType.PARTITION) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Transferor (Seller) Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GovSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = GovSaffron,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "TRANSFEROR (SELLER) DETAILS",
                            color = GovNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(GovEmeraldLight)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "OCR POPULATED",
                            color = GovEmeraldDark,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                OcrSummaryRow("Seller Full Name:", formData.sellerName, isBold = true)
                OcrSummaryRow("Father's / Spouse's Name:", formData.sellerFatherName)
                OcrSummaryRow("Aadhaar / ID Token:", formData.sellerAadhaarMasked, isMono = true)
                OcrSummaryRow("Share in Parcel:", formData.sellerShare, isMono = true)
            }
        }

        // Transferee (Buyer) Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GovSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = GovBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "TRANSFEREE (BUYER) DETAILS",
                            color = GovNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(GovBlueLight)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "PRIMARY APPLICANT",
                            color = GovBlue,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                OcrSummaryRow("Buyer Full Name:", buyerUser.name, valueColor = GovNavy, isBold = true)
                OcrSummaryRow("Citizen ID:", buyerUser.citizenId, isMono = true)
                OcrSummaryRow("Registered Mobile:", buyerUser.mobileNumber)
                OcrSummaryRow("e-KYC Status:", "✓ Biometric Linked", valueColor = GovEmeraldDark, isBold = true)
            }
        }
    }
}

@Composable
fun DeedTypeCard(
    deedType: DeedType,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GovBlueLight else GovSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) GovBlue else GovCardBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) GovBlue else GovSlate400,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = deedType.label,
                    color = if (isSelected) GovNavy else GovSlate900,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = deedType.subtitle,
                    color = GovSlate500,
                    fontSize = 10.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STEP 3: Geo-Parcel & Demarcation
// -------------------------------------------------------------------------------------------------
@Composable
fun Step3GeoParcelBoundary(
    formData: RegistrationFormData
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // 1. Cadastral Map Canvas Container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Cadastral Blueprint / Survey Grid Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines
                    val step = 40.dp.toPx()
                    var x = 0f
                    while (x < w) {
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(x, 0f),
                            end = Offset(x, h),
                            strokeWidth = 1f
                        )
                        x += step
                    }
                    var y = 0f
                    while (y < h) {
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                        y += step
                    }

                    // Adjacent dashed parcels
                    val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)

                    // Parcel Left
                    val pLeft = Path().apply {
                        moveTo(w * 0.05f, h * 0.15f)
                        lineTo(w * 0.38f, h * 0.18f)
                        lineTo(w * 0.35f, h * 0.45f)
                        lineTo(w * 0.08f, h * 0.40f)
                        close()
                    }
                    drawPath(pLeft, color = Color(0xFF93C5FD), style = Stroke(width = 1.5f, pathEffect = dashedEffect))

                    // Parcel Right
                    val pRight = Path().apply {
                        moveTo(w * 0.68f, h * 0.14f)
                        lineTo(w * 0.95f, h * 0.16f)
                        lineTo(w * 0.90f, h * 0.50f)
                        lineTo(w * 0.63f, h * 0.46f)
                        close()
                    }
                    drawPath(pRight, color = Color(0xFF93C5FD), style = Stroke(width = 1.5f, pathEffect = dashedEffect))

                    // Active Parcel Polygon (Sy. 142/3-B)
                    val pActive = Path().apply {
                        moveTo(w * 0.38f, h * 0.35f) // top left
                        lineTo(w * 0.64f, h * 0.38f) // top right
                        lineTo(w * 0.60f, h * 0.85f) // bottom right
                        lineTo(w * 0.35f, h * 0.85f) // bottom left
                        close()
                    }

                    // Active Fill
                    drawPath(pActive, color = Color(0x332563EB))
                    // Active Orange Border
                    drawPath(pActive, color = Color(0xFFEA580C), style = Stroke(width = 3.5.dp.toPx()))

                    // Vertices (Orange anchor dots with white outline)
                    val points = listOf(
                        Offset(w * 0.38f, h * 0.35f),
                        Offset(w * 0.64f, h * 0.38f),
                        Offset(w * 0.60f, h * 0.85f),
                        Offset(w * 0.35f, h * 0.85f)
                    )
                    points.forEach { pt ->
                        drawCircle(color = Color.White, radius = 6.dp.toPx(), center = pt)
                        drawCircle(color = Color(0xFFEA580C), radius = 4.dp.toPx(), center = pt)
                    }
                }

                // Top Left Chip: Cadastral Sy. 142/3-B
                Box(
                    modifier = Modifier
                        .padding(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GovNavy)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Cadastral Sy. ${formData.surveyNumber}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Center Label inside Polygon
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sy. ${formData.surveyNumber} Boundary",
                        color = GovNavy,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Auto-snapped to DILRMP",
                        color = GovSaffron,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Bottom Strip Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.95f))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
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
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = GovEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Boundary Concordance: 100%",
                                color = GovEmeraldDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "DILRMP-GIS",
                            color = GovSlate400,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. Cadastral Spatial Coordinates Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GovSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GovBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "CADASTRAL SPATIAL COORDINATES",
                        color = GovNavy,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                OcrSummaryRow("Survey / Sub-Division:", "${formData.surveyNumber} (${formData.subDivision})", isBold = true)
                OcrSummaryRow("Calculated Area:", "${formData.calculatedArea} Hectares (Matches Deed)", valueColor = GovEmeraldDark, isBold = true)
                OcrSummaryRow("Overlap / Anomaly:", "✓ Nil Encroachment", valueColor = GovEmeraldDark, isBold = true)
                OcrSummaryRow("CORS Geodetic Datum:", formData.geodeticDatum, isMono = true)
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STEP 4: Review & E-Sign
// -------------------------------------------------------------------------------------------------
@Composable
fun Step4ReviewAndESign(
    formData: RegistrationFormData,
    isConsentChecked: Boolean,
    onConsentToggled: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // 1. Application Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GovSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = GovSaffron,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "APPLICATION SUMMARY",
                            color = GovNavy,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(GovEmeraldLight)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "READY",
                            color = GovEmeraldDark,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                OcrSummaryRow("Mutation Type:", "${formData.deedType.label} Transfer", isBold = true)
                OcrSummaryRow(
                    "Declared Parcel Value:",
                    "₹${formatIndianCurrency(formData.declaredValue)}",
                    isMono = true,
                    isBold = true
                )
                OcrSummaryRow("Parcel ULPIN:", formData.ulpin, isMono = true, isBold = true)
                OcrSummaryRow("Seller & Buyer:", "Verified ✓", valueColor = GovEmeraldDark, isBold = true)
            }
        }

        // 2. Fee & Stamp Duty Breakdown Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GovSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = GovSaffron,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "FEE & STAMP DUTY BREAKDOWN",
                            color = GovNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "GRAS Linked",
                        color = GovSlate400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                HorizontalDivider(color = GovSlate100, thickness = 1.dp)

                OcrSummaryRow("Stamp Duty (5%):", "₹${formatIndianCurrency(formData.stampDuty)}", isMono = true, isBold = true)
                OcrSummaryRow("Registration Fee (1%):", "₹${formatIndianCurrency(formData.registrationFee)}", isMono = true, isBold = true)
                OcrSummaryRow("Processing Cess:", "₹${formatIndianCurrency(formData.processingCess)}", isMono = true, isBold = true)

                // Total Payable Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Payable:",
                            color = GovNavy,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹${formatIndianCurrency(formData.totalPayable)}",
                            color = GovBlue,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Text(
                    text = "Treasury Challan Generated (GRAS portal linked)",
                    color = GovSlate400,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 3. Biometric / Aadhaar E-Sign Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GovSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = GovBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "BIOMETRIC / AADHAAR E-SIGN",
                            color = GovNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(GovEmeraldLight)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Ready to e-Sign",
                            color = GovEmeraldDark,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Consent Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GovSlate100)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = isConsentChecked,
                            onCheckedChange = onConsentToggled,
                            colors = CheckboxDefaults.colors(
                                checkedColor = GovSaffron,
                                checkmarkColor = Color.White
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "I hereby authenticate this mutation filing via Aadhaar e-Sign and consent to cryptographic RoR generation.",
                            color = GovSlate700,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

fun formatIndianCurrency(amount: Long): String {
    val formatter = NumberFormat.getInstance(Locale.forLanguageTag("en-IN"))
    return formatter.format(amount)
}
