# Changelog

## [0.2.0] - 2026-07-16

### Added

- Automatic UDP receiver discovery.
- PocketInput Protocol v1.
- Transport-agnostic networking supporting USB tethering, Wi-Fi, and Bluetooth PAN.
- Stable receiver identities (`deviceId`, `deviceName`).
- `Discovery.kt` and `Connection.kt` networking modules.
- Receiver discovery service on the Linux side.

### Changed

- Removed the need to manually configure receiver IP addresses.
- Discovery now enumerates all active network interfaces and broadcasts on each network's directed broadcast address.
- Refactored Android networking into separate Discovery, Connection, and TCPClient layers.
- TCPClient now operates on an established TCP connection rather than creating one itself.

### Fixed

- Fixed Android UDP discovery on multiple network transports.
- Fixed Kotlin serialization omitting default-valued fields during discovery.
- Fixed discovery reply deserialization.
- Improved connection establishment and rediscovery workflow.

---

## [0.1.0] - 2026-07-06

### Added

- Initial public prototype.
- Android controller UI built with Jetpack Compose.
- TCP controller streaming.
- Linux receiver using `python-evdev` and `uinput`.
- Virtual Xbox-compatible controller.
