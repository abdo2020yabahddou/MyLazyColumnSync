package com.example.mylazycolumnsync.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylazycolumnsync.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun BottomBar() {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val viewModel: MainViewModel = viewModel()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .padding(bottom = 36.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {
            scope.launch { lazyListState.animateScrollToItem(0) }
        }) {
            Text("Scroll to Top")
        }
        Button(onClick = {
            scope.launch { lazyListState.animateScrollToItem(viewModel.itemList.lastIndex) }
        }) {
            Text("Scroll to Bottom")
        }
    }
}