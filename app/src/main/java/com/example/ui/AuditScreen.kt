package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DatasetAuditStatus
import com.example.model.DatasetCoverageStatus
import com.example.viewmodel.PanchangaViewModel

@Composable
fun AuditScreen(
    viewModel: PanchangaViewModel,
    modifier: Modifier = Modifier
) {
    val auditStatus by viewModel.datasetAuditStatus.collectAsState()
    val activeLocation by viewModel.selectedLocation.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("audit_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "పంచాంగ డేటా ధృవీకరణ & ఆడిట్",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "ప్రామాణిక వైదిక దృక్ గణిత ఆస్ట్రోనామికల్ సమాచారం",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Status Banner
        item {
            val statusColor = when (auditStatus.coverageStatus) {
                DatasetCoverageStatus.COMPLETE_365_DAYS -> Color(0xFF2E7D32)
                DatasetCoverageStatus.PARTIAL_DATA_REQUIRED -> Color(0xFFE65100)
                DatasetCoverageStatus.UNAVAILABLE -> MaterialTheme.colorScheme.error
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, statusColor, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (auditStatus.coverageStatus == DatasetCoverageStatus.COMPLETE_365_DAYS) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = statusColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = auditStatus.coverageStatus.labelTe,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = statusColor
                            )
                        }
                        Surface(
                            color = statusColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "2026 నమోదులు: ${auditStatus.totalDatesLoaded}/${auditStatus.totalDatesExpected}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = statusColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = statusColor.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "• లభ్యమైన పరిధి: 2026-03-22 నుండి 2026-12-31 వరకు (రాష్ట్రీయ పంచాంగ్ 1948 శక సంపుటి)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• అవసరమైన పరిధి: 2026-01-01 నుండి 2026-03-21 వరకు (1947 శక సంపుటి డేటా కోరబడుతోంది)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD84315),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• అవసరమైన దస్త్రం: ${auditStatus.requiredFileName ?: "rashtriya_panchang_1947_saka.json"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Provenance & Source Information Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "మూల గ్రంథ వివరాలు (Provenance & Source)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    AuditDetailRow("గణన పద్ధతి:", "వైదిక దృక్ గణిత పద్ధతి (Drik Ganita)")
                    AuditDetailRow("వేధశాల స్థానం:", "IST ప్రామాణిక రేఖాంశం (82°30'E, 23°11'N)")
                    AuditDetailRow("అయనాంశం:", "చిత్రపక్ష (లాహిరి) అయనాంశం")
                    AuditDetailRow("స్థానిక సవరణ విధానం:", "ఎంచుకున్న నగరం (${activeLocation.name}) అక్షాంశ, రేఖాంశాల ప్రకారం సూర్యోదయ సవరణలు")
                }
            }
        }

        // Calculation Comparison Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "గణన వ్యత్యాసాల వివరణ (Methodology Audit)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. కేంద్ర ప్రభుత్వ వేధశాల సమాచారం (82°30'E) కు మరియు స్థానిక నగర సూర్యోదయానికి (${activeLocation.name} - ${activeLocation.longitude}°E) వ్యత్యాసం ఉంటుంది.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "2. తిథి, నక్షత్ర, యోగ ముగింపు సమయాలు స్థానిక సూర్యోదయ నిమిషాలను బట్టి సమాంతర సవరణ పొందుతాయి.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "3. రాష్ట్రీయ పంచాంగ్ పద్ధతి మరియు లాహిరి (చిత్రపక్ష) దృక్ గణిత పద్ధతికి సంబంధించిన అయనాంశమును వేర్వేరుగా పరిరక్షించడమైనది.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun AuditDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.6f)
        )
    }
}
