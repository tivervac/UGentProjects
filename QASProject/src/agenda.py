class Agenda:
    def __init__(self, c):
        self.c = c
        self.events = {}
        self.current_time = 0
        self.queue = []

    def schedule(self, time, event, arguments):
        if time in self.events:
            self.events[time].append([event, arguments])
        else:
            self.events[time] = [[event, arguments]]

    def next_event(self):
        self.current_time = min(self.events)
        for event, args in self.events.pop(self.current_time):
            args = [self] + args
            event(*args)

    def has_events(self):
        return len(self.events) > 0

    def has_servers(self):
        return self.c > 0

    def get_server(self):
        self.c -= 1

    def free_server(self):
        self.c += 1

    def add_customer(self, customer):
        self.queue.append(customer)
