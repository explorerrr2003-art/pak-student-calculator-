package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.example.data.SemesterItem
import com.example.ui.components.AdSensePlaceholder
import com.example.ui.components.ResultCard
import com.example.ui.components.ShareResultButton
import com.example.utils.Calculators

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CgpaScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Semesters State
    var semesters by remember {
        mutableStateOf(
            listOf(
                SemesterItem(1, "Semester 1", "3.67", "18"),
                SemesterItem(2, "Semester 2", "3.45", "17"),
                SemesterItem(3, "Semester 3", "3.20", "18")
            )
        )
    }

    var nextId by remember { mutableStateOf(4) }
    var resultCalculated by remember { mutableStateOf<Calculators.CgpaResult?>(
        Calculators.calculateCgpa(
            listOf(
                SemesterItem(1, "Semester 1", "3.67", "18"),
                SemesterItem(2, "Semester 2", "3.45", "17"),
                SemesterItem(3, "Semester 3", "3.20", "18")
            )
        )
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("University CGPA Calculator", fontWeight = FontWeight.Bold) },
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
            // Top Ad Banner
            AdSensePlaceholder(id = "ad-top", isRectangle = false)

            Spacer(modifier = Modifier.height(8.dp))

            // Main sem input card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Semester Breakdown",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Add Semester Button
                        Button(
                            onClick = {
                                semesters = semesters + SemesterItem(nextId, "Semester $nextId", "", "15")
                                nextId++
                            },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Item",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Sem", fontSize = 12.sp)
                        }
                    }
                    Text(
                        text = "Compute Cumulative GPA (CGPA) on standard 4.0 scale by HEC guidelines.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Semesters Table Header Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Semester", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1.5f))
                        Text("GPA (0-4.0)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1.2f))
                        Text("Credits", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(40.dp)) // space for delete action
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Semesters rows inputs
                    semesters.forEachIndexed { index, sem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = sem.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1.5f)
                            )

                            // GPA Input
                            OutlinedTextField(
                                value = sem.gpa,
                                onValueChange = { newVal ->
                                    if (newVal.isEmpty() || newVal.toDoubleOrNull() != null || newVal.endsWith(".")) {
                                        semesters = semesters.toMutableList().apply {
                                            this[index] = this[index].copy(gpa = newVal)
                                        }
                                    }
                                },
                                placeholder = { Text("e.g. 3.5", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1.2f),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            // Credits Input
                            OutlinedTextField(
                                value = sem.creditHours,
                                onValueChange = { newVal ->
                                    if (newVal.all { it.isDigit() }) {
                                        semesters = semesters.toMutableList().apply {
                                            this[index] = this[index].copy(creditHours = newVal)
                                        }
                                    }
                                },
                                placeholder = { Text("e.g. 18", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            // Delete Action
                            IconButton(
                                onClick = {
                                    if (semesters.size > 1) {
                                        semesters = semesters.filter { it.id != sem.id }
                                    }
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Semester",
                                    tint = if (semesters.size > 1) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Explicit Calculate CGPA Button
                    Button(
                        onClick = {
                            resultCalculated = Calculators.calculateCgpa(semesters)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Calculate Cumulative CGPA", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ad banner between inputs and results
            AdSensePlaceholder(id = "ad-result-sub", isRectangle = false)

            Spacer(modifier = Modifier.height(8.dp))

            // Result presentation
            val result = resultCalculated
            if (result != null) {
                ResultCard(
                    visible = true,
                    title = "CUMULATIVE CGPA OUTCOME",
                    score = String.format("%.2f", result.cgpa),
                    scoreLabel = "CGPA (4.0 Scale)",
                    grade = result.letterGrade,
                    gradeLabel = "HEC Descriptor",
                    remarks = "Equivalent HEC Score: ${String.format("%.1f", result.percentageEquivalent)}%\nTotal Credits Accumulated: ${result.totalCreditHours.toInt()} Hrs\n${result.remarks}",
                    isPass = result.cgpa >= 2.00
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Standard Pakistani Grading Map (HEC)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("3.7 - 4.0: Grade A (85-100%)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f), modifier = Modifier.weight(1f))
                            Text("3.0 - 3.6: Grade B (75-84%)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f), modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                            Text("2.0 - 2.9: Grade C (60-74%)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f), modifier = Modifier.weight(1f))
                            Text("Below 2.0: probation concern", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f), modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Prepare WhatsApp share string
                val shareText = buildString {
                    appendLine("🎓 *My Cumulative University CGPA Report* 🎓")
                    appendLine("---------------------------------")
                    semesters.forEach {
                        if (it.gpa.isNotEmpty()) {
                            appendLine("• ${it.name}: GPA ${it.gpa} (Credits: ${it.creditHours})")
                        }
                    }
                    appendLine("---------------------------------")
                    appendLine("🎯 *Final CGPA:* ${String.format("%.2f", result.cgpa)} / 4.00")
                    appendLine("📊 *HEC Percentage Equivalent:* ${String.format("%.1f", result.percentageEquivalent)}%")
                    appendLine("🏷️ *Equivalent Grade:* ${result.letterGrade}")
                    appendLine("📖 *Total Credit Hours:* ${result.totalCreditHours.toInt()} Hrs")
                    appendLine("---------------------------------")
                    appendLine("Instant Matric, FSc & CGPA tracker free on *Pak Student Calculator* 🇵🇰")
                }

                ShareResultButton(textToShare = shareText)

                Spacer(modifier = Modifier.height(16.dp))

                // Ad Rect
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
                            text = "Awaiting CGPA Entry 📝",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Add semesters and input GPAs, then click 'Calculate Cumulative CGPA' to compute.",
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
