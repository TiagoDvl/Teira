package br.com.tick.teira

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.tick.ui.theme.TeiraTheme
import br.com.tick.ui.TeiraScaffold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeiraTheme {
                TeiraScaffold()
            }
        }
    }
}
