package com.example.utils

import com.example.data.SubjectItem
import com.example.data.UniversityEligibility

object Calculators {

    // Matric & FSc calculation result wrapper
    data class SchoolResult(
        val totalObtained: Int,
        val totalMarks: Int,
        val percentage: Double,
        val gradeString: String,
        val remarks: String, // Urdu/English remarks
        val isPass: Boolean
    )

    // Calculate Matric / FSc Result
    fun calculateSchoolResult(subjects: List<SubjectItem>): SchoolResult? {
        if (subjects.isEmpty()) return null
        
        var totalObtained = 0
        var totalMarks = 0
        var anyInvalid = true

        for (sub in subjects) {
            val obtained = sub.obtainedMarks.toDoubleOrNull() ?: continue
            val total = sub.totalMarks.toDoubleOrNull() ?: 100.0
            totalObtained += obtained.toInt()
            totalMarks += total.toInt()
            anyInvalid = false
        }

        if (anyInvalid || totalMarks == 0) return null

        val percentage = (totalObtained.toDouble() / totalMarks.toDouble()) * 100.0
        
        // Pakistani grading:
        // A1 = 90-100
        // A  = 80-89
        // B  = 70-79
        // C  = 60-69
        // D  = 50-59
        // F  = below 50 (Fail)
        val (grade, remarks, isPass) = when {
            percentage >= 90.0 -> Triple("A1", "Exceptional! (شاندار کارکردگی)", true)
            percentage >= 80.0 -> Triple("A", "Excellent! (بہت خوب)", true)
            percentage >= 70.0 -> Triple("B", "Very Good! (بہت اچھا)", true)
            percentage >= 60.0 -> Triple("C", "Good / Satisfactory (اچھا)", true)
            percentage >= 50.0 -> Triple("D", "Fair / Pass (قابل قبول)", true)
            else -> Triple("F", "Fail (ناکام) - Needs Improvement", false)
        }

        return SchoolResult(
            totalObtained = totalObtained,
            totalMarks = totalMarks,
            percentage = percentage,
            gradeString = grade,
            remarks = remarks,
            isPass = isPass
        )
    }

    // CGPA Calculation outcome wrapper
    data class CgpaResult(
        val cgpa: Double,
        val totalCreditHours: Double,
        val percentageEquivalent: Double,
        val letterGrade: String,
        val remarks: String
    )

    // Calculate CGPA across all entered semester GPAs
    // Formula: Sum(GPA * Credits) / Sum(Credits)
    fun calculateCgpa(semesters: List<com.example.data.SemesterItem>): CgpaResult? {
        var totalCredits = 0.0
        var weightedSum = 0.0
        var validSemesters = 0

        for (sem in semesters) {
            val gpa = sem.gpa.toDoubleOrNull() ?: continue
            val credits = sem.creditHours.toDoubleOrNull() ?: 0.0
            
            // Constrain GPA to 0.0 - 4.0
            val normalizedGpa = gpa.coerceIn(0.0, 4.0)
            if (credits > 0.0) {
                weightedSum += (normalizedGpa * credits)
                totalCredits += credits
                validSemesters++
            }
        }

        if (totalCredits == 0.0 || validSemesters == 0) return null

        val cgpa = weightedSum / totalCredits
        val percentage = (cgpa / 4.0) * 100.0

        // Pakistani university grading equivalent scale:
        // 4.0 = A+ = 90-100%
        // 3.7 = A  = 85-89%
        // ...
        val (grade, remarks) = when {
            cgpa >= 3.90 -> Pair("A+", "Outstanding / Gold Medalist potential! (شاندار)")
            cgpa >= 3.66 -> Pair("A", "Excellent / Dean's List (بہت اعلیٰ)")
            cgpa >= 3.33 -> Pair("B+", "Very Good! (بہت اچھا)")
            cgpa >= 3.00 -> Pair("B", "Good (اچھا)")
            cgpa >= 2.66 -> Pair("B-", "Satisfactory (ٹھیک ہے)")
            cgpa >= 2.33 -> Pair("C+", "Average")
            cgpa >= 2.00 -> Pair("C", "Below Average")
            cgpa >= 1.50 -> Pair("D", "Pass / Probation Hazard")
            else -> Pair("F", "Academic Fail (فیل)")
        }

        return CgpaResult(
            cgpa = cgpa,
            totalCreditHours = totalCredits,
            percentageEquivalent = percentage,
            letterGrade = grade,
            remarks = remarks
        )
    }

