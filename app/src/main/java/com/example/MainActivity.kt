package com.example

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.ui.FinanceTrackerApp
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.FinanceViewModel

class MainActivity : FragmentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    val viewModel = ViewModelProvider(this)[FinanceViewModel::class.java]

    setContent {
      MyApplicationTheme {
        FinanceTrackerApp(viewModel = viewModel)
      }
    }
  }
}
