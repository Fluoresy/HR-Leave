# HR Leave (US1)
Java 17, Maven, console app, data in-memory.

Jalankan:
    mvn compile
    java -cp target/classes com.example.hrleave.ui.ConsoleApp

Scan SonarQube (lokal):
    mvn verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=<TOKEN>
