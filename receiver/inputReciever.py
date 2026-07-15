import json
from inputParser import InputParser
from connection import *
from discover_clients import *
import time

TCP_PORT = 5000

server = start_tcp_server(TCP_PORT)
parser = InputParser()

try:
    while True:
        find_client(TCP_PORT)

        print("Waiting for TCP connection...")
        conn, addr = wait_for_connection(server)

        buffer = ""

        try:
            while True:
                data = conn.recv(1024)

                if not data:
                    print("Client disconnected.")
                    break

                buffer += data.decode()

                while "\n" in buffer:
                    line, buffer = buffer.split("\n", 1)

                    if not line.strip():
                        continue

                    packet = json.loads(line)
                    parser.parse(packet)

        finally:
            parser.reset()
            conn.close()

except KeyboardInterrupt:
    ...