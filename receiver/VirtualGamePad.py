import asyncio
from evdev import UInput, categorize, ecodes as e
from evdev import AbsInfo
import time

class VirtualGamePad:
    def __init__(self):
        self.cap = {
            e.EV_KEY: [
                e.BTN_SOUTH,   # A
                e.BTN_EAST,    # B
                e.BTN_NORTH,   # X
                e.BTN_WEST,    # Y
                e.BTN_TL,   #LB
                e.BTN_TR,   #RB 
                e.BTN_SELECT, #Back
                e.BTN_START,
            ],
            e.EV_ABS: [
                #Left Joystick
                (
                    e.ABS_X,
                    AbsInfo(value=0, min=-32768, max=32767,
                            fuzz=0, flat=0, resolution=0),
                ),
                (
                    e.ABS_Y,
                    AbsInfo(value=0, min=-32768, max=32767,
                            fuzz=0, flat=0, resolution=0),
                ),
                #Right JoyStick
                (
                    e.ABS_RX,
                    AbsInfo(value=0, min=-32768, max=32767,
                            fuzz=0, flat=0, resolution=0),
                ),
                (
                    e.ABS_RY,
                    AbsInfo(value=0, min=-32768, max=32767,
                            fuzz=0, flat=0, resolution=0),
                ),
                #Left Trigger
                (
                    e.ABS_Z,
                    AbsInfo(value=0, min=0, max=255,
                            fuzz=0, flat=0, resolution=0),
                ),
                #Right Trigger
                (
                    e.ABS_RZ,
                    AbsInfo(value=0, min=0, max=255,
                            fuzz=0, flat=0, resolution=0),
                ),
            ],
        }

        self.ui = UInput(self.cap, name='PocketInput', version=0x3)
        self.map = {
            # Axes
            "lx": e.ABS_X,
            "ly": e.ABS_Y,
            "rx": e.ABS_RX,
            "ry": e.ABS_RY,
            "lt": e.ABS_Z,
            "rt": e.ABS_RZ,

            # Buttons
            "a": e.BTN_SOUTH,
            "b": e.BTN_EAST,
            "x": e.BTN_WEST,
            "y": e.BTN_NORTH,

            "lb": e.BTN_TL,
            "rb": e.BTN_TR,

            "start": e.BTN_START,
            "back": e.BTN_SELECT,
        }

    def press(self, button):
        self.ui.write(e.EV_KEY, self.map[button], 1)

    def release(self, button):
        self.ui.write(e.EV_KEY, self.map[button], 0)

    def left_stick(self, x, y):
        self.ui.write(e.EV_ABS, e.ABS_X, x)
        self.ui.write(e.EV_ABS, e.ABS_Y, y)

    def right_stick(self, x, y):
        self.ui.write(e.EV_ABS, e.ABS_RX, x)
        self.ui.write(e.EV_ABS, e.ABS_RY, y)

    def left_trigger(self, value):
        self.ui.write(e.EV_ABS, e.ABS_Z, value)

    def right_trigger(self, value):
        self.ui.write(e.EV_ABS, e.ABS_RZ, value)

    def sync(self):
        self.ui.syn()
    
    def close(self):
        self.ui.close()
        

