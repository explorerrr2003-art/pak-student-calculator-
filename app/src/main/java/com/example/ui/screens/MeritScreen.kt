package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AdSensePlaceholder
import com.example.ui.components.ResultCard
import com.example.ui.components.ShareResultButton
import com.example.utils.Calculators

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeritScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Test options in Pakistan
    val tests = listOf("MDCAT", "ECAT", "NTS NAT")
    var selectedTest by remember { mutableStateOf(tests[0]) }

    // Inputs
    var matricPctText by remember { mutableStateOf("88.5") }
    var fscPctText by remember { mutableStateOf("82.4") }
    var testPctText by remember { mutableStateOf("78.0") }

    var calculatedMerit by remember { mutableStateOf(Calculators.calculateMerit(88.5, 82.4, 78.0)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("University Merit Score Tracker", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Banner
            AdSensePlaceholder(id = "ad-top", isRectangle = false)

            Spacer(modifier = Modifier.height(8.dp))

            // Formula Info Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFBBDEFB), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Formula Banner",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "HEC Merit Formula",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Admission Score = Matric (10%) + Intermediate (40%) + Entry Test (50%)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.82f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Parameters Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Admission Parameters",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Enter percentages directly to simulate your aggregate admission chances.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Entry Test Type selector Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Test Type:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            modifier = Modifier.weight(1f)
                        )
                        
                        Row(
                            modifier = Modifier.weight(3f),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            tests.forEach { test ->
                                val selected = selectedTest == test
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedTest = test }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = test,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }

                    // Input 1: Matric Pct
                    OutlinedTextField(
                        value = matricPctText,
                        onValueChange = { newVal ->
                            if (newVal.isEmpty() || newVal.toDoubleOrNull() != null || newVal.endsWith(".")) {
                                matricPctText = newVal
                            }
                        },
                        label = { Text("Matric / O-Levels Equivalent %", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
                        placeholder = { Text("e.g. 91.5", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Input 2: FSc Pct
                    OutlinedTextField(
                        value = fscPctText,
                        onValueChange = { newVal ->
                            if (newVal.isEmpty() || newVal.toDoubleOrNull() != null || newVal.endsWith(".")) {
                                fscPctText = newVal
                            }
                        },
                        label = { Text("FSc / Intermediate Equivalent %", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
                        placeholder = { Text("e.g. 84.2", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Input 3: Admission Test Pct
                    OutlinedTextField(
                        value = testPctText,
                        onValueChange = { newVal ->
                            if (newVal.isEmpty() || newVal.toDoubleOrNull() != null || newVal.endsWith(".")) {
                                testPctText = newVal
                            }
                        },
                        label = { Text("$selectedTest Marks / Percent Score", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
                        placeholder = { Text("e.g. 75", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Calculate Merit Button inside Card
                    Button(
                        onClick = {
                            val m = matricPctText.toDoubleOrNull() ?: 0.0
                            val f = fscPctText.toDoubleOrNull() ?: 0.0
                            val t = testPctText.toDoubleOrNull() ?: 0.0
                            calculatedMerit = Calculators.calculateMerit(m, f, t)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Calculate Aggregate Merit", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Result presentation
            if (calculatedMerit > 0.0) {
                ResultCard(
                    visible = true,
                    title = "ESTIMATED ADMISSION AGGREGATE",
                    score = String.format("%.2f", calculatedMerit) + "%",
                    scoreLabel = "Calculated Aggregate",
                    grade = if (calculatedMerit >= 85.0) "Excellent" else if (calculatedMerit >= 70.0) "Capable" else "Average",
                    gradeLabel = "Aggregate Standing",
                    remarks = "Calculations based on $selectedTest criteria.\nBased on historical cutoff indices, scroll below to check matching departments & colleges.",
                    isPass = calculatedMerit >= 50.0
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ad placeholder below aggregate
                AdSensePlaceholder(id = "ad-result-sub", isRectangle = false)

                Spacer(modifier = Modifier.height(8.dp))

                // University Eligibility List based on Cutoff Map
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Historical University Match Matrix",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Based on last year's closed cutoffs. Tap on list items to investigate.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Eligible universities
                        val eligibleList = Calculators.getUniversityEligibility(calculatedMerit, selectedTest)
                        eligibleList.forEach { match ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = match.university,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Surface(
                                        color = when {
                                            match.matchChance.contains("High") || match.matchChance.contains("Good") -> Color(0xFFDCFCE7)
                                            match.matchChance.contains("Moderate") -> Color(0xFFFEF9C3)
                                            else -> Color(0xFFFEE2E2)
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = match.matchChance,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when {
                                                match.matchChance.contains("High") || match.matchChance.contains("Good") -> Color(0xFF15803D)
                                                match.matchChance.contains("Moderate") -> Color(0xFF854D0E)
                                                else -> Color(0xFF991B1B)
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Typical cutoff: ${match.typicalMeritRange}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    Text(match.region, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Format Share Content
                val shareText = buildString {
                    appendLine("🇵🇰 *My Pakistani University Merit Estimate* 🇵🇰")
                    appendLine("Admission Test: $selectedTest")
                    appendLine("---------------------------------")
                    appendLine("• Matric Score equivalent: $matricPctText%")
                    appendLine("• FSc Score equivalent: $fscPctText%")
                    appendLine("• Entry Test Score: $testPctText%")
                    appendLine("---------------------------------")
                    appendLine("🎯 *Admission Aggregate:* ${String.format("%.2f", calculatedMerit)}%")
                    appendLine("🌟 *Test Type Target:* $selectedTest")
                    appendLine("👉 Calculated on the *Pak Student Calculator*")
                    appendLine("Instant Matric, Intermediate & Merit Aggregate estimator!")
                }

                // WhatsApp Share Button
                ShareResultButton(textToShare = shareText)

                Spacer(modifier = Modifier.height(16.dp))

                // Ad block below result
                AdSensePlaceholder(id = "ad-result", isRectangle = true)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Ad Banner
            AdSensePlaceholder(id = "ad-bottom", isRectangle = false)
        }
    }
}
