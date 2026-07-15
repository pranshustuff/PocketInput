# PocketInput

### v0.2.0

PocketInput is an open-source Android application that turns your phone into a game controller for Linux.

The Android client automatically discovers a Python receiver over UDP and streams controller state over TCP. The receiver translates controller state into Linux input events using the uinput subsystem, exposing a virtual Xbox-compatible controller compatible with Steam, SDL2, and other Linux applications.

## Features

* Automatic receiver discovery (UDP)
* Zero-configuration networking
* Transport-agnostic (USB tethering, Wi-Fi, Bluetooth PAN)
* TCP + JSON controller protocol
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
├── Networking
│   │
│   ├── Discovery (UDP)
│   ├── Connection (TCP)
│   └── TCPClient (JSON stream)
│
└──────────────────────────────────────────────┐
                                               │
                 UDP Discovery                 │
                 TCP Controller Stream         │
                 USB / Wi-Fi / Bluetooth PAN   │
                                               ▼
Linux
│
├── Discovery Server (UDP)
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

| Component        | Responsibility                                                              |
| ---------------- | --------------------------------------------------------------------------- |
| Compose UI       | Captures touch input.                                                       |
| ControllerState  | Represents the complete controller state.                                   |
| Discovery        | Discovers PocketInput receivers over UDP.                                   |
| Connection       | Establishes TCP connections to discovered receivers.                        |
| TCPClient        | Serializes and streams controller state over an established TCP connection. |
| Discovery Server | Responds to UDP discovery requests.                                         |
| TCP Server       | Accepts controller connections and receives controller packets.             |
| InputParser      | Maps controller state to Linux input events.                                |
| VirtualGamePad   | Emits virtual controller events using `uinput` and `python-evdev`.          |
| Steam / SDL2     | Uses the virtual controller as a standard gamepad.                          |


## PocketInput Protocol v1
```text
┌──────────────────────────┐                 ┌──────────────────────────┐
│      Android App         │                 │     Linux Receiver       │
└──────────────────────────┘                 └──────────────────────────┘

        │                                              │
        │ Enumerate network interfaces                 │
        │ (Wi-Fi, USB, Bluetooth...)                   │
        │                                              │
        │ Broadcast DISCOVER ─────────────────────────►│
        │  UDP :5678                                   │
        │                                              │
        │◄────────────────────── DISCOVER_REPLY        │
        │         deviceId, deviceName, port           │
        │                                              │
        │ TCP connect ────────────────────────────────►│
        │               TCP :5000                      │
        │                                              │
        │ Controller packets ─────────────────────────►│
        │                                              │
        │ Controller packets ─────────────────────────►│
        │                                              │
        │ Controller packets ─────────────────────────►│
        │                                              │
        │        TCP disconnect / network loss         │
        │◄────────────────────────────────────────────►│
        │                                              │
        │ Restart discovery                            │
```

Each packet contains the complete controller state, including:

* Left and right analog sticks
* Left and right triggers
* Face buttons (A, B, X, Y)
* Shoulder buttons (LB, RB)
* Start and Back buttons

The receiver is stateless with respect to incoming packets—every packet completely describes the current controller state.

## Android Discovery
```
Android                                Receiver

Enumerate interfaces
        │
        ├────────────► 10.198.145.255 (WiFi)
        │
        ├────────────► 10.185.164.255 (USB)
        │
        │                    Receive DISCOVER
        |
        │◄────────────────── DISCOVER_REPLY
        │
First valid reply wins
        │
TCP connect
```

## Demo

*Coming soon.*

## Roadmap
* Robust auto-reconnect and network health
* Improve joystick UX (thumb snapping, deadzones)
* Adjustable sensitivity
* Improved controller layout
* Easy Setup script
* Packageing apk and script
---
### Long-Term
* Multiple controllers
* Haptic Feedback
* Receiver GUI
* 
