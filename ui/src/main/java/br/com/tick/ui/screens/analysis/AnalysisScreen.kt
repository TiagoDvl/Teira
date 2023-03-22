package br.com.tick.ui.screens.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.tick.ui.theme.spacing

@Composable
fun AnalysisScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.small),
    ) {
        ExpenseDateRelation(modifier = Modifier.fillMaxWidth().weight(0.5f))
        CategoryRank(modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.large))
        FinancialHealthComposable(modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.large))
    }
}
