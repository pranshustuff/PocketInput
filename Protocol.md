PocketInput Protocol v1

UDP
----
Port: 5678

DISCOVER
```json
{
    "service": "PocketInput",
    "type": "discover",
    "protocol": 1,
}

```


DISCOVER_REPLY

```json
{
        "service": "PocketInput",
        "type": "discover_reply",
        "deviceId": device_info["deviceId"],
        "deviceName": device_info["deviceName"],
        "protocol": 1,
        "port": TCP_PORT,
}

```

TCP
----
Port: 5000

INPUT
HEARTBEAT

Discovery
---------
Android broadcasts DISCOVER.
Receiver replies with DISCOVER_REPLY.
Android connects via TCP.