package br.com.tick.teira.ui.screens.wallet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.ui.screens.wallet.states.MonthlyIncomeStates
import br.com.tick.teira.ui.screens.wallet.viewmodels.ConfigurationScreenViewModel

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
    modifier: Modifier = Modifier,
    viewModel: ConfigurationScreenViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            val incomeValue by remember(viewModel) { viewModel.monthlyIncomeFlow }.collectAsState(MonthlyIncomeStates.Loading)
            var monthlyIncomeTextFieldValue by remember(incomeValue) { mutableStateOf(incomeValue.value.toString()) }

            Text(
                modifier = Modifier.align(CenterHorizontally),
                text = "Valor do ordenado mensal",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                modifier = Modifier.align(CenterHorizontally),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = monthlyIncomeTextFieldValue,
                onValueChange = {
                    monthlyIncomeTextFieldValue = it
                    viewModel.saveMonthlyIncome(it.toDouble())
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.width(200.dp).align(CenterHorizontally),
                text = "O valor será salvo ao digitar. Avisaremos se algo der errado :)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }
    }
}