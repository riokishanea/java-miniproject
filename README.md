# Student Skill Passport

A small Java Swing desktop application for students to keep a profile of their skills and projects. Faculty can view student profiles. The app uses MySQL to save its information.

## Project structure

```text
OOPS/
|-- database.sql                 Creates the database, tables, and sample data
|-- lib/
|   `-- mysql-connector-j-26.7.0.jar  Java driver used to connect to MySQL
|-- src/
|   |-- Main.java                Starts the application at the login screen
|   |-- database/
|   |   `-- DBConnection.java    Opens a connection to MySQL
|   |-- model/                   Java classes that represent app data
|   |   |-- User.java
|   |   |-- Student.java
|   |   |-- Skill.java
|   |   `-- Project.java
|   |-- dao/                     Reads and writes data in the database
|   |   |-- UserDAO.java
|   |   |-- StudentDAO.java
|   |   |-- SkillDAO.java
|   |   `-- ProjectDAO.java
|   `-- ui/                      Swing screens shown to the user
|       |-- LoginFrame.java
|       |-- RegisterFrame.java
|       |-- StudentDashboard.java
|       `-- FacultyDashboard.java
`-- out/                         Compiled Java files (created when building)
```

### How the parts work together

1. `Main.java` opens the login screen in `ui`.
2. A screen gets or updates information through a DAO in `dao`.
3. A DAO uses `DBConnection.java` to connect to MySQL and stores or retrieves data represented by the classes in `model`.

## Requirements

- A JDK installed (includes `java` and `javac` commands).
- MySQL Server running on this computer.
- MySQL Workbench, used below to create the project database.

## Set up MySQL with Workbench

1. Start MySQL Server and open MySQL Workbench.
2. Open your local MySQL connection. Use the MySQL username and password you created when installing MySQL. The project assumes the server is on `localhost`, port `3306`.
3. In Workbench, choose **File > Open SQL Script** and open `database.sql` from this project folder.
4. Click the lightning bolt **Execute** button. This creates the `student_skill_passport` database, its tables, and sample student/faculty records.
5. In Workbench's **Schemas** panel, refresh the schemas list. You should see `student_skill_passport`.

## Set the app's MySQL password

The username, password, and connection URL are in `src/database/DBConnection.java`. Set `USERNAME` to your MySQL username and `PASSWORD` to the password for that MySQL account. These are the **database credentials**, not the demo login credentials below. The database name and connection URL already match the script (`student_skill_passport` on `localhost:3306`).

The app currently keeps these settings in the source code, so update them before running it on another computer. Do not share a real database password publicly.

## Compile and run on Windows

Open PowerShell in the project folder (the folder containing `src` and `lib`) and run:

```powershell
New-Item -ItemType Directory -Force out
javac -cp "lib/mysql-connector-j-26.7.0.jar" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out;lib/mysql-connector-j-26.7.0.jar" Main
```

Keep MySQL Server running while using the app. If PowerShell says `javac` or `java` is not recognized, install a JDK and make sure its `bin` folder is on your Windows `PATH`.

## Demo login accounts

These accounts are inserted by `database.sql`:

| Role | Email | Password |
|---|---|---|
| Faculty | `faculty@college.com` | `faculty123` |
| Student | `aarav@student.com` | `student123` |
| Student | `diya@student.com` | `student123` |
| Student | `rohan@student.com` | `student123` |

Choose the matching role on the login screen. These are sample accounts for demonstration.

## Troubleshooting

- **"No suitable driver found"**: include `lib/mysql-connector-j-26.7.0.jar` in both commands exactly as shown above.
- **Access denied for user**: check `USERNAME` and `PASSWORD` in `DBConnection.java` against the MySQL account you use in Workbench.
- **Connection refused / communications link failure**: make sure MySQL Server is running and listening on port `3306`.
- **Unknown database `student_skill_passport`**: run `database.sql` in Workbench and refresh the Schemas panel.
- **Login failed**: check the email, password, and selected role. Re-run the SQL script if the sample data is missing.
