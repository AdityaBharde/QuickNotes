# QuickNotes

## Description
QuickNotes is a simple and efficient note-taking application for Android, designed for users who need to quickly capture their thoughts and ideas. The app provides a clean and intuitive interface for creating, editing, and managing notes. It's built with modern Android development practices, using Jetpack Compose for the UI and Room for local data persistence.

---

## Key Features
* **Create and Manage Notes:** Easily add new notes, edit existing ones, and delete notes you no longer need.
* **Search Functionality:** Quickly find specific notes by searching for keywords in the title or content.
* **Sort Notes:** Organize your notes by sorting them based on the newest first or by title in alphabetical order.
* **Data Persistence:** Your notes are saved locally on your device using the Room database, ensuring that your data is always available.
* **Undo Deletion:** A snackbar with an "Undo" action appears after deleting a note, allowing you to easily recover it.

---

## Technologies Used
* **Kotlin:** The primary programming language for building the application.
* **Jetpack Compose:** For building the entire user interface of the app.
* **Room:** For creating and managing the local database to store all the notes.
* **ViewModel:** To manage and store UI-related data in a lifecycle-conscious way.
* **Navigation Compose:** To handle the navigation between the different screens of the app.
* **Material Design 3:** The app follows the latest Material Design guidelines for a modern and intuitive user experience.

---

## Setup Instructions
To set up and run this project locally, you will need to do the following:

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/adityabharde/quicknotes.git](https://github.com/adityabharde/quicknotes.git)
    ```
2.  **Open in Android Studio:** Open the cloned project in Android Studio.
3.  **Build and run the app:** Build and run the application on an Android emulator or a physical device. The project is self-contained and does not require any external API keys or special setup.
