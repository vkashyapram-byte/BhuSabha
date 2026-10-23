package com.example.bhusabha.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhusabha.theme.GovAmberGold
import com.example.bhusabha.theme.GovBg
import com.example.bhusabha.theme.GovBlue
import com.example.bhusabha.theme.GovBlueLight
import com.example.bhusabha.theme.GovCardBorder
import com.example.bhusabha.theme.GovEmerald
import com.example.bhusabha.theme.GovEmeraldBorder
import com.example.bhusabha.theme.GovEmeraldDark
import com.example.bhusabha.theme.GovEmeraldLight
import com.example.bhusabha.theme.GovNavy
import com.example.bhusabha.theme.GovSaffron
import com.example.bhusabha.theme.GovSaffronLight
import com.example.bhusabha.theme.GovSlate400
import com.example.bhusabha.theme.GovSlate500
import com.example.bhusabha.theme.GovSlate600
import com.example.bhusabha.theme.GovSlate900
import com.example.bhusabha.theme.GovSurface

@Composable
fun PersonaWelcomeScreen(
    onCitizenLoginClick: () -> Unit,
    onOfficialLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GovBg)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Top Bar: Offline Ready Chip & Digital India
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Pill: Offline Ready
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(GovEmeraldLight)
                    .border(1.dp, GovEmeraldBorder, RoundedCornerShape(50.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(GovEmerald)
                        .alpha(pulseAlpha)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(GovEmerald)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Offline Ready (v3.2) - Auto-Sync Active",
                    color = GovEmeraldDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.2).sp
                )
            }

            // Right Item: Digital India
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = "Digital India",
                    tint = GovSlate400,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Digital India",
                    color = GovSlate400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 2. Center Branding & Emblem
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Emblem with ambient glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(96.dp)
            ) {
                // Ambient glow layer
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .blur(16.dp)
                        .background(
                            color = GovSaffron.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(24.dp)
                        )
                )

                // Main Gradient Emblem
                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(22.dp))
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF1E1B4B),
                                    Color(0xFF312E81),
                                    GovSaffron
                                )
                            )
                        )
                        .border(2.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Scales of Justice Icon (Custom Path representation)
                    ScalesOfJusticeIcon(
                        modifier = Modifier.size(38.dp),
                        tint = Color.White
                    )
                }

                // Bottom Right Verified Gold Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(28.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(GovAmberGold)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Verified Cadastre",
                        tint = GovNavy,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // App Title Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "BhuSabha",
                    color = GovNavy,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(GovBlue)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "GOV.IN",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Platform Subtitle
            Text(
                text = "NATIONAL DIGITAL LAND GOVERNANCE PLATFORM",
                color = GovEmeraldDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description Paragraph
            Text(
                text = "Ministry of Rural Development & Department of Land Resources. Unified registry, DILRMP spatial GIS, and cryptographic RoR verification.",
                color = GovSlate500,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Zero-Trust & NGDRS Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(GovSurface)
                    .border(1.dp, GovCardBorder, RoundedCornerShape(50.dp))
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Zero-Trust",
                        tint = GovBlue,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Zero-Trust Cadastre",
                        color = GovSlate600,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "•",
                    color = GovSlate400,
                    fontSize = 12.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Hub,
                        contentDescription = "NGDRS",
                        tint = GovSaffron,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "NGDRS Synced",
                        color = GovSlate600,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // 3. Select Portal Persona Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SELECT PORTAL PERSONA",
                color = GovSlate400,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Card 1: Login as Citizen
            PersonaCard(
                title = "Login as Citizen",
                subtitle = "View titles, e-Passbook & mutation status",
                iconColor = GovBlue,
                iconBackgroundColor = GovBlueLight,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Citizen",
                        tint = GovBlue,
                        modifier = Modifier.size(26.dp)
                    )
                },
                onClick = onCitizenLoginClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Card 2: Login as Official
            PersonaCard(
                title = "Login as Official",
                subtitle = "Revenue Inspector, Tahsildar & Sub-Registrar Portal",
                iconColor = GovSaffron,
                iconBackgroundColor = GovSaffronLight,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Official",
                        tint = GovSaffron,
                        modifier = Modifier.size(26.dp)
                    )
                },
                onClick = onOfficialLoginClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Footer Text
            Text(
                text = "Secured with Aadhaar OTP & DigiLocker Integration",
                color = GovSlate400,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PersonaCard(
    title: String,
    subtitle: String,
    iconColor: Color,
    iconBackgroundColor: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GovSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GovCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }

                // Texts
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = title,
                        color = GovSlate900,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = subtitle,
                        color = GovSlate500,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Enter",
                tint = GovSlate400,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * Custom Scales of Justice Icon drawing for high-fidelity Gov emblem
 */
@Composable
fun ScalesOfJusticeIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.White
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Central vertical beam
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.15f),
            end = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.85f),
            strokeWidth = 2.5f
        )
        // Center finial ball
        drawCircle(
            color = tint,
            radius = 3f,
            center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.15f)
        )
        // Base plate
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(w * 0.35f, h * 0.85f),
            end = androidx.compose.ui.geometry.Offset(w * 0.65f, h * 0.85f),
            strokeWidth = 3f
        )
        // Horizontal balance bar
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(w * 0.15f, h * 0.32f),
            end = androidx.compose.ui.geometry.Offset(w * 0.85f, h * 0.32f),
            strokeWidth = 2.5f
        )

        // Left Pan Strings
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(w * 0.22f, h * 0.32f),
            end = androidx.compose.ui.geometry.Offset(w * 0.14f, h * 0.55f),
            strokeWidth = 1.5f
        )
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(w * 0.22f, h * 0.32f),
            end = androidx.compose.ui.geometry.Offset(w * 0.30f, h * 0.55f),
            strokeWidth = 1.5f
        )
        // Left Pan arc
        drawArc(
            color = tint,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.12f, h * 0.46f),
            size = androidx.compose.ui.geometry.Size(w * 0.20f, h * 0.18f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
        )

        // Right Pan Strings
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(w * 0.78f, h * 0.32f),
            end = androidx.compose.ui.geometry.Offset(w * 0.70f, h * 0.55f),
            strokeWidth = 1.5f
        )
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(w * 0.78f, h * 0.32f),
            end = androidx.compose.ui.geometry.Offset(w * 0.86f, h * 0.55f),
            strokeWidth = 1.5f
        )
        // Right Pan arc
        drawArc(
            color = tint,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.68f, h * 0.46f),
            size = androidx.compose.ui.geometry.Size(w * 0.20f, h * 0.18f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
        )
    }
}
