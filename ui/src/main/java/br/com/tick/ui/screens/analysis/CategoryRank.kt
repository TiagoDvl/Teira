package br.com.tick.ui.screens.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.R
import br.com.tick.ui.screens.analysis.models.MostExpensiveCategory
import br.com.tick.ui.screens.analysis.states.MostExpensiveCategoriesStates
import br.com.tick.ui.screens.analysis.viewmodels.AnalysisScreenViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun CategoryRank(
    modifier: Modifier = Modifier,
    viewModel: AnalysisScreenViewModel = hiltViewModel()
) {
    val mostExpensiveCategoriesStates by remember {
        viewModel.mostExpenseCategoryList
    }.collectAsState(initial = MostExpensiveCategoriesStates.Loading)

    when (val state = mostExpensiveCategoriesStates) {
        is MostExpensiveCategoriesStates.Full -> MostExpensiveCategoryBody(modifier, state)
        MostExpensiveCategoriesStates.Loading -> {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(id = R.string.generic_loading),
                style = MaterialTheme.textStyle.h4
            )
        }
    }
}

@Composable
fun MostExpensiveCategoryBody(
    modifier: Modifier = Modifier,
    mostExpensiveCategoriesState: MostExpensiveCategoriesStates.Full
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.analysis_most_expensive_category_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.spacing.medium))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            mostExpensiveCategoriesState.mostExpensiveCategories.forEach {
                val formattedCategoryNameLength = if (it.categoryName.length < 6) it.categoryName.length else 6
                CategoryCircle(
                    label = it.categoryName.substring(0, formattedCategoryNameLength),
                    subLabel = it.amount.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

}

@Composable
fun CategoryCircle(modifier: Modifier = Modifier, label: String, subLabel: String, color: Color) {
    Box(
        modifier = modifier.size(70.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawCircle(color = color)
            }
        )
        Column(
            modifier = modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.textStyle.h2bold,
                color = MaterialTheme.colorScheme.onSecondary
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
            listOf(MostExpensiveCategory("Test", Color.Red, 56.0))
        )
    )
}
