import json
import time

from connect import *

receiver = None
sock = None
state = False

while True:
    if sock is None:
        print("Searching for receiver...")

        receiver, sock = discover_and_connect()

        if sock is None:
            time.sleep(1)
            continue

        print(f"Connected to {receiver['deviceName']}")

    packet = {
        "lx": 12000 if state else -12000,
        "ly": 0,
        "rx": 0,
        "ry": 0,
        "lt": 255 if state else 0,
        "rt": 0,
        "a": state,
        "b": False,
        "x": False,
        "y": False,
        "lb": False,
        "rb": False,
        "start": False,
        "back": False,
    }

    try:
        sock.sendall((json.dumps(packet) + "\n").encode())

    except (BrokenPipeError, ConnectionResetError, OSError):
        print("Connection lost.")
        sock.close()
        sock = None
        continue

    state = not state
    time.sleep(1)