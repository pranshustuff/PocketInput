# PocketInput

### v0.1.0

PocketInput is an open-source Android application that turns your phone into a game controller for Linux.

The Android client communicates with a Python receiver over TCP. The receiver translates controller state into Linux input events using the `uinput` subsystem, exposing a virtual Xbox-compatible controller that works with Steam, SDL2, and other applications.

## Features

* Transport-agnostic TCP/IP architecture
* Works over USB tethering, Wi-Fi, and Bluetooth PAN
* TCP + JSON protocol
* Virtual Xbox-compatible controller
* Compatible with Steam and SDL2 games
* Built with Kotlin, Jetpack Compose, and `python-evdev`

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
        │ TCP + JSON
        │
        │ (USB tethering / Wi-Fi / Bluetooth PAN)
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

| Component       | Responsibility                                                     |
| --------------- | ------------------------------------------------------------------ |
| Compose UI      | Captures touch input.                                              |
| ControllerState | Represents the complete controller state.                          |
| TCPClient       | Serializes the controller state and transmits it over TCP.         |
| TCP Server      | Receives JSON packets from the Android client.                     |
| InputParser     | Maps controller state to Linux input events.                       |
| VirtualGamePad  | Emits virtual controller events using `uinput` and `python-evdev`. |
| Steam / SDL2    | Uses the virtual controller as a standard gamepad.                 |

## Protocol

Each packet contains the complete controller state, including:

* Left and right analog sticks
* Left and right triggers
* Face buttons (A, B, X, Y)
* Shoulder buttons (LB, RB)
* Start and Back buttons

The receiver is stateless with respect to incoming packets—every packet completely describes the current controller state.

## Demo

*Coming soon.*

## Roadmap

* Connection management and automatic reconnect
* Improve joystick UX (thumb snapping, deadzones)
* Adjustable sensitivity
* Automatic receiver discovery
* Improved controller layout
