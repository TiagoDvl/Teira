package br.com.tick.teira.ui.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ConfigurationScreen(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier.padding(4.dp)
        ) {
            Row(
                modifier = modifier.fillMaxHeight()
            ) {
                SettingField(
                    modifier = modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun SettingField(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .background(Color.Yellow))
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            var incomeValue by remember { mutableStateOf(0.0) }
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Valor do ordenado mensal"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box (
                modifier = Modifier.fillMaxWidth()
            ){
                TextField(
                    modifier = Modifier.align(Alignment.CenterStart),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = incomeValue.toString(),
                    onValueChange = { incomeValue = it.toDouble() }
                )
                Button(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "OK")
                }
            }

        }
    }
}