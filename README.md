# Aklati 🍽️ | Your Ultimate Food Companion

![Aklati Banner](https://your-link-to-cover-image.png)

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com)
[![Architecture](https://img.shields.io/badge/Architecture-MVP-orange.svg)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Aklati** is a modern, high-performance food discovery application built following industry best practices. It offers a seamless experience for searching, saving, and learning new recipes with a polished UI and robust offline capabilities.

---

## 📱 Visual Showcase

| Authentication | Home & Discovery | Meal Details |
|---|---|---|



## ✨ Key Features

* **Complete Authentication:** Secure Login and Registration system with real-time input validation.
* **Meal Discovery:** Explore meals by category (Beef, Chicken, Dessert) or area (Egyptian, Filipino, French).
* **Offline Favorites:** Save your favorite recipes to a local **Room Database** for instant access without internet.
* **Step-by-Step Guides:** Detailed ingredients lists and cooking steps for every meal.
* **YouTube Integration:** Watch video tutorials directly inside the app for a better learning experience.
* **Modern UI/UX:** Built with **Material Design 3** and fully immersive **Edge-to-Edge** layouts.

---

## 🛠️ Technical Excellence (Tech Stack)

### **Core Development**
* **Languages:** Developed using **Java** with seamless **Kotlin** interoperability.
* **Architecture:** Implemented **MVP (Model-View-Presenter)** for a clean separation of concerns and maintainable codebase.
* **Build System:** **Gradle** with modern dependency management.

### **Architecture & Libraries**
* **Navigation Component:** Managed complex fragment transactions and backstacks with a single-activity architecture.
* **Room Database:** High-level abstraction over SQLite for persistent local storage.
* **Shared Preferences:** Used for efficient user session and preference management.
* **Glide:** For optimized image loading, caching, and placeholder handling.

### **Problem Solving & UX Polish**
* **Edge-to-Edge Layouts:** Utilizing `WindowInsets` to respect system bars and display cutouts.
* **Advanced IME Handling:** Custom logic to prevent the keyboard from overlapping input fields during authentication.
* **SplashScreen API:** Providing a modern launch experience for Android 12+ devices.

---

## 🏗️ Project Structure

```text
app/
├── src/main/
│   ├── java/com/example/aklati/
│   │   ├── data/           # Repositories & Room Database
│   │   ├── presenter/      # Business Logic (MVP Presenters)
│   │   ├── ui/             # Fragments, Activities, & Adapters
│   │   └── utils/          # Helpers (SharedPrefs, Validators)
│   ├── res/                # Layouts, Drawables, & Navigation Graph
│   └── AndroidManifest.xml
└── build.gradle
```

##🚀Getting Started

Prerequisites 
 - Android Studio Narwhal | 2025.1.1 or later.
 - JDK 17 or later.
 - Android SDK API 21+ (Lollipop).

## Installation

1.Clone the repository:
```
      git clone https://github.com/yourusername/aklati.git
```
2.Open in Android Studio.
3.Sync Project with Gradle Files.
4.Run on a Physical Device (Recommended for Edge-to-Edge testing).
ؤ
### 👤 Author
Ahmed Nageh Mohamed
- 🎓 ITI Student - Android Development Track
- 📱 Connect with me on LinkedIn
- 💻 More projects on GitHub

### 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
