package com.example.exchangeratetracker.presentation.navigation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.example.exchangeratetracker.presentation.navigation.BottomNavItem

@Composable
fun RowScope.NavigationBarItemStyled(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val animatedColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "nav-item-color"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (selected) 1.15f else 1f,
        label = "nav-item-scale"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = animatedColor,
            modifier = Modifier.scale(animatedScale)
        )
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            color = animatedColor
        )
    }
}