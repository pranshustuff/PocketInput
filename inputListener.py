import evdev
from evdev import *

dev = evdev.InputDevice('/dev/input/event15')
print(dev)

for event in dev.read_loop():
    if event.type == ecodes.EV_KEY:
        print(categorize(event))