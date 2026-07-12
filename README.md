# Hollow Knight mini

A 2D action-platformer game developed in **Java** using the **libGDX** framework.

This project was developed as the **Advanced Programming (AP) Graphics Course Project** at **Aryamehr University of Technology (Sharif University of Technology)**.

---

## Architecture

The project follows the **Model-View-Controller (MVC)** architectural pattern to separate game logic, rendering, and user interactions.

---

## Features

* 2D player movement
* Melee combat system
* Enemy AI
* Boss battle
* Multiple maps
* Health & Soul HUD
* Achievement system
* Main menu and settings
* Sound effects and background music
* Animated characters and enemies
* Tile-based levels designed with Tiled

---

## Technologies

* Java
* libGDX
* LWJGL3
* Gradle
* Tiled Map Editor

---

## Project Structure

The project is organized using the MVC architecture.

```text
HollowKnight
├── assets/
│   ├── audio/
│   ├── enemies/
│   ├── hud/
│   ├── map/
│   ├── player/
│   ├── ui/
│   └── ...
│
├── core/
│   ├── controller/
│   ├── model/
│   ├── view/
│   ├── utils/
│   ├── Main.java
│   └── ...
│
├── lwjgl3/
│   └── DesktopLauncher.java
│
├── gradle/
├── build.gradle
└── settings.gradle
```

> The complete project directory contains nearly **2,800 files**, including source code, assets, maps, animations, audio files, and Gradle configuration.

---

## Getting Started

### Requirements

* Java 17+
* Gradle (or the included Gradle Wrapper)

### Run

```bash
./gradlew lwjgl3:run
```

Windows:

```bash
gradlew.bat lwjgl3:run
```

### Build

```bash
./gradlew lwjgl3:jar
```

The runnable JAR will be generated inside:

```text
lwjgl3/build/libs/
```

---

## Screenshots

Gameplay screenshots and GIFs will be added soon.

---

## Course Information

**Course:** Advanced Programming (Graphics Project)

**University:** Aryamehr University of Technology (Sharif University of Technology)

**Framework:** libGDX

**Language:** Java
