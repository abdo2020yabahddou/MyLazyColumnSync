package com.example.mylazycolumnsync

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylazycolumnsync.data.WordItem

@Composable
fun MainView() {


    val context = LocalContext.current.applicationContext as Application
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context))
    val lazyListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val isAscending by viewModel.isAscending.collectAsState()
    val wordList by viewModel.sortedWords.collectAsState()

    // Apply sorting based on isAscending
    val sortedList = if (isAscending) {
        wordList.sortedBy {item: WordItem -> item.word.lowercase() }
    } else {
        wordList.sortedByDescending {item: WordItem -> item.word.lowercase() }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, end = 20.dp, start = 20.dp),
        topBar = {
            TopBar(viewModel= viewModel)
            Spacer(modifier = Modifier.height(16.dp))
        },
        bottomBar = { BottomBar() }
    ) { it ->
        Column(modifier = Modifier.padding(it)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFF6200EE)
                        )
                    },
                    value = viewModel.itemValue,
                    onValueChange = {
                        //if the added value is in the list, don't add it
                        viewModel.onValueChange(it)
                    },
                    trailingIcon = {
                        if (viewModel.isError) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = Color.Red
                            )
                        }
                    },
                    isError = viewModel.isError,
                    supportingText = {
                        if (viewModel.isError) {
                            Text(
                                text = viewModel.errorMessage,
                                color = Color.Red
                            )
                        }
                    },
                    label = { Text("New Word") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp), // Match button height
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        viewModel.addItem()
                    })
                )
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.addItem()
                    },
                    modifier = Modifier
                        .height(56.dp) // Match text field height
                        .defaultMinSize(minWidth = 56.dp), // Square shape when no text
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color(0xFF6200EE)
                            )
                        }
                        Text(text = "Add")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp),
                state = lazyListState
            ) {
                    items(sortedList) { item ->
                        ListItem(
                            value = item.word,
                            onDeleteClick = { viewModel.deleteItem(item.word) }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView()
}