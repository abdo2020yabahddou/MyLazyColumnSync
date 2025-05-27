package com.example.mylazycolumnsync

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun MainView() {

    val lazyListState = rememberLazyListState()
    val viewModel: MainViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Lazy Column Sync",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                backgroundColor = Color.Magenta,
                elevation = 8.dp,
                actions = {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Icon(
                            imageVector = if (viewModel.isAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Sort",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    viewModel.toggleSortOrder()
                                }
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
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
    ) {
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
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )

                Button(
                    onClick = { viewModel.addItem() },
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

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = lazyListState
            ) {
                itemsIndexed(viewModel.itemList) { index, item ->
                    ListItem(
                        value = item,
                        index = index,
                        onDeleteClick = { viewModel.deleteItem(it) }
                    )
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