import matplotlib.pyplot as plt
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib.animation import FuncAnimation
import sys
import math

mpl.use('Agg')
ID_IDX = 0
X_IDX = 1
Y_IDX = 2
R_IDX = 3
VX_IDX = 4
VY_IDX = 5
MARKED_IDX = 6
BASE_PATH = "output"

def plot(xs, ys, filename):
    fig, ax = plt.subplots()
    ax.scatter(xs, ys)
    plt.savefig(f"{BASE_PATH}/{filename}.png")


if __name__ == "__main__":
    LIMIT = 10000
    history = []

    with open(f"{BASE_PATH}/state.txt", "r") as f:
        particles = []
        counter = 0
        for line in f:
            pid, x, y, r, vx, vy, marked = line[:-1].split(" ")
            if pid == '0' and len(particles) != 0:
                history.append(particles)
                particles = []
                if counter > LIMIT:
                    break
                else:
                    counter += 1

            particles.append(np.array([int(pid), float(x), float(y), float(r), float(vx), float(vy), int(marked)]))
        history.append(particles)

    print(len(history))
    PARTITION_SIZE = 100
    INTERVAL = 0.001
    MASS = 1
    L = 0.1
    RADIUS = 0.001
    OBS_RADIUS = 0.005
    partitions = [history[x::PARTITION_SIZE] for x in range(len(history)//PARTITION_SIZE)]
    wall_pressures = []
    obs_pressures = []
    for partition in partitions:
        wall_pressure = 0
        obs_pressure = 0
        for i, s in enumerate(partition):
            marked_particles = [p for p in s if p[MARKED_IDX] == 1]
            if len(marked_particles) == 1:
                p = marked_particles[0]
                if math.sqrt((p[X_IDX] - L / 2)**2 + (p[X_IDX] - L / 2)**2) <= 1.5 * (RADIUS + OBS_RADIUS):
                    obs_pressure += 2 * MASS * math.sqrt(p[VX_IDX] ** 2 + p[VY_IDX] ** 2 ) / (INTERVAL * PARTITION_SIZE * 4 * L)
                else:
                    wall_pressure += 2 * MASS * math.sqrt(p[VX_IDX] ** 2 + p[VY_IDX] ** 2 ) / (INTERVAL * PARTITION_SIZE * 4 * L)
        wall_pressures.append(wall_pressure)
        obs_pressures.append(obs_pressure)
    print(len(partitions))
    plot([INTERVAL * PARTITION_SIZE * i for i in range(len(partitions))], wall_pressures, "wall_pressure")
    plot([INTERVAL * PARTITION_SIZE * i for i in range(len(partitions))], obs_pressures, "obs_pressure")