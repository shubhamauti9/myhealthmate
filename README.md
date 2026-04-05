# MyHealthMate

MyHealthMate is a comprehensive Android application designed to bridge the healthcare gap between doctors and patients. It acts as a digital health companion, providing a seamless healthcare management experience. The app allows patients to connect with doctors, manage their health profiles, track their medical expenses, schedule appointments, and perform preliminary health assessments.

## 🚀 Features

### For Patients
- **Health Profiles**: Maintain and manage comprehensive personal health profiles.
- **Doctor Connectivity**: Search for available doctors, view their profiles, and seamlessly schedule appointments.
- **Smart Questionnaires**: Perform preliminary health assessments using dedicated questionnaires for:
  - COVID-19
  - Influenza
  - Stress
- **Medication Reminders**: Set alarms and manage reliable reminders for taking medications on time.
- **Expense Tracking**: Keep a close log of medical expenses and manage finances effectively.

### For Doctors
- **Doctor Dashboard**: A specialized dashboard to manage workflow, see connected patients, and handle appointments.
- **Patient Reports**: Doctors can systematically view and analyze reports for their patients.
- **Profile Management**: Set up and update professional profiles for patients to connect.

## 🛠 Tech Stack

- **Platform**: Android SDK
- **Language**: Java
- **UI/Layout**: XML (ConstraintLayout, Material Design Components, CardView)
- **Backend Services**: 
  - [Firebase Realtime Database](https://firebase.google.com/docs/database) (for storing app data like users and expenses)
  - [Firebase Authentication](https://firebase.google.com/docs/auth) (for secure patient/doctor login and registration)
- **Networking**: [OkHttp](https://square.github.io/okhttp/)
- **Additional Libraries**: SearchableSpinner

## 📋 Prerequisites

To run this project on your local machine, you'll need the following:

- **Android Studio**: Arctic Fox or newer is recommended.
- **Android SDK**: Minimum API Level 19, Target API Level 30.
- **Java SDK**: Java 8 or above.

## ⚙️ Installation and Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/shubhamauti9/myhealthmate.git
   ```

2. **Open the project in Android Studio**:
   - Open Android Studio.
   - Click on `File` > `Open`.
   - Select the cloned directory.

3. **Configure Firebase**:
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project or select an existing one.
   - Add an Android app to your Firebase project using the package name `com.example.myhealthmate`.
   - Download the generated `google-services.json` file.
   - Place `google-services.json` into the `app/` directory of your project.

4. **Sync Gradle**:
   - Let Android Studio download necessary Gradle and dependency libraries.
   - Click "Sync Now" if prompted.

5. **Run the App**:
   - Connect an Android device or start an emulator.
   - Click the **Run** button (green arrow) in Android Studio's top toolbar.

## 📱 App Flow Architecture

1. **Main Screen**: Users open the application and are presented with an option to proceed as a **Doctor** or a **Patient**.
2. **Authentication**:
   - **Signup/Login**: Users can sign in or register based on their selected role.
3. **Dashboards**:
   - **Patient Dashboard**: The central hub for the patient covering all major features (Medication, Expense, Connecting to Doctor, Questionnaires).
   - **Doctor Dashboard**: The central hub tailored for doctors, displaying reports and patient lists.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the [LICENSE] automatically provided in the root directory. Feel free to modify and use it strictly adhering to its terms.