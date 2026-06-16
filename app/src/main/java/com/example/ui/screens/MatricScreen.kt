package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
import com.example.data.SubjectItem
import com.example.ui.components.AdSensePlaceholder
import com.example.ui.components.ResultCard
import com.example.ui.components.ShareResultButton
import com.example.utils.Calculators

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatricScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // 6 Default Matric subjects
    val defaultSubjects = remember {
        listOf(
            SubjectItem("English (انگریزی)", "100", "75"),
            SubjectItem("Urdu (اردو)", "100", "80"),
            SubjectItem("Mathematics (ریاضی)", "100", "85"),
            SubjectItem("Physics (طبیعیات)", "100", "72"),
            SubjectItem("Chemistry (کیمسٹری)", "100", "68"),
            SubjectItem("Biology / Comp (حیاتیات)", "100", "78")
        )
    }

    var subjects by remember { mutableStateOf(defaultSubjects) }
    var resultCalculated by remember { mutableStateOf<Calculators.SchoolResult?>(Calculators.calculateSchoolResult(defaultSubjects)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matric Board Calculator", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        subjects = defaultSubjects.map { it.copy(obtainedMarks = "") }
                        resultCalculated = null
                    }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset Form")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
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
            // Top Ad Banner
            AdSensePlaceholder(id = "ad-top", isRectangle = false)

            Spacer(modifier = Modifier.height(8.dp))

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
                        text = "Enter Subject Marks",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Customize subject names, obtained scores, and total limits as needed.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Column Table Header Row to guide the compact inputs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Subject Name", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1.5f))
                        Text("Obtained Marks", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f))
                        Text("Total", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(0.8f))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subjects Rows
                    subjects.forEachIndexed { index, sub ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Subject Name Entry
                            OutlinedTextField(
                                value = sub.name,
                                onValueChange = { newVal ->
                                    subjects = subjects.toMutableList().apply {
                                        this[index] = this[index].copy(name = newVal)
                                    }
                                },
                                placeholder = { Text("Subject", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                                modifier = Modifier.weight(1.5f),
                                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            // Obtained Marks Field
                            OutlinedTextField(
                                value = sub.obtainedMarks,
                                onValueChange = { newVal ->
                                    if (newVal.all { it.isDigit() }) {
                                        subjects = subjects.toMutableList().apply {
                                            this[index] = this[index].copy(obtainedMarks = newVal)
                                        }
                                    }
                                },
                                placeholder = { Text("Obtained", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.weight(1f),
                                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            // Total Marks Field
                            OutlinedTextField(
                                value = sub.totalMarks,
                                onValueChange = { newVal ->
                                    if (newVal.all { it.isDigit() }) {
                                        subjects = subjects.toMutableList().apply {
                                            this[index] = this[index].copy(totalMarks = newVal)
                                        }
                                    }
                                },
                                placeholder = { Text("Total", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.weight(0.8f),
                                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Calculate button inside input card
                    Button(
                        onClick = {
                            resultCalculated = Calculators.calculateSchoolResult(subjects)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Calculate Matric Result", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AdSense placeholder between inputs and result
            AdSensePlaceholder(id = "ad-result-sub", isRectangle = false)

            Spacer(modifier = Modifier.height(8.dp))

            // Result presentation
            val result = resultCalculated
            if (result != null) {
                ResultCard(
                    visible = true,
                    title = "MATRIC BOARD RESULT (میٹرک رزلٹ)",
                    score = "${result.totalObtained} / ${result.totalMarks}",
                    scoreLabel = "Marks Obtained",
                    grade = result.gradeString,
                    gradeLabel = "Board Grade",
                    remarks = "Percentage: ${String.format("%.2f", result.percentage)}%\n${result.remarks}",
                    isPass = result.isPass
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Format Share Content
                val shareText = buildString {
                    appendLine("📊 *My Matric Result Report Card* 📊")
                    appendLine("---------------------------------")
                    subjects.forEach {
                        if (it.obtainedMarks.isNotEmpty()) {
                            appendLine("• ${it.name}: ${it.obtainedMarks}/${it.totalMarks}")
                        }
                    }
                    appendLine("---------------------------------")
                    appendLine("💰 *Total Marks:* ${result.totalObtained} / ${result.totalMarks}")
                    appendLine("📈 *Percentage:* ${String.format("%.2f", result.percentage)}%")
                    appendLine("⭐ *Grade:* ${result.gradeString}")
                    appendLine("✨ *Outcome:* ${if (result.isPass) "PASS (کامیاب)" else "Needs Improvement"}")
                    appendLine("---------------------------------")
                    appendLine("Calculate your Matric, FSc, and CGPA on the *Pak Student Calculator App* 🇵🇰")
                }

                // WhatsApp Share Button
                ShareResultButton(textToShare = shareText)

                Spacer(modifier = Modifier.height(16.dp))

                // AdSense rectangle below result
                AdSensePlaceholder(id = "ad-result", isRectangle = true)
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Awaiting Marks Entry 📝",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Please enter your obtained marks. Click 'Calculate Matric Result' to compute.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Ad Banner
            AdSensePlaceholder(id = "ad-bottom", isRectangle = false)
        }
    }
}
