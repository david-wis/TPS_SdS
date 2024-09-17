import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import math
import json
mpl.use('Agg')
ID_IDX = 0
X_IDX = 1
Y_IDX = 2
R_IDX = 3
VX_IDX = 4
VY_IDX = 5
MARKED_IDX = 6
BASE_PATH = "output"

def plot_regr(xs, ys, filename, std, slope, intercept):
    fig, ax = plt.subplots()
    ax.errorbar(xs, ys, yerr=std, fmt='o')
    ax.plot(xs, [slope * x + intercept for x in xs], color='red')
    plt.savefig(f"{BASE_PATH}/{filename}.png")



def plot(xs, ys, filename):
    fig, ax = plt.subplots()
    ax.scatter(xs, ys)
    plt.savefig(f"{BASE_PATH}/{filename}.png")



if __name__ == "__main__":
    LIMIT = 10000
    with open("./config.json", "r" ) as f:
        config = json.load(f)
        DT = config["DT"]
        INTERVAL = config["INTERVAL"]
        MASS = config["M"]
        L = config["L"]
        RADIUS = config["R"]
        OBS_RADIUS = config["OBSTACLE_RADIUS"]
        EPSILON = 1e-6
        V = int(config["V"])
        MOVING_OBSTACLE = config["MOVING_OBSTACLE"]

    #history ==================================================================================================================================
    history = []
    with open(f"{BASE_PATH}/state_{V}.txt", "r") as f:
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

    temperatures = []
    for i, state in enumerate(history):
        if len(state) == 0:
            continue
        temperatures.append(MASS * sum([(p[VX_IDX] ** 2 + p[VY_IDX] ** 2) for p in state]) / (2 * len(state)))

    temperatures = [round(t, 2) for t in temperatures]
    plot([INTERVAL * i for i in range(len(temperatures))], temperatures, "temperature")

    #OBSTACLE==================================================================================================================================
    obs_pressures = {}
    obs_first_collisions = dict()
    obst_evs = []
    with (open(f"{BASE_PATH}/obstacle_events_{V}.txt", "r") as f):
        for line in f:
            t, id, x, y, vx, vy = line[:-1].split(" ")
            obst_evs.append((float(t), int(id), float(x), float(y), float(vx), float(vy)))

    for i, s in enumerate(obst_evs):
        t, id, x, y, vx, vy = s
        idx = int(t // DT)
        if idx not in obs_pressures:
            obs_pressures[idx] = 0
        def get_normal_velocity(x, y, vx, vy):
            return abs((x - L / 2) * vx + (y - L / 2) * vy) / math.sqrt((x - L / 2) ** 2 + (y - L / 2) ** 2)
        obs_pressures[idx] += 2 * MASS * get_normal_velocity(x, y, vx, vy) / (DT * math.pi * OBS_RADIUS * 2)

        if id not in obs_first_collisions.keys():
            obs_first_collisions[id] = t

    plot([DT * i for i in range(len(obs_pressures))], list(obs_pressures.values()), "obs_pressure")
    times = obs_first_collisions.values()
    times = sorted(times)
    plot(times, [i for i in range(len(times))], "first_collisions")
    plot([e[0] for e in obst_evs], [i for i in range(len(obst_evs))], "all_collisions")

    #WALL========================================================================================================================================
    wall_pressures = {}
    wall_evs = []
    with (open(f"{BASE_PATH}/wall_events_{V}.txt", "r") as f):
        for line in f:
            t, id, x, y, vx, vy, dirs = line[:-1].split(" ")
            wall_evs.append((float(t), int(id), float(x), float(y), float(vx), float(vy), dirs))

    for i, s in enumerate(wall_evs):
        t, id, x, y, vx, vy, dirs = s
        idx = int(t // DT)
        if idx not in wall_pressures:
            wall_pressures[idx] = 0
        def get_velocity_by_wall(x, y, vx, vy, r, dirs):
            if "x" in dirs:
                return abs(vx)
            elif "y" in dirs:
                return abs(vy)
            print("zero")
            return 0
        wall_pressures[idx] += 2 * MASS * get_velocity_by_wall(x, y, vx, vy, RADIUS, dirs) / (DT * 4 * L)

    plot([DT * i for i in range(len(wall_pressures))], list(wall_pressures.values()), "wall_pressure")


    #MOVING OBSTACLE===========================================================================================================================
    if MOVING_OBSTACLE:
        msds = dict()
        msdsdevs = dict()
        with open(f"{BASE_PATH}/moving_obstacle_positions_{V}.txt", "r") as f:
            positions = []
            for line in f:
                t, x, y = line[:-1].split(" ")
                positions.append((float(t), float(x), float(y)))

        displacements = dict()
        for p in positions:
            t, x, y = p
            idx = int(t // DT)

            # Calculate the displacement squared
            displacement = ((x - L / 2) ** 2 + (y - L / 2) ** 2) / DT

            # Accumulate displacement squared values
            if idx not in displacements:
                displacements[idx] = []
            displacements[idx].append(displacement)

        times = []
        mean_msd = []

        for idx in sorted(displacements):  # Ensure time steps are sorted
            msds[idx] = np.mean(displacements[idx])
            msdsdevs[idx] = np.std(displacements[idx])
            times.append(idx * DT)  # Convert the index back to time
            mean_msd.append(msds[idx])

        # Perform linear regression (fit a line to the data)
        slope, intercept = np.polyfit(times, mean_msd, 1)

        plot_regr([DT * (i + 1) for i in range(len(msds))], msds.values(), "msd", msdsdevs.values(), slope, intercept)




