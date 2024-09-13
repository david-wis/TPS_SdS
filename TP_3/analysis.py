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

    obst_evs = []
    with (open(f"{BASE_PATH}/obstacle_events.txt", "r") as f):
        for line in f:
            t, x, y, vx, vy = line[:-1].split(" ")
            obst_evs.append((float(t), float(x), float(y), float(vx), float(vy)))

    wall_evs = []
    with (open(f"{BASE_PATH}/wall_events.txt", "r") as f):
        for line in f:
            t, x, y, vx, vy = line[:-1].split(" ")
            wall_evs.append((float(t), float(x), float(y), float(vx), float(vy)))

    print(len(history))
    INTERVAL = 0.1
    MASS = 1
    L = 0.1
    RADIUS = 0.001
    OBS_RADIUS = 0.005
    EPSILON = 1e-6

    # Group by partition, considering time interval (the first element of the tuple)
    wall_pressures = {}
    obs_pressures = {}
    for i, s in enumerate(obst_evs):
        t, x, y, vx, vy = s
        idx = int(t // INTERVAL)
        if idx not in obs_pressures:
            obs_pressures[idx] = 0
        def get_normal_velocity(x, y, vx, vy):
            return abs((x - L / 2) * vx + (y - L / 2) * vy) / math.sqrt((x - L / 2) ** 2 + (y - L / 2) ** 2)
        obs_pressures[idx] += 2 * MASS * get_normal_velocity(x, y, vx, vy) / (INTERVAL * math.pi * OBS_RADIUS * 2)

    for i, s in enumerate(wall_evs):
        t, x, y, vx, vy = s
        idx = int(t // INTERVAL)
        if idx not in wall_pressures:
            wall_pressures[idx] = 0
        def get_velocity_by_wall(x, y, vx, vy, r):
            if x - r <= EPSILON or x + r >= L - EPSILON:
                return abs(vx)
            if y - r <= EPSILON or y + r >= L - EPSILON:
                return abs(vy)
            return 0
        wall_pressures[idx] += 2 * MASS * get_velocity_by_wall(x, y, vx, vy, RADIUS) / (INTERVAL * 4 * L)

    print(len(wall_pressures))

    plot([INTERVAL * i for i in range(len(wall_pressures))], list(wall_pressures.values()), "wall_pressure")
    plot([INTERVAL * i for i in range(len(obs_pressures))], list(obs_pressures.values()), "obs_pressure")


    # partitions = [history[x::PARTITION_SIZE] for x in range(len(history)//PARTITION_SIZE)]
    # wall_pressures = []
    # obs_pressures = []
    # for partition in partitions:
    #     wall_pressure = 0
    #     obs_pressure = 0
    #     for i, s in enumerate(partition):
    #         marked_particles = [p for p in s if p[MARKED_IDX] == 1]
    #         if len(marked_particles) == 1:
    #             p = marked_particles[0]
    #             if math.sqrt((p[X_IDX] - L / 2)**2 + (p[X_IDX] - L / 2)**2) <= 1.5 * (RADIUS + OBS_RADIUS):
    #                 def get_normal_velocity(x, y, vx, vy):
    #                     return abs((x - L / 2) * vx + (y - L / 2) * vy) / math.sqrt((x - L / 2) ** 2 + (y - L / 2) ** 2)
    #                 obs_pressure += 2 * MASS * get_normal_velocity(p[X_IDX], p[Y_IDX], p[VX_IDX], p[VY_IDX]) / (INTERVAL * PARTITION_SIZE * math.pi * OBS_RADIUS * 2)
    #             else:
    #                 def get_velocity_by_wall(x, y, vx, vy, r):
    #                     if x - r <= EPSILON or x + r >= L - EPSILON:
    #                         return abs(vx)
    #                     if y - r <= EPSILON or y + r >= L - EPSILON:
    #                         return abs(vy)
    #                     return 0
    #                 wall_pressure += 2 * MASS * get_velocity_by_wall(p[X_IDX], p[Y_IDX], p[VX_IDX], p[VY_IDX], RADIUS) / (INTERVAL * PARTITION_SIZE * 4 * L)
    #     wall_pressures.append(wall_pressure)
    #     obs_pressures.append(obs_pressure)
    # print(len(partitions))
    # plot([INTERVAL * PARTITION_SIZE * i for i in range(len(partitions))], wall_pressures, "wall_pressure")
    # plot([INTERVAL * PARTITION_SIZE * i for i in range(len(partitions))], obs_pressures, "obs_pressure")