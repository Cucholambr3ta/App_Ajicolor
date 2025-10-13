package com.example.apppolera_ecommerce_grupo4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.apppolera_ecommerce_grupo4.ui.theme.AppPolera_ecommerce_Grupo4Theme
import com.example.apppolera_ecommerce_grupo4.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPolera_ecommerce_Grupo4Theme {
                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppPolera_ecommerce_Grupo4Theme {
        AppNavigation()
    }
}