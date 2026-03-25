package com.opencontacts.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val themePresets = listOf("CLASSIC", "GLASS", "AMOLED", "SOFT", "MINIMAL", "ELEGANT", "PASTEL")
private val accentPalettes = listOf("BLUE", "EMERALD", "SUNSET", "LAVENDER", "ROSE", "AMBER", "SLATE")

@Composable
fun AppearanceRoute(
    onBack: () -> Unit,
    appViewModel: AppViewModel,
) {
    val settings by appViewModel.appLockSettings.collectAsStateWithLifecycle()
    SettingsScaffold(title = "Appearance", onBack = onBack) { modifier ->
        LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                SettingsSection(title = "Theme mode", subtitle = "Choose when OpenContacts uses a light or dark foundation.") {
                    SettingsChoiceRow(
                        title = "Mode",
                        subtitle = "Apply instantly without restarting the app.",
                        selected = settings.themeMode,
                        choices = listOf("SYSTEM", "LIGHT", "DARK"),
                        onSelect = appViewModel::setThemeMode,
                    )
                }
            }
            item {
                SettingsSection(title = "Theme preset", subtitle = "Modern presets inspired by polished Android apps.") {
                    ThemePresetPicker(
                        selected = settings.themePreset,
                        onSelect = appViewModel::setThemePreset,
                    )
                    SettingsSpacer()
                    AccentPalettePicker(
                        selected = settings.accentPalette,
                        onSelect = appViewModel::setAccentPalette,
                    )
                    SettingsSpacer()
                    SettingsChoiceRow(
                        title = "Corner style",
                        subtitle = "Adjust card and container roundness for a softer or sharper look.",
                        selected = settings.cornerStyle,
                        choices = listOf("ROUNDED", "COMPACT", "SHARP"),
                        onSelect = appViewModel::setCornerStyle,
                    )
                }
            }
            item {
                SettingsSection(title = "Language", subtitle = "Switch the app language independently from the device when needed.") {
                    SettingsChoiceRow(
                        title = "App language",
                        subtitle = "Arabic uses real translated strings and RTL layout when available.",
                        selected = settings.appLanguage,
                        choices = listOf("SYSTEM", "EN", "AR"),
                        onSelect = appViewModel::setAppLanguage,
                    )
                }
            }
            item {
                SettingsSection(title = "Live preview", subtitle = "Preview how cards, accents, and surfaces will feel across the app.") {
                    ThemePreviewCard(
                        title = settings.themePreset,
                        subtitle = "${settings.accentPalette} • ${settings.cornerStyle}",
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemePresetPicker(selected: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Preset", style = MaterialTheme.typography.titleMedium)
        Text(
            "Each preset changes the mood of the app while keeping contacts, call cards, and settings readable.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.height(280.dp),
            userScrollEnabled = false,
        ) {
            items(themePresets.size) { index ->
                val preset = themePresets[index]
                ThemePresetCard(
                    label = preset,
                    selected = selected.equals(preset, ignoreCase = true),
                    onClick = { onSelect(preset) },
                )
            }
        }
    }
}

@Composable
private fun ThemePresetCard(label: String, selected: Boolean, onClick: () -> Unit) {
    val gradient = when (label) {
        "GLASS" -> listOf(Color(0xFFDBEAFE), Color(0xFFF8FAFC))
        "AMOLED" -> listOf(Color(0xFF020617), Color(0xFF111827))
        "SOFT" -> listOf(Color(0xFFFFF1F2), Color(0xFFFDF2F8))
        "MINIMAL" -> listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0))
        "ELEGANT" -> listOf(Color(0xFF111827), Color(0xFF334155))
        "PASTEL" -> listOf(Color(0xFFEDE9FE), Color(0xFFDCFCE7))
        else -> listOf(Color(0xFFE0E7FF), Color(0xFFDBEAFE))
    }
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradient))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(label, style = MaterialTheme.typography.titleMedium, color = if (label == "AMOLED" || label == "ELEGANT") Color.White else Color(0xFF0F172A))
                if (selected) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(6.dp).size(14.dp))
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth().height(36.dp).background(Color.White.copy(alpha = 0.35f), MaterialTheme.shapes.medium))
                Box(modifier = Modifier.fillMaxWidth().height(18.dp).background(Color.Black.copy(alpha = 0.12f), MaterialTheme.shapes.medium))
            }
        }
    }
}

@Composable
private fun AccentPalettePicker(selected: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Accent palette", style = MaterialTheme.typography.titleMedium)
        Text(
            "Circular selectors keep the palette chooser lightweight, similar to modern note and organization apps.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        ) {
            accentPalettes.forEach { palette ->
                val color = when (palette) {
                    "EMERALD" -> Color(0xFF10B981)
                    "SUNSET" -> Color(0xFFF97316)
                    "LAVENDER" -> Color(0xFF8B5CF6)
                    "ROSE" -> Color(0xFFE11D48)
                    "AMBER" -> Color(0xFFF59E0B)
                    "SLATE" -> Color(0xFF475569)
                    else -> Color(0xFF2563EB)
                }
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(color, CircleShape)
                        .border(if (selected.equals(palette, true)) 3.dp else 1.dp, if (selected.equals(palette, true)) MaterialTheme.colorScheme.onSurface else color.copy(alpha = 0.35f), CircleShape)
                        .clickable { onSelect(palette) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected.equals(palette, true)) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemePreviewCard(title: String, subtitle: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(108.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer,
                            ),
                        ),
                        MaterialTheme.shapes.large,
                    ),
            )
        }
    }
}
