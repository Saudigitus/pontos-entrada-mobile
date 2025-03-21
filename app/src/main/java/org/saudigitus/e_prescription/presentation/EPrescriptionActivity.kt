package org.saudigitus.e_prescription.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import org.saudigitus.e_prescription.presentation.theme.EPrescriptionTheme

class EPrescriptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EPrescriptionTheme {
                Scaffold { innerPadding ->
                    Column(Modifier.fillMaxSize().padding(innerPadding)) {

                    }
                }
            }
        }
    }
}
