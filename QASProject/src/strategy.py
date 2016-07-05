from abc import abstractmethod
from math import sqrt, ceil, floor

from numpy.random import poisson, gamma

from agenda import Agenda
from customer import Customer


class Strategy:
    def __init__(self, max_arrivals, a_ser=1, b_ser=1, a_arr=1, b_arr=1, c=1):
        self.max_arrivals = max_arrivals
        self.aS = a_ser
        self.bS = b_ser
        self.aA = a_arr
        self.bA = b_arr
        self.c = c
        self.lam = (self.aS + self.bS + self.aA + self.bA) / 3
        # M
        self.batch_size = floor(self.max_arrivals / 100)

        # Keeps track of customers
        self.arrivals = 0
        self.departures = 0
        self.done = []

        # Performance measures
        self.mon_events = []
        self.queue_contents = []
        self.avg_waiting_time = []
        self.avg_sojourn_time = []

        # Utils for calculating performance measures
        self.prev_waiting_time = 0
        self.prev_sojourn_time = 0
        self.prev_done_index = 0

    def run(self):
        agenda = Agenda(self.c)
        agenda.schedule(0, self.arrival, [])
        agenda.schedule(0, self.monitor, [])
        while agenda.has_events():
            agenda.next_event()

    def batch_means(self, kp=1):
        # K
        tot_event = len(self.mon_events)
        # L
        batches = ceil(tot_event / self.batch_size)
        sample_means = []
        for i in range(0, batches):
            batch = self.queue_contents[i * self.batch_size:(i + 1) * self.batch_size]
            # j_l
            sample_means.append(sum(batch) / len(batch))

        # J_K
        overall_sample_mean = sum(sample_means) / batches
        temp_sum = sum([(l - overall_sample_mean) ** 2 for l in sample_means])
        variance = temp_sum / (batches ** 2)
        sd = sqrt(temp_sum) / batches
        # J
        estimated_mean = sum(self.queue_contents) / len(self.mon_events)
        low = overall_sample_mean - (kp * sd)
        high = overall_sample_mean + (kp * sd)
        return low, estimated_mean, high, variance

    def arrival(self, agenda):
        # Servers are available, service the customer
        service_time = gamma(self.aS, self.bS)
        if agenda.has_servers():
            # Initialise the customer
            customer = Customer(agenda.current_time, service_time)
            customer.set_service_start(agenda.current_time)

            # Schedule customer for departure
            agenda.get_server()
            agenda.schedule(agenda.current_time + service_time, self.departure, [customer])

        # No servers available, add customer to the queue
        else:
            agenda.add_customer(Customer(agenda.current_time, service_time))

        self.arrivals += 1
        if self.arrivals < self.max_arrivals:
            # New arrival
            new_arr_time = gamma(self.aA, self.bA)
            agenda.schedule(agenda.current_time + new_arr_time, self.arrival, [])

    def departure(self, agenda, customer):
        self.departures += 1
        customer.set_service_done(agenda.current_time)
        agenda.free_server()
        self.done.append(customer)

        # If customers in queue
        if agenda.queue:
            # Get the customer according to the current strategy and further initialise it
            new_customer = self.get_customer(agenda)
            new_customer.set_service_start(agenda.current_time)

            # Schedule customer for departure
            agenda.get_server()
            dep_time = agenda.current_time
            agenda.schedule(dep_time + new_customer.service_time, self.departure, [new_customer])

    def monitor(self, agenda):
        self.mon_events.append(agenda.current_time)
        # Sum the performance measures since the previous monitor and save them
        self.queue_contents.append(len(agenda.queue))
        self.prev_waiting_time += sum([getattr(x, "waiting_time")() for x in self.done[self.prev_done_index:]])
        self.prev_sojourn_time += sum([getattr(x, "sojourn_time")() for x in self.done[self.prev_done_index:]])
        denominator = 1 if len(self.done) == 0 else len(self.done)
        self.avg_waiting_time.append(self.prev_waiting_time / denominator)
        self.avg_sojourn_time.append(self.prev_sojourn_time / denominator)
        self.prev_done_index = len(self.done)

        # Schedule the next monitoring event if not all customers have been served yet
        if self.departures < self.max_arrivals:
            poiss_time = poisson(self.lam)
            agenda.schedule(agenda.current_time + poiss_time, self.monitor, [])

    @abstractmethod
    def get_customer(self):
        """Returns a customer according to the current strategy"""
        return
