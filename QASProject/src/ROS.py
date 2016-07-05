from random import randrange

from strategy import Strategy


class ROS(Strategy):
    @staticmethod
    def get_customer(agenda):
        return agenda.queue.pop(randrange(len(agenda.queue)))

    @staticmethod
    def name():
        return "ROS"
