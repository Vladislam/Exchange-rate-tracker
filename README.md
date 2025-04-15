# 💱 Exchange Rate Tracker

A modern Android application for tracking real-time exchange rates — built as part of the Al Hilal Android Engineer Assignment.

---

## ✨ Features

- 🔎 **Search Currencies** by code or name with live filtering
- 📌 **Pin Currencies** to save them for easy access on the Home screen
- 🔄 **Auto-Refresh Every 5 Seconds** for pinned rates via background service
- 🧠 **Offline Support** — Cached data shown if internet is unavailable
- 💬 **Recent Searches** saved and shown with flow
- 🗑️ **Remove by Swipe** on Home screen
- 🎨 **Material 3 UI + Smooth Transitions** and animations
- 🧪 **Unit Tested ViewModels** and composable UI tested
- 🧱 Built with **Clean Architecture + MVI**

---

## 📷 Screenshots

> *(Add your own here if you'd like to impress visually)*

---

## 🧪 Tech Stack

- **Jetpack Compose**
- **Hilt DI**
- **Room DB**
- **Coroutines + Flow**
- **Retrofit + Gson**
- **MVI Architecture**
- **Unit Testing with JUnit + MockK**

---

## 🧰 Setup Instructions

1. Clone the project:

```bash
git clone https://github.com/yourname/exchange-rate-tracker.git
```
2. Get a free API key from OpenExchangeRates

3. Add this line to your local.properties:
OPEN_EXCHANGE_API_KEY=95a4cd05c8a9417790f02c93f7939622

## 🧪 Running Tests
```bash
./gradlew testDebug
```
## 📦 Project Structure
📦com.example.exchangeratetracker
 ┣ 📂data          → Room, Retrofit, Mappers
 ┣ 📂domain        → Models, Repository Interface, Use Cases
 ┣ 📂presentation  → Screens, ViewModels, MVI Contracts
 ┣ 📂service       → Foreground service for syncing
 ┣ 📂di            → Hilt modules
 ┣ 📂utils         → Resource, Extensions

## 📌 Notes
API key is injected via BuildConfig and not hardcoded
Rates are auto-fetched in the background when app is open
All dependencies managed via libs.versions.toml

## ✅ Completed Assignment Goals
 Home screen with selected rates
 Search screen with matching & caching
 Rate updates every 5 seconds
 Persistent pinned items
 Smooth UI experience
 Unit + UI testing
 Clean architecture (MVI + DI + Room)

👤 Author
Vladylsav Kutsmai