    // Merit Calculator Formula: Matric 10% + FSc 40% + Entry Test 50%
    fun calculateMerit(matricPct: Double, fscPct: Double, entryTestPct: Double): Double {
        return (matricPct * 0.10) + (fscPct * 0.40) + (entryTestPct * 0.50)
    }

    // Get typical Pakistani university options based on entry test and merit percentage
    fun getUniversityEligibility(merit: Double, testType: String): List<UniversityEligibility> {
        val list = mutableListOf<UniversityEligibility>()
        
        if (testType == "MDCAT") {
            // Medical Colleges
            list.add(UniversityEligibility("King Edward Medical University (KEMU)", "Lahore, Punjab", "91.5% - 94.5%", getChance(merit, 91.5), "MDCAT"))
            list.add(UniversityEligibility("Allama Iqbal Medical College (AIMC)", "Lahore, Punjab", "90.0% - 91.5%", getChance(merit, 90.0), "MDCAT"))
            list.add(UniversityEligibility("Dow University of Health Sciences (DUHS)", "Karachi, Sindh", "86.5% - 89.0%", getChance(merit, 86.5), "MDCAT"))
            list.add(UniversityEligibility("Kingston / Regional Medical Colleges", "Federal/KPK/Sindh", "81.0% - 86.0%", getChance(merit, 81.0), "MDCAT"))
            list.add(UniversityEligibility("Private Sector Medical/Dental Colleges", "Pakistan White-list", "65.0% - 80.0%", getChance(merit, 65.0), "MDCAT"))
        } else if (testType == "ECAT") {
            // Engineering Colleges
            list.add(UniversityEligibility("NUST (SEECS / SMME)", "Islamabad, Capital", "78.0% - 85.0%", getChance(merit, 78.0), "ECAT / NET"))
            list.add(UniversityEligibility("GIKI (Engineering & IT)", "Topi, KPK", "73.0% - 80.0%", getChance(merit, 73.0), "GIK Custom / ECAT"))
            list.add(UniversityEligibility("UET Lahore (Mechanical / Electrical)", "Lahore, Punjab", "75.0% - 82.0%", getChance(merit, 75.0), "ECAT"))
            list.add(UniversityEligibility("FAST NUCES (Computer Science)", "All Campuses", "72.0% - 79.0%", getChance(merit, 72.0), "FAST Entry Test / ECAT"))
            list.add(UniversityEligibility("COMSATS (Software Engineering / CS)", "Islamabad / Lahore", "68.0% - 75.0%", getChance(merit, 68.0), "NTS NAT / ECAT"))
            list.add(UniversityEligibility("NED Univ of Eng & Technology", "Karachi, Sindh", "70.0% - 78.0%", getChance(merit, 70.0), "NED Test / ECAT"))
        } else {
            // General / NTS NAT Colleges
            list.add(UniversityEligibility("COMSATS University Islamabad", "All Campuses", "68.0% - 75.0%", getChance(merit, 68.0), "NTS NAT"))
            list.add(UniversityEligibility("Punjab University (PU)", "Lahore, Punjab", "65.0% - 73.0%", getChance(merit, 65.0), "PU Custom / NTS"))
            list.add(UniversityEligibility("Quaid-i-Azam University (QAU)", "Islamabad, Capital", "67.0% - 74.0%", getChance(merit, 67.0), "NTS NAT"))
            list.add(UniversityEligibility("Air University (Engineering & Computing)", "Islamabad", "62.0% - 70.0%", getChance(merit, 62.0), "Air Test / NTS"))
            list.add(UniversityEligibility("Bahria University (Business & CS)", "Islamabad / Karachi", "55.0% - 65.0%", getChance(merit, 55.0), "NTS NAT"))
        }

        return list.sortedByDescending { it.typicalMeritRange }
    }

    private fun getChance(userMerit: Double, cutoff: Double): String {
        return when {
            userMerit >= (cutoff + 2.0) -> "High Choice (گرین سگنل)"
            userMerit >= cutoff -> "Good Choice (قوی امکان)"
            userMerit >= (cutoff - 5.0) -> "Moderate Chance (امید رکھیں)"
            else -> "Low Chance / Needs High Entrance Score (مشکل)"
        }
    }
}
