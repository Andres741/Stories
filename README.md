Personal project to practice with KMP and SwiftUI technologies, consisting of a small social network where users can share life stories made up of text or image articles.  

The application is developed using KMP, with the UI implemented in Jetpack Compose on the Android side and SwiftUI on the iOS side.  

It follows Clean Architecture with MVI, where both the ViewModel and the domain layer reside in the common module, leaving only the UI in the platform-specific modules.  

The app accesses both cloud and local data. It connects to the server (also developed by me using Spring Boot) via a REST API using the Ktor library. Local data is stored in a MongoDB Realm database.
