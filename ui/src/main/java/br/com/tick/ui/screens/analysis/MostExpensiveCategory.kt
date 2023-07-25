package br.com.tick.ui.screens.analysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.R
import br.com.tick.ui.core.TeiraNoAvailableDataState
import br.com.tick.ui.screens.analysis.models.MostExpensiveCategory
import br.com.tick.ui.screens.analysis.states.MostExpensiveCategoriesStates
import br.com.tick.ui.screens.analysis.viewmodels.AnalysisScreenViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun MostExpensiveCategory(
    modifier: Modifier = Modifier,
    viewModel: AnalysisScreenViewModel = hiltViewModel()
) {
    val mostExpensiveCategoriesStates by viewModel.mostExpenseCategoryList
        .collectAsState(initial = MostExpensiveCategoriesStates.NoDataAvailable)

    Column(modifier = modifier.fillMaxWidth()) {
        when (val state = mostExpensiveCategoriesStates) {
            is MostExpensiveCategoriesStates.Full -> MostExpensiveCategoryBody(modifier, state)
            MostExpensiveCategoriesStates.NoDataAvailable -> TeiraNoAvailableDataState(modifier)
        }
    }
}

@Composable
private fun MostExpensiveCategoryBody(
    modifier: Modifier = Modifier,
    mostExpensiveCategoriesState: MostExpensiveCategoriesStates.Full
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.analysis_most_expensive_categories_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        Row(
            modifier = modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            mostExpensiveCategoriesState.mostExpensiveCategories.forEach { mostExpensiveCategory ->
                val categoryCardColor = mostExpensiveCategory.color?.let {
                    Color(it)
                } ?: MaterialTheme.colorScheme.secondary

                CategoryCard(
                    label = mostExpensiveCategory.categoryName,
                    subLabel = mostExpensiveCategory.amount.toString(),
                    color = categoryCardColor
                )
            }
        }
    }
}

@Composable
fun CategoryCard(modifier: Modifier = Modifier, label: String, subLabel: String, color: Color) {
    Card(
        modifier = modifier
            .size(80.dp)
            .padding(MaterialTheme.spacing.smallest),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.textStyle.h2bold,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subLabel,
                style = MaterialTheme.textStyle.h3small,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Preview
@Composable
fun MostExpensiveCategoryBodyPreview() {
    MostExpensiveCategoryBody(
        mostExpensiveCategoriesState = MostExpensiveCategoriesStates.Full(
            listOf(MostExpensiveCategory("Test", Color.Red.toArgb(), 56.0))
        )
    )
}
