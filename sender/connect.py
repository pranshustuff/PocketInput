import socket

from discovery import find_receiver

UDP_PORT = 5678

def connect_receiver(receiver: dict):
    """Establish a TCP connection to a discovered receiver."""

    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)

    try:
        sock.connect((receiver["ip"], receiver["port"]))
        return sock

    except OSError as e:
        print(f"Failed to connect: {e}")
        sock.close()
        return None


def discover_and_connect():
    """Discover a receiver and establish a TCP connection."""

    receiver = find_receiver(UDP_PORT)

    if receiver is None:
        return None, None

    sock = connect_receiver(receiver)

    if sock is None:
        return receiver, None

    return receiver, sock