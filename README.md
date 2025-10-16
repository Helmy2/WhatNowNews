# What Now News

What Now News is a sample news application for Android, built with modern technologies and a clean architecture.

## Features

*   **Authentication:** Users can create an account, log in, and reset their password.
*   **News Feed:** A continuously updated list of news articles.
*   **Article Details:** View the full content of any news article.
*   **Favorites:** Save articles to read later.
*   **Settings:** Customize the app experience.

## Technologies Used

*   **Kotlin:** The primary programming language for the application.
*   **Coroutines:** For managing background threads and asynchronous operations.
*   **Flow:** A reactive stream library from Kotlin for data streams.
*   **Android Jetpack:**
    *   **ViewModel:** To store and manage UI-related data in a lifecycle-conscious way.
    *   **LiveData:** To build data objects that notify views when the underlying data changes.
    *   **Navigation Component:** To handle in-app navigation.
    *   **Room:** For local persistence of data.
    *   **DataStore:** For storing key-value pairs.
*   **Retrofit:** A type-safe HTTP client for Android and Java.
*   **Koin:** A pragmatic lightweight dependency injection framework for Kotlin.
*   **Coil & Glide:** Image loading libraries for Android.
*   **Firebase:**
    *   **Firebase Authentication:** For handling user authentication.
    *   **Cloud Firestore:** A NoSQL document database for storing app data.

## Architecture

The app follows the principles of Clean Architecture, with a layered approach that separates concerns and promotes testability and maintainability. The main layers are:

*   **Presentation:** The UI layer, which includes Activities, Fragments, and ViewModels.
*   **Domain:** The business logic of the application, containing Use Cases and pure Kotlin models.
*   **Data:** The layer responsible for providing data to the application, from either a remote (network) or local (database) source.

## Getting Started

To build and run the project, you will need to:

1.  Clone the repository.
2.  Create a `local.properties` file in the root of the project.
3.  Add your News API key to the `local.properties` file with the key `API_KEY`.
4.  Set up a Firebase project and add your `google-services.json` file to the `app` module.
5.  Build and run the app.
