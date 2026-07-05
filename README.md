# PocketInput

PocketInput is an open-source Android application that turns your phone into a wired game controller for Linux using USB tethering.

The Android app communicates with a Python receiver over TCP, which emulates a virtual Xbox-compatible controller using Linux's uinput subsystem.

## Features

- USB tethering (no Bluetooth)
- TCP + JSON protocol
- Virtual Xbox-compatible controller
- Works with Steam and SDL2 games
- Built with Kotlin, Jetpack Compose, and python-evdev

## Architecture

## Architecture

```text
Android
│
├── Jetpack Compose UI
│
├── ControllerState
│
└── TCPClient
        │
        │ TCP + JSON over USB tethering
        ▼
Linux
│
├── TCP Server
│
├── InputParser
│
├── VirtualGamePad
│
└── uinput / evdev
        │
        ▼
Steam / SDL2 / Native Linux Games
```

### Components

| Component | Responsibility |
|-----------|----------------|
| Compose UI | Captures touch input. |
| ControllerState | Represents the complete controller state. |
| TCPClient | Serializes the controller state and sends it over TCP. |
| TCP Server | Receives JSON packets from the Android client. |
| InputParser | Maps JSON fields to controller events. |
| VirtualGamePad | Emits Linux input events through `uinput`. |
| Steam / SDL2 | Uses the virtual controller like any physical gamepad. |

## Demo

## Roadmap

- Better joystick UX
- Adjustable sensitivity
- Auto-discover receiver
- Improved controller layout
