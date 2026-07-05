from VirtualGamePad import VirtualGamePad
    # Example:
    # packet = {
    #     "lx": 12000,
    #     "ly": -8000,
    #     "rx": 3000,
    #     "ry": -12000,
    #     "lt": 255,
    #     "rt": 0,

    #     "a": True,
    #     "b": False,
    #     "x": False,
    #     "y": False,

    #     "lb": True,
    #     "rb": False,

    #     "start": False,
    #     "back": False,
    # }

class InputParser:
    def __init__(self):
        self.pad = VirtualGamePad()
        self.buttons = (
            "a", "b", "x", "y",
            "lb", "rb",
            "start", "back"
        )

    def parse_analog(self, data):
        self.pad.left_stick(data["lx"], data["ly"])
        self.pad.right_stick(data["rx"], data["ry"])
        self.pad.left_trigger(data["lt"])
        self.pad.right_trigger(data["rt"])

    def parse_buttons(self, data):
        for button in self.buttons:
            if data[button]:
                self.pad.press(button)
            else:
                self.pad.release(button)

    def parse(self, data):
        self.parse_analog(data)
        self.parse_buttons(data)
        self.pad.sync()

    def reset(self):
        self.pad.left_stick(0, 0)
        self.pad.right_stick(0, 0)
        self.pad.left_trigger(0)
        self.pad.right_trigger(0)

        for button in self.buttons:
            self.pad.release(button)

        self.pad.sync()