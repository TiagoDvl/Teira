package br.com.tick.ui.core

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.R
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.screens.wallet.viewmodels.ExpensesGridViewModel
import br.com.tick.ui.theme.Pink40
import br.com.tick.ui.theme.PurpleGrey80
import br.com.tick.ui.theme.spacing

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

// https://fvilarino.medium.com/creating-a-rotating-card-in-jetpack-compose-ba94c7dd76fb
@ExperimentalFoundationApi
@Composable
fun FlipCard(
    expenseCard: ExpenseCard,
    modifier: Modifier = Modifier
) {
    val cardFace = remember { mutableStateOf(CardFace.Front) }

    val rotation = animateFloatAsState(
        targetValue = cardFace.value.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )

    Card(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            }
            .combinedClickable(
                onClick = {},
                onLongClick = { cardFace.value = cardFace.value.next }
            ),
    ) {
        if (rotation.value <= 90f) {
            FlipCardFront(expenseCard)
        } else {
            FlipCardBack(expenseCard = expenseCard) {
                cardFace.value = cardFace.value.next
            }
        }
    }
}

@Composable
fun FlipCardFront(expenseCard: ExpenseCard) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey80)
            .height(120.dp)
            .padding(MaterialTheme.spacing.medium)
    ) {
        Column {
            Text(text = expenseCard.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = expenseCard.risk.name, style = MaterialTheme.typography.bodySmall)
        }
        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(MaterialTheme.spacing.extraLarge),
            painter = painterResource(id = R.drawable.ic_flip),
            contentDescription = "Flip a card"
        )
        Text(
            modifier = Modifier.align(Alignment.BottomEnd),
            text = "€${expenseCard.value}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun FlipCardBack(
    expenseCard: ExpenseCard,
    expensesGridViewModel: ExpensesGridViewModel = hiltViewModel(),
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Pink40)
                .height(120.dp)
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FilledTonalButton(
                onClick = {

                }
            ) {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit expense"
                )
            }
            FilledTonalButton(
                onClick = {
                    onDelete()
                    expensesGridViewModel.removeCard(expenseCard.id)
                }
            ) {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete expense"
                )
            }
        }
    }
}
