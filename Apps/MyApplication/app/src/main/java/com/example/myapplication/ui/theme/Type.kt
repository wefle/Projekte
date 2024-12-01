package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

// Set of Material typography styles to start with
val fonts = FontFamily(
    Font(R.font.kadwa_regular),
    Font(R.font.kadwa_bold, weight = FontWeight.Bold)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    ),
    /*
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)