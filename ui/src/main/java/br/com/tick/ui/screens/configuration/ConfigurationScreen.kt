package br.com.tick.ui.screens.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.screens.configuration.states.MonthlyIncomeStates
import br.com.tick.ui.screens.configuration.viewmodels.ConfigurationScreenViewModel
import br.com.tick.ui.theme.spacing

@Composable
fun ConfigurationScreen(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.extraSmall),
            verticalArrangement = Arrangement.Center
        ) {
            SettingField()
        }
    }
}

@Composable
fun SettingField(
    modifier: Modifier = Modifier,
    viewModel: ConfigurationScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.extraSmall),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val incomeValue by remember { viewModel.monthlyIncomeFlow }.collectAsState(MonthlyIncomeStates.Loading)

        Text(
            text = "Valor do ordenado mensal",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        TextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = incomeValue.value.toString(),
            onValueChange = {
                viewModel.saveMonthlyIncome(it.toDouble())
            }
        )
        Text(
            text = "O valor será salvo ao digitar. Avisaremos se algo der errado :)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}
