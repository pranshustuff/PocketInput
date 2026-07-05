import asyncio
from evdev import UInput, categorize, ecodes as e
from evdev import AbsInfo, ecodes
import time

cap = {
    ecodes.EV_KEY: [
        ecodes.BTN_SOUTH,   # A
        ecodes.BTN_EAST,    # B
        ecodes.BTN_NORTH,   # X
        ecodes.BTN_WEST,    # Y
        ecodes.BTN_TL,
        ecodes.BTN_TR,
        ecodes.BTN_SELECT,
        ecodes.BTN_START,
    ],
    ecodes.EV_ABS: [
        (
            ecodes.ABS_X,
            AbsInfo(value=0, min=-32768, max=32767,
                    fuzz=0, flat=0, resolution=0),
        ),
        (
            ecodes.ABS_Y,
            AbsInfo(value=0, min=-32768, max=32767,
                    fuzz=0, flat=0, resolution=0),
        ),
    ],
}

ui = UInput(cap, name='test-controller', version=0x3)
x = -32768

while True:
    ui.write(e.EV_ABS, e.ABS_X, x)
    ui.syn()

    x = -x
    time.sleep(1)