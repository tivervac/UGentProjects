import matplotlib.pyplot as plt

from FCFS import FCFS
from LCFS import LCFS
from ROS import ROS
from SSTF import SSTF

base = "../figures/"
batches = 50


def plot_batch_means(batch_means, strat):
    plt.figure()
    plt.plot(range(0, batches), [b[0] for b in batch_means], 'r', label="Low")
    plt.plot(range(0, batches), [b[1] for b in batch_means], 'b', label="Estimated Sample mean")
    plt.plot(range(0, batches), [b[2] for b in batch_means], 'g', label="High")
    plt.xlabel("K .10^3")
    plt.ylabel("E[Q]")
    plt.legend(loc=2)
    plt.title("Confidence interval")
    plt.savefig(base + strat.name() + "_batch_mean_ci.eps")


def plot_batch_vars(batch_means, strats):
    plt.figure()
    for k in range(0, len(strats)):
        plt.plot(range(0, batches), [b[3] for b in batch_means[k]],
                 label=strats[k].name())

    plt.xlabel("K . 10^3")
    plt.ylabel("Sample variance of batches")
    plt.legend(loc=1)
    plt.title("Sample variance of batches versus number of batches")
    plt.savefig(base + "batch_mean_var.eps")


def plot_queue_content(strats, aarr=1, barr=1, aser=1, bser=1, c=1):
    plt.figure()
    for st in strats:
        plt.plot(st.mon_events[:-10], st.queue_contents[:-10], label=st.name())
    plt.xlabel("time (s)")
    plt.ylabel("Q(t)")
    plt.legend(loc=2)
    plt.gca().set_xlim([0, 16000])
    plt.title("Queue content at observed times\naA={0} bA={1} aS={2} bS={3} c={4}".format(aarr, barr, aser, bser, c))
    plt.savefig(base + "queue_content" + str(aarr) + str(barr) + str(aser) + str(bser) + str(c) + ".eps")


def plot_waiting_times(strats, aarr=1, barr=1, aser=1, bser=1, c=1):
    plt.figure()
    for st in strats:
        plt.plot(st.mon_events[:-10], st.avg_waiting_time[:-10], label=st.name())
    plt.xlabel("time (s)")
    plt.ylabel("E[W]")
    plt.legend(loc=2)
    plt.gca().set_xlim([0, 16000])
    plt.title(
        "Estimated waiting times at observed times\naA={0} bA={1} aS={2} bS={3} c={4}".format(aarr, barr, aser, bser,
                                                                                              c))
    plt.savefig(base + "waiting_times" + str(aarr) + str(barr) + str(aser) + str(bser) + str(c) + ".eps")


def plot_sojourn_times(strats, aarr=1, barr=1, aser=1, bser=1, c=1):
    plt.figure()
    for st in strats:
        plt.plot(st.mon_events[:-10], st.avg_sojourn_time[:-10], label=st.name())
    plt.xlabel("time (s)")
    plt.ylabel("E[D]")
    plt.legend(loc=2)
    plt.gca().set_xlim([0, 16000])
    plt.title(
        "Estimated sojourn times at observed times\naA={0} bA={1} aS={2} bS={3} c={4}".format(aarr, barr, aser, bser,
                                                                                              c))
    plt.savefig(base + "sojourn_times" + str(aarr) + str(barr) + str(aser) + str(bser) + str(c) + ".eps")


if __name__ == '__main__':
    strategies = [FCFS, LCFS, SSTF, ROS]

    done = []
    for s in strategies:
        done.append(s(10000, 4, 4, 1, 1, 1))
        done[-1].run()

    plot_queue_content(done, 4, 4, 1, 1, 1)
    plot_waiting_times(done, 4, 4, 1, 1, 1)
    plot_sojourn_times(done, 4, 4, 1, 1, 1)

    means2 = []
    for s in strategies:
        means = [(0, 0, 0, 0)]
        for i in range(1, batches):
            strategy = s(i * 1000)
            strategy.run()
            means.append(strategy.batch_means(2.58))

        means2.append(means)
        plot_batch_means(means, s)
    plot_batch_vars(means2, done)
