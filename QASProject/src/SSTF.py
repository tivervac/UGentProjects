from operator import attrgetter

from strategy import Strategy


class SSTF(Strategy):
    @staticmethod
    def get_customer(agenda):
        result = min(agenda.queue, key=attrgetter('service_time'))
        agenda.queue.remove(result)

        return result

    @staticmethod
    def name():
        return "SSTF"
