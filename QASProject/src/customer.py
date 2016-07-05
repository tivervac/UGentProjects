class Customer:
    def __init__(self, arrival_time, service_time):
        self.service_time = service_time
        self.arrival_time = arrival_time
        self.service_start = 0
        self.service_done = 0

    def set_service_start(self, service_start):
        self.service_start = service_start

    def set_service_done(self, service_done):
        self.service_done = service_done

    def waiting_time(self):
        return self.service_start - self.arrival_time

    def sojourn_time(self):
        return self.service_done - self.arrival_time
