from FCFS import FCFS
from LCFS import LCFS
from ROS import ROS
from SSTF import SSTF

batches = 50

if __name__ == '__main__':
    strategies = [FCFS, LCFS, SSTF, ROS]

    done = []
    for s in strategies:
        done.append(s(10000))
        done[-1].run()

    plot_queue_content(done)
    plot_waiting_times(done)
    plot_sojourn_times(done)

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
