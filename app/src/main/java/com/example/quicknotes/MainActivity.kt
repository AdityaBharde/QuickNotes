package com.example.quicknotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quicknotes.data.Note
import com.example.quicknotes.ui.AddEditNoteScreen
import com.example.quicknotes.ui.NoteViewModel
import com.example.quicknotes.ui.NotesScreen
import com.example.quicknotes.ui.theme.QuickNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickNotesTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val noteViewModel: NoteViewModel = viewModel()
    var selectedNote: Note? = null

    NavHost(navController = navController, startDestination = "notes_screen") {
        composable("notes_screen") {
            NotesScreen(
                noteViewModel = noteViewModel,
                onAddNote = {
                    selectedNote = null
                    navController.navigate("add_edit_screen")
                },
                onNoteClick = { note ->
                    selectedNote = note
                    navController.navigate("add_edit_screen")
                }
            )
        }
        composable("add_edit_screen") {
            AddEditNoteScreen(
                note = selectedNote,
                onSave = { note ->
                    if (selectedNote == null) {
                        noteViewModel.addNote(note)
                    } else {
                        noteViewModel.updateNote(note)
                    }
                },
                onNavigateUp = {
                    navController.popBackStack()
                }
            )
        }
    }
}