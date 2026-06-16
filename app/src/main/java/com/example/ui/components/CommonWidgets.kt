package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Common styled card to present the output calculations nicely.
 */
@Composable
fun ResultCard(
    visible: Boolean,
    title: String,
    score: String,
    scoreLabel: String,
    grade: String,
    gradeLabel: String,
    remarks: String,
    isPass: Boolean = true,
    additionalContent: @Composable () -> Unit = {}
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(
                    width = 1.dp,
                    color = if (isPass) Color(0xFFBBDEFB) else Color(0xFFFCA5A5),
                    shape = RoundedCornerShape(24.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (isPass) Color(0xFFE3F2FD) else Color(0xFFFFF1F2) // Clean alert blues/pinks
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat styling per theme
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPass) Color(0xFF1565C0) else Color(0xFF991B1B),
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Score
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = score,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isPass) Color(0xFF0D47A1) else Color(0xFFB91C1C)
                        )
                        Text(
                            text = scoreLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPass) Color(0xFF1565C0) else Color(0xFF991B1B).copy(alpha = 0.8f)
                        )
                    }

                    // Vertical divider
                    Box(
                        modifier = Modifier
                            .height(56.dp)
                            .width(1.dp)
                            .background(if (isPass) Color(0xFFBBDEFB) else Color(0xFFFCA5A5))
                    )

                    // Grade
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = grade,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isPass) Color(0xFF0D47A1) else Color(0xFFB91C1C)
                        )
                        Text(
                            text = gradeLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPass) Color(0xFF1565C0) else Color(0xFF991B1B).copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Status Badge
                Surface(
                    color = if (isPass) Color(0xFF1565C0) else Color(0xFFDC2626),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = if (isPass) "PASS (کامیاب)" else "Needs Improvement (توجہ طلب)",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                    )
                }

                Text(
                    text = remarks,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isPass) Color(0xFF1E293B) else Color(0xFF7F1D1D),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                additionalContent()
            }
        }
    }
}

/**
 * Share result details to WhatsApp (or other social providers)
 */
@Composable
fun ShareResultButton(
    textToShare: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    Button(
        onClick = {
            shareTextToSocial(context, textToShare)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF25D366) // WhatsApp Green
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Share on WhatsApp (شیئر کریں)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// Share text intent fallback dispatcher
fun shareTextToSocial(context: Context, text: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            // Attempt to target WhatsApp directly
            `package` = "com.whatsapp"
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to general share-sheet if WhatsApp isn't installed
        val generalIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(generalIntent, "Share your calculations via"))
    }
}
