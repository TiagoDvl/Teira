package br.com.tick.teira.ui.screens.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.ui.screens.analysis.states.MostExpensiveCategoriesStates
import br.com.tick.teira.ui.screens.analysis.viewmodels.AnalysisScreenViewModel

@Composable
fun CategoryRank(modifier: Modifier = Modifier, viewModel: AnalysisScreenViewModel = hiltViewModel()) {
    val mostExpensiveCategoriesStates by remember {
        viewModel.mostExpenseCategoryList
    }.collectAsState(initial = MostExpensiveCategoriesStates.Loading)

    when (mostExpensiveCategoriesStates) {
        is MostExpensiveCategoriesStates.Full -> {
            MostExpensiveCategoryBody(mostExpensiveCategoriesStates as MostExpensiveCategoriesStates.Full, modifier)
        }
        MostExpensiveCategoriesStates.Loading -> {
            Text(text = "Loading...")
        }
    }
}

@Composable
fun MostExpensiveCategoryBody(
    mostExpensiveCategoriesState: MostExpensiveCategoriesStates.Full,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        mostExpensiveCategoriesState.mostExpensiveCategories.forEach {
            Column {
                CategoryCircle(
                    it.categoryName.substring(0, 3),
                    it.color
                )
                Text(text = String.format("%.2f", it.amount))
            }

        }
    }
}

@Composable
fun CategoryCircle(firstExpenseLetter: String, color: Color) {
    Box {
        Canvas(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center),
            onDraw = {
                drawCircle(color = color)
            }
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = firstExpenseLetter
        )
    }
}
