package com.example.quicknotes.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quicknotes.data.Note
import kotlinx.coroutines.launch

private enum class SortOrder { NEWEST, TITLE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    noteViewModel: NoteViewModel = viewModel(),
    onAddNote: () -> Unit,
    onNoteClick: (Note) -> Unit
) {
    val notes by noteViewModel.notes.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var sortOrder by rememberSaveable { mutableStateOf(SortOrder.NEWEST) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredNotes by remember(notes, searchQuery, sortOrder) {
        mutableStateOf(
            notes
                .filter { n ->
                    if (searchQuery.isBlank()) true
                    else n.title.contains(searchQuery, ignoreCase = true) ||
                            n.content.contains(searchQuery, ignoreCase = true)
                }
                .let { list ->
                    when (sortOrder) {
                        SortOrder.NEWEST -> list.sortedByDescending { it.id }
                        SortOrder.TITLE -> list.sortedBy { it.title.lowercase() }
                    }
                }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuickNotes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    TextButton(onClick = {
                        sortOrder = if (sortOrder == SortOrder.NEWEST) SortOrder.TITLE else SortOrder.NEWEST
                    }) {
                        Text(text = if (sortOrder == SortOrder.NEWEST) "Sort: New" else "Sort: Title")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search notes") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(filteredNotes, key = { it.id }) { note ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        NoteItem(
                            note = note,
                            onNoteClick = { onNoteClick(note) },
                            onDeleteClick = {
                                scope.launch {
                                    noteViewModel.deleteNote(note)
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Note deleted",
                                        actionLabel = "Undo",
                                        withDismissAction = true
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        noteViewModel.addNote(note)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onNoteClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = note.content, maxLines = 2)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Note")
            }
        }
    }
}