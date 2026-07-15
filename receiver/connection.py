import socket

def start_tcp_server(port: int):
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    server.bind(("0.0.0.0", port))
    server.listen()

    print(f"TCP server listening on 0.0.0.0:{port}")

    return server

def wait_for_connection(server):
    conn, addr = server.accept()
    print(f"Connected to {addr[0]}:{addr[1]}")
    return conn, addr