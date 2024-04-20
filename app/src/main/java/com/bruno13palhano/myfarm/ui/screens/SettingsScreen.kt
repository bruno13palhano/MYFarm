package com.bruno13palhano.myfarm.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bruno13palhano.myfarm.R

@Composable
fun SettingsRoute() { SettingsScreen() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.settings_label)) })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {

        }
    }
}