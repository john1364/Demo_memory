package ru.domclick.mortgage.debug

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.domclick.mortgage.debug.ui.search.SearchActivity
import ru.domclick.mortgage.debug.ui.theme.MyApplicationTheme

var MAP_SIZE = 0

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("Главный экран приложения")
                        Button(
                            onClick = {
                                this@MainActivity.startActivity(
                                    Intent(this@MainActivity, SearchActivity::class.java)
                                )
                            }) {
                            Text("Карта")
                        }
                    }
                }
            }
        }
    }
}