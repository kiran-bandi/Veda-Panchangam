package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AutoResizedText
import com.example.ui.theme.AuspiciousGreen
import com.example.ui.theme.InauspiciousRed

data class TableColumn(
    val title: String,
    val weight: Float = 1f,
    val alignment: Alignment.Horizontal = Alignment.Start
)

data class KeyValueRowData(
    val indexLabel: String? = null,
    val label: String,
    val value: String,
    val subtext: String? = null,
    val isHighlighted: Boolean = false,
    val statusBadge: String? = null,
    val isAuspiciousBadge: Boolean? = null
)

@Composable
fun ReusableTableContainer(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    headerRightContent: @Composable (() -> Unit)? = null,
    headerContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
    testTag: String = "reusable_table",
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (title != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = headerContainerColor,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            if (subtitle != null) {
                                Text(
                                    text = subtitle,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (headerRightContent != null) {
                            headerRightContent()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            content()
        }
    }
}

@Composable
fun KeyValueTable(
    modifier: Modifier = Modifier,
    rows: List<KeyValueRowData>,
    title: String? = null,
    subtitle: String? = null,
    headerRightContent: @Composable (() -> Unit)? = null,
    headerContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
    testTag: String = "key_value_table"
) {
    ReusableTableContainer(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        headerRightContent = headerRightContent,
        headerContainerColor = headerContainerColor,
        testTag = testTag
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
        ) {
            rows.forEachIndexed { idx, row ->
                val isEven = idx % 2 == 0
                val bgColor = when {
                    row.isHighlighted -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    row.isAuspiciousBadge == true -> AuspiciousGreen.copy(alpha = 0.1f)
                    row.isAuspiciousBadge == false -> InauspiciousRed.copy(alpha = 0.1f)
                    isEven -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    else -> MaterialTheme.colorScheme.surface
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (row.indexLabel != null) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = row.indexLabel,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    AutoResizedText(
                        text = row.label,
                        fontSize = 12.sp,
                        minFontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(0.95f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = row.value,
                                fontSize = 12.sp,
                                fontWeight = if (row.isHighlighted) FontWeight.Bold else FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            if (row.statusBadge != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                val badgeColor = when (row.isAuspiciousBadge) {
                                    true -> AuspiciousGreen
                                    false -> InauspiciousRed
                                    else -> MaterialTheme.colorScheme.tertiary
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = badgeColor.copy(alpha = 0.18f),
                                    border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = row.statusBadge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = badgeColor,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        if (row.subtext != null) {
                            Text(
                                text = row.subtext,
                                fontSize = 10.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (idx < rows.size - 1) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun GridDataTable(
    modifier: Modifier = Modifier,
    columns: List<TableColumn>,
    title: String? = null,
    subtitle: String? = null,
    headerRightContent: @Composable (() -> Unit)? = null,
    headerContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
    testTag: String = "grid_data_table",
    rowsCount: Int,
    rowContent: @Composable (rowIndex: Int, isEven: Boolean) -> Unit
) {
    ReusableTableContainer(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        headerRightContent = headerRightContent,
        headerContainerColor = headerContainerColor,
        testTag = testTag
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f))
                    .padding(horizontal = 10.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                columns.forEach { col ->
                    Text(
                        text = col.title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.weight(col.weight),
                        textAlign = when (col.alignment) {
                            Alignment.CenterHorizontally -> TextAlign.Center
                            Alignment.End -> TextAlign.End
                            else -> TextAlign.Start
                        }
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), thickness = 1.5.dp)

            // Body Rows
            for (i in 0 until rowsCount) {
                val isEven = i % 2 == 0
                rowContent(i, isEven)
                if (i < rowsCount - 1) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), thickness = 1.dp)
                }
            }
        }
    }
}
