package com.example.mylazycolumnsync

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TopBar() {

    val viewModel: MainViewModel = viewModel()
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "Lazy Column Sync",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        backgroundColor = Color.Magenta,
        elevation = 8.dp,
        actions = {
            Icon(
                imageVector = if (viewModel.isAscending) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = "Sort",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
                    .clickable {
                        viewModel.toggleSortOrder()
                    }
            )
        }
    )

}