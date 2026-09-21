package com.example.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Responsive AutoResizedText that automatically adjusts font size down
 * if the text overflows its bounds or exceeds maxLines on smaller device screens,
 * preventing truncation ("...") across all devices.
 */
@Composable
fun AutoResizedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 12.sp,
    minFontSize: TextUnit = 7.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = false,
    maxLines: Int = 1,
    style: TextStyle = TextStyle.Default
) {
    var resizedFontSize by remember(text, fontSize) { mutableStateOf(fontSize) }

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = resizedFontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        style = style,
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            if (textLayoutResult.hasVisualOverflow && resizedFontSize > minFontSize) {
                resizedFontSize = (resizedFontSize.value - 0.5f).sp
            }
        }
    )
}
