import socket
import json
import time

HOST = "127.0.0.1"
PORT = 5000

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST, PORT))

state = False

while True:
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

    sock.sendall((json.dumps(packet) + "\n").encode())

    state = not state
    time.sleep(1)