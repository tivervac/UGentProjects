from strategy import Strategy


class LCFS(Strategy):
    @staticmethod
    def get_customer(agenda):
        return agenda.queue.pop(0)

    @staticmethod
    def name():
        return "LCFS"
