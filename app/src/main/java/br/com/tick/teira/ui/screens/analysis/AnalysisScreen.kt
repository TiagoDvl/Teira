package br.com.tick.teira.ui.screens.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnalysisScreen() {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ExpenseDateRelation(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.6f)
        )
        CategoryRank(modifier = Modifier.weight(0.7f))
        FinancialHealthComposable(modifier = Modifier.weight(0.7f))
    }
}
