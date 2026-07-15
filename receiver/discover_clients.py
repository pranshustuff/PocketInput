from pathlib import Path
import json
import socket
import uuid

CONFIG_PATH = Path.home() / ".config" / "pocketinput" / "config.json"

UDP_IP = "0.0.0.0"
UDP_PORT = 5678
TCP_PORT = 5000

PROTOCOL = 1
SERVICE = "PocketInput"


def load_device_info():
    if CONFIG_PATH.exists():
        return json.loads(CONFIG_PATH.read_text())

    device_info = {
        "deviceId": str(uuid.uuid4()),
        "deviceName": socket.gethostname(),
        "protocol": PROTOCOL,
    }

    CONFIG_PATH.parent.mkdir(parents=True, exist_ok=True)
    CONFIG_PATH.write_text(json.dumps(device_info, indent=4))

    return device_info


def find_client():
    device_info = load_device_info()

    DISCOVER_REPLY = {
        "service": SERVICE,
        "type": "discover_reply",
        "deviceId": device_info["deviceId"],
        "deviceName": device_info["deviceName"],
        "protocol": PROTOCOL,
        "port": TCP_PORT,
    }

    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as server:
        server.bind((UDP_IP, UDP_PORT))

        print(f"Listening for discovery on {UDP_IP}:{UDP_PORT}")

        while True:
            data, addr = server.recvfrom(1024)

            try:
                packet = json.loads(data.decode())
            except json.JSONDecodeError:
                continue

            if (
                packet.get("type") == "discover"
                and packet.get("service") == SERVICE
                and packet.get("protocol") == PROTOCOL
            ):
                server.sendto(
                    json.dumps(DISCOVER_REPLY).encode(),
                    addr,
                )

                print(f"Discovered client at {addr[0]}:{addr[1]}")

                return addr

if __name__ == "__main__":
    find_client()