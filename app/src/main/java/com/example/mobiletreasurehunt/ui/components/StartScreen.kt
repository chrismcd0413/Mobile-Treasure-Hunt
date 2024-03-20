/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.mobiletreasurehunt.R
import com.example.mobiletreasurehunt.data.LocalDataProvider.allRules
import com.example.mobiletreasurehunt.data.Rule
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun StartPage(
    onStartButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    val isScrollingUp = remember { mutableStateOf(true) }
    var previousFirstVisibleItemIndex by remember { mutableStateOf(0) }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { newIndex ->
                isScrollingUp.value = newIndex <= previousFirstVisibleItemIndex
                previousFirstVisibleItemIndex = newIndex
            }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFAB(
                onClick = onStartButtonClicked,
                expanded = isScrollingUp.value
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                lineHeight = 1.25.em,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = stringResource(R.string.rules_header),
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 1.27.em,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
            RulesList(
                modifier = Modifier,
                state = scrollState
            )
        }
    }
}

@Composable
fun RulesList(state: LazyListState, modifier: Modifier = Modifier) {
    val rules = allRules
    LazyColumn(
        state = state,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
    ) { items(rules) { rule ->
            Rule(rule)
        }
    }
}

@Composable
fun Rule(
    rule: Rule,
    modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 24.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .requiredHeight(height = 64.dp)
            ) {
                BuildingBlocksMonogram(rule.id)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(rule.header),
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 1.27.em,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically))
                Text(
                    text = stringResource(rule.rule),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 1.5.em,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth())
            }
        }
    }
}

@Composable
fun BuildingBlocksMonogram(
    number: Int,
    modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredSize(size = 40.dp)
            .clip(shape = RoundedCornerShape(100.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = stringResource(number),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            lineHeight = 1.5.em,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp),
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .offset(
                    x = 0.dp,
                    y = 0.dp
                )
                .requiredSize(size = 40.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
    }
}

@Composable
private fun ExtendedFAB(
    onClick: () -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        expanded = expanded,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        icon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, "Start the game.") },
        text = { Text(stringResource(R.string.action_button_text))}
    )
}


@Preview()
@Composable
private fun StartPagePreview() {
    // StartPage(Modifier)
}
