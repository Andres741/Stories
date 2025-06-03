# Stories - A Multiplatform Storytelling App

**Stories** is a modern, multiplatform application designed to allow users to browse and read stories. Built using Kotlin Multiplatform Mobile (KMM), it leverages the power of shared Kotlin code to target Android and iOS. This project demonstrates best practices in multiplatform development, including modularization, clean architecture, dependency injection, and remote and local data handling.

## App Demo

[This video](https://drive.google.com/file/d/1ePAMIZB90lRYn7lkd_y7LdCbpPjguzKi/view?usp=sharing) demonstrates the Stories app in action.

[This other video](https://drive.google.com/file/d/1LRCQUvSIrAMAjS9v6GBOMqdMf5yRT9WI/view?usp=sharing) shows the new hero animation implemented.

## Features

*   **Browse Stories:** Users can browse a list of stories.
*   **Read Stories:** Users can select a story to read its full content.
*   **Cross-platform:** The project is designed to run in Android and IOS, on each platform using the native UI libraries. 
*   **Remote Data Source:** Stories and related data are fetched from a remote server.
*   **Offline support:** The users can see and edit its own data offline.
*   **Clean Architecture:** The project utilizes a clean architecture approach to separate concerns and enhance testability.
*   **Dependency Injection:** Dependency injection is used to manage dependencies and increase flexibility.
*   **Modularization:** The project is divided into modules, promoting code organization and reusability.
*   **Modern Kotlin:** The project is written in Kotlin, leveraging its powerful features for concise and expressive code.

## Project Structure

The project is structured into several key modules:

*   **`commonMain`:** This is the core shared module that contains everything related to the Domain and ViewModel layers of the application.
*   **`androidMain`:** This module contains the Android-specific UI and entry points.
*   **`iosMain`:** Module for the IOS specific implementation.

## How It Works

1.  **Remote Data:** The application fetches story data from a remote server.
2.  **API Interaction:** The `URLs.kt` file defines the base URL for communicating with the server.
3.  **Data Handling:** The application uses a repository pattern to abstract the data source (remote or local) from the rest of the application.
4.  **Clean Architecture:** The core business logic and data handling are separate from the UI, following the principles of clean architecture.
5. **Dependency Injection:** The Koin dependency injection framework is used to make the code easier to test and maintain.
6. **Kotlin Multiplatform:** Kotlin Multiplatform is used to make the common code shareable between platforms Android and iOS.

## Future Improvements

*   **User Authentication:** Add user login/registration.
*   **Advanced UI/UX:** Improve the UI/UX with animations, transitions, and a more polished design.
*   **More tests:** Improve the test coverage.
