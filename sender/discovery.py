import json
import socket

DISCOVER = {
    "service": "PocketInput",
    "type": "discover",
    "protocol": 1,
}

BROADCAST = "255.255.255.255"


def find_receiver(port):
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
        sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        sock.settimeout(2)

        sock.sendto(
            json.dumps(DISCOVER).encode(), 
            (BROADCAST, port)
        )

        try:
            data, addr = sock.recvfrom(1024)
        except socket.timeout:
            return None

    packet = json.loads(data.decode())

    if (
        packet.get("type") == "discover_reply"
        and packet.get("protocol") == DISCOVER["protocol"]
    ):
        packet["ip"] = addr[0]
        return packet

    return None