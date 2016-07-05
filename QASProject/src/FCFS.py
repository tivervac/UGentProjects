from strategy import Strategy


class FCFS(Strategy):
    @staticmethod
    def get_customer(agenda):
        return agenda.queue.pop()

    @staticmethod
    def name():
        return "FCFS"
