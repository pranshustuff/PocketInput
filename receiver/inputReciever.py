import socket
import json
from inputParser import InputParser

HOST = "10.78.238.132"
PORT = 5000

server = socket.socket(socket.AF_INET, socket.SOL_SOCKET)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind((HOST, PORT))
server.listen(1)

print(f"Listening on {HOST}:{PORT}")

parser = InputParser()

try:
    while True:
        print("Waiting for connection...")
        conn, addr = server.accept()
        print(f"Client connected: {addr}")

        buffer = ""

        try:
            while True:
                data = conn.recv(1024)

                if not data:
                    print("Client disconnected.")
                    parser.reset()
                    break

                buffer += data.decode()

                while "\n" in buffer:
                    line, buffer = buffer.split("\n", 1)

                    if not line.strip():
                        continue

                    try:
                        packet = json.loads(line)
                        parser.parse(packet)
                    except json.JSONDecodeError:
                        print("Received malformed JSON. Packet ignored.")
                    except KeyError as e:
                        print(f"Missing field: {e}. Packet ignored.")

        finally:
            conn.close()

except KeyboardInterrupt:
    print("\nShutting down...")

finally:
    parser.reset()
    parser.pad.close()
    server.close()
