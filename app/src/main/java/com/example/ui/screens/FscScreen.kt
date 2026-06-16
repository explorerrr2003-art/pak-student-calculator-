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
fun FscScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Stream list in Pakistan
    val streams = listOf(
        "Pre-Medical",
        "Pre-Engineering",
        "ICS (Computer Science)",
        "I.Com (Commerce)",
        "F.A (Arts / Humanities)"
    )

    var selectedStream by remember { mutableStateOf(streams[0]) }

    // Map stream to default FSc/FA subjects
    fun getSubjectsForStream(stream: String): List<SubjectItem> {
        return when (stream) {
            "Pre-Medical" -> listOf(
                SubjectItem("English (انگریزی) - 1", "100", "78"),
                SubjectItem("Urdu (اردو) - 1", "100", "82"),
                SubjectItem("Islamic Studies", "50", "42"),
                SubjectItem("Physics (طبیعیات) - 1", "85", "65"),
                SubjectItem("Chemistry (کیمسٹری) - 1", "85", "68"),
                SubjectItem("Biology (حیاتیات) - 1", "85", "70"),
                SubjectItem("Practicals (Phy, Chem, Bio)", "45", "38")
            )
            "Pre-Engineering" -> listOf(
                SubjectItem("English (انگریزی) - 1", "100", "76"),
                SubjectItem("Urdu (اردو) - 1", "100", "80"),
                SubjectItem("Islamic Studies", "50", "43"),
                SubjectItem("Physics (طبیعیات) - 1", "85", "68"),
                SubjectItem("Chemistry (کیمسٹری) - 1", "85", "69"),
                SubjectItem("Mathematics (ریاضی) - 1", "100", "88"),
                SubjectItem("Practicals (Phy, Chem)", "30", "26")
            )
            "ICS (Computer Science)" -> listOf(
                SubjectItem("English (انگریزی) - 1", "100", "75"),
                SubjectItem("Urdu (اردو) - 1", "100", "78"),
                SubjectItem("Islamic Studies", "50", "40"),
                SubjectItem("Physics / Stats", "85", "62"),
                SubjectItem("Computer Studies", "75", "64"),
                SubjectItem("Mathematics (ریاضی) - 1", "100", "80"),
                SubjectItem("Computer Practicals", "50", "44")
            )
            "I.Com (Commerce)" -> listOf(
                SubjectItem("English (انگریزی)", "100", "72"),
                SubjectItem("Urdu (اردو)", "100", "75"),
                SubjectItem("Islamic/Pak Studies", "50", "41"),
                SubjectItem("Principles of Accounting", "100", "82"),
                SubjectItem("Principles of Economics", "75", "62"),
                SubjectItem("Principles of Commerce", "75", "58"),
                SubjectItem("Business Mathematics", "50", "44")
            )
            else -> listOf(
                SubjectItem("English (انگریزی)", "100", "70"),
                SubjectItem("Urdu (اردو)", "100", "74"),
                SubjectItem("Islamic/Pak Studies", "50", "38"),
                SubjectItem("Elective Arts Subject 1", "100", "78"),
                SubjectItem("Elective Arts Subject 2", "100", "72"),
                SubjectItem("Elective Arts Subject 3", "100", "65")
            )
        }
    }

    var subjects by remember { mutableStateOf(getSubjectsForStream(selectedStream)) }
    var resultCalculated by remember { mutableStateOf<Calculators.SchoolResult?>(Calculators.calculateSchoolResult(getSubjectsForStream(selectedStream))) }

    // Synchronize subject reload when stream changes and calculate initial value
    LaunchedEffect(selectedStream) {
        val loaded = getSubjectsForStream(selectedStream)
        subjects = loaded
        resultCalculated = Calculators.calculateSchoolResult(loaded)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FSc / FA Calculator", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        subjects = getSubjectsForStream(selectedStream).map { it.copy(obtainedMarks = "") }
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
            // Top Banner
            AdSensePlaceholder(id = "ad-top", isRectangle = false)

            Spacer(modifier = Modifier.height(8.dp))

            // Stream Selection Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Select Intermediate Stream",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Custom Stream Dropdown Selector representation (Beautiful chips)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            streams.take(3).forEach { stream ->
                                FilterChip(
                                    selected = selectedStream == stream,
                                    onClick = { selectedStream = stream },
                                    label = { Text(stream, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        streams.drop(3).forEach { stream ->
                            FilterChip(
                                selected = selectedStream == stream,
                                onClick = { selectedStream = stream },
                                label = { Text(stream, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subject Marks Card
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
                            text = "$selectedStream Subjects",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
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

                    // Explicit Calculate Button
                    Button(
                        onClick = {
                            resultCalculated = Calculators.calculateSchoolResult(subjects)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Calculate Intermediate Result", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AdSense placeholder under inputs
            AdSensePlaceholder(id = "ad-result-sub", isRectangle = false)

            Spacer(modifier = Modifier.height(8.dp))

            // Result presenting
            val result = resultCalculated
            if (result != null) {
                ResultCard(
                    visible = true,
                    title = "INTERMEDIATE BOARD RESULT ($selectedStream)",
                    score = "${result.totalObtained} / ${result.totalMarks}",
                    scoreLabel = "Total Marks",
                    grade = result.gradeString,
                    gradeLabel = "Board Grade",
                    remarks = "Percentage: ${String.format("%.2f", result.percentage)}%\n${result.remarks}",
                    isPass = result.isPass
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Format Share Content
                val shareText = buildString {
                    appendLine("🎓 *My Intermediate FSc / FA Result Summary* 🎓")
                    appendLine("Stream: $selectedStream")
                    appendLine("---------------------------------")
                    subjects.forEach {
                        if (it.obtainedMarks.isNotEmpty()) {
                            appendLine("• ${it.name}: ${it.obtainedMarks}/${it.totalMarks}")
                        }
                    }
                    appendLine("---------------------------------")
                    appendLine("💰 *Obtained:* ${result.totalObtained} / ${result.totalMarks}")
                    appendLine("📈 *Percentage:* ${String.format("%.2f", result.percentage)}%")
                    appendLine("🌟 *Board Grade:* ${result.gradeString}")
                    appendLine("✨ *Status:* ${if (result.isPass) "SUCCESS" else "Needs Improvement"}")
                    appendLine("---------------------------------")
                    appendLine("Calculate your board results & merit score on the *Pak Student Calculator*!")
                }

                // WhatsApp Share Button
                ShareResultButton(textToShare = shareText)

                Spacer(modifier = Modifier.height(16.dp))

                // AdSense Rect
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
                            text = "Awaiting FSc Marks 📝",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Please enter your obtained marks. Click 'Calculate Intermediate Result' to compute.",
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
