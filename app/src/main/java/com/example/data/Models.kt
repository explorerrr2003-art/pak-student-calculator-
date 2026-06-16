package com.example.data

// Subject model for Matric / FSc calculators
data class SubjectItem(
    val name: String,
    val totalMarks: String = "100",
    val obtainedMarks: String = ""
)

// Semester model for CGPA calculator
data class SemesterItem(
    val id: Int,
    val name: String,
    val gpa: String = "",
    val creditHours: String = ""
)

// University Eligibility suggestion helper on Merit Screen
data class UniversityEligibility(
    val university: String,
    val region: String,
    val typicalMeritRange: String,
    val matchChance: String, // "High", "Medium", "Borderline"
    val testName: String
)
