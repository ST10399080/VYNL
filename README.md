# <h1 align="center">VYNL</h1>

<p align="center">
  <img width="1184" height="628" alt="Screenshot 2026-08-11 171544" src="https://github.com/user-attachments/assets/b1b90b3c-c741-4dc7-965c-555152f907a5" />
</p>

<p align="center"><i>**Music Discovery, Tracking, Review, and Recommendation Application**</i></p>

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Kotlin-purple)
![API](https://img.shields.io/badge/API-REST-blue)
![Database](https://img.shields.io/badge/Database-MySQL%20%7C%20SQLite-orange)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow)
![License](https://img.shields.io/badge/License-MIT-lightgrey)
</i>
---

# 📖 Overview

VYNL is an Android application designed to help music enthusiasts discover, organise, review, and share their favourite music. Inspired by platforms such as **Last.fm**, **Musicboard**, and **Spotify**, the application combines music discovery, album tracking, user reviews, personalised recommendations, and social interaction into a single modern platform.

Unlike traditional music streaming services, VYNL focuses on helping users build a personal music library, discover new albums, review what they listen to, and connect with people who share similar musical tastes.

This application is being developed as part of the **OPSC6312 Open Source Coding (Intermediate) Portfolio of Evidence (PoE)**.

---

# 🎯 Project Objectives

The primary objectives of this project are to:

* Discover new music through intelligent recommendations.
* Organise albums and songs into personalised listening lists.
* Track listening history.
* Rate and review albums.
* Encourage community interaction.
* Recommend music based on user preferences.
* Demonstrate Android development best practices.
* Integrate REST APIs with a database.
* Showcase GitHub version control and CI/CD.

---

# ✨ Core Features

## 🎵 Music Discovery

* Search Albums
* Search Songs
* Search Artists
* Browse Trending Music
* Browse Popular Albums
* Album Information
* Artist Profiles

---

## 👤 User Accounts

* User Registration
* Secure Login
* Password Encryption
* User Settings
* Profile Management

---

## ⭐ Reviews & Ratings

* Album Ratings
* Album Reviews
* Community Reviews
* Average Ratings
* Review History

---

## 📚 Music Library

* Personal Listening Lists
* Favourite Albums
* Favourite Songs
* Listening History
* Recently Played

---

## 🤝 Social Features

* User Profiles
* Public Reviews
* Taste Compatibility

---

# 🚀 User-Defined Features

## ⭐ User Feature 1 — Album Rating System (Part 2)

Users can rate albums using a five-star rating system.

Features include:

* Five-star ratings
* Edit ratings
* Store ratings
* Average album rating

---

## 🏆 User Feature 2 — Album Ranking (Part 2)

Automatically generate ranked album lists based on ratings.

Examples:

* Highest Rated Albums
* Most Popular Albums
* User Top Albums

---

## 🎨 User Feature 3 — Profile Customisation (Part 2)

Users can personalise their accounts by changing:

* Profile Picture
* Biography
* Favourite Genres
* Favourite Artists
* Theme Preferences

---

## ❤️ User Feature 4 — Interactive Taste Compatibility Index (Final PoE)

Compare two users' music preferences to determine compatibility.

The compatibility score is generated using:

* Favourite Genres
* Favourite Artists
* Favourite Albums
* Album Ratings
* Listening Activity

---

# 🌟 Planned Add-On Features

These features are outside the assessment requirements but are planned as future enhancements.

## 🎮 Daily Music Challenge

A daily music guessing game where users identify albums, artists, or songs using progressively revealed clues.

Possible challenge types include:

* Album Guess
* Artist Guess
* Genre Guess
* Music Trivia

---

# 🏗️ Technology Stack

## 📲 Mobile Development

* Kotlin
* Android Studio
* Material Design 3
* Jetpack Components

---

## 📱 Backend

* REST API
* JSON
* HTTP

---

## 🗄️ Database

* MySQL *(Server Database)*
* SQLite / Room *(Offline Storage)*

---

## 🫆 Authentication

* Secure Password Hashing
* Google Sign-In (Final PoE)

---

## 🆚 Version Control

* Git
* GitHub

---

## ✅ CI/CD

* GitHub Actions

---

## 🔨 Development Tools

* Android Studio
* IntelliJ IDEA
* Postman
* Figma
* GitHub Projects

---

# 🗂️ Planned Architecture

```text
Android Application
        │
        ▼
REST API
        │
        ▼
Application Server
        │
        ▼
MySQL Database

        ▲

Room Database
(Offline Storage)
```

---

# 📱 Planned Screens

* Splash Screen
* Login
* Register
* Home
* Search
* Album Details
* Artist Details
* Reviews
* Profile
* Settings
* Listening Lists
* Compatibility

---

# 🗄️ Database Overview

The application stores information including:

* Users
* Albums
* Songs
* Artists
* Ratings
* Reviews
* Listening Lists
* Listening History
* Compatibility Scores

---

# 🔒 Security

Security features include:

* Password Encryption
* Input Validation
* Secure Authentication
* API Validation
* Secure Data Storage
* Session Management

---

# 🌐 API Integration

The application communicates with a REST API to:

* Authenticate Users
* Retrieve Album Information
* Retrieve Artist Information
* Store Ratings
* Store Reviews
* Retrieve Recommendations
* Manage User Profiles

---

# 📦 Installation

## Clone Repository

```bash
git clone https://github.com/yourusername/undefined.git
```

---

## Open Project

Open the project in **Android Studio**.

---

## Build Project

Allow Gradle to download all dependencies.

---

## ▶️ Run

Run the application on:

* Android Emulator
* Physical Android Device

---

# 🧪 Testing

The project uses:

* Unit Testing
* Integration Testing
* UI Testing
* GitHub Actions Continuous Integration

---

# 📈 Project Roadmap

## Part 2

* Project Setup
* User Authentication
* REST API Integration
* Album Search
* Song Search
* Album Rating System
* Album Ranking
* Profile Customisation
* GitHub Actions
* Demonstration Video

---

## Final PoE

* Single Sign-On
* Offline Synchronisation
* Push Notifications
* Multi-language Support
* Taste Compatibility Index
* Google Play Preparation
* Release Notes

---

# 🤝 Contributing

This repository is maintained for educational purposes as part of the OPSC6312 Portfolio of Evidence.

Contributions are welcome through:

* Pull Requests
* Issue Reporting
* Feature Suggestions

---

# ▶️ Youtube Demo

* Part 2 Demo Link:
* POE Demo Link:

---

# 📄 License

This project is licensed under the **MIT License**.

---

# 👨‍💻 Author

**Developer:** 
* Fezile Jam-Jam (ST10399080)
* Tumelo Teka (ST10126814)
* Ivant Wambo (ST10448302)

📕 Module:

**OPSC6312 – Open Source Coding (Intermediate)**

🏫 Institution:

**The Independent Institute of Education (IIE) Rosebank International Braamfontein**

---

# 🙏 Acknowledgements

This project was inspired by:

* Last.fm
* Musicboard
* Spotify

Special thanks to the Android open-source community for the tools, libraries, and documentation that supported the development of this project.
