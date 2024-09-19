import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import json
import os
mpl.use('Agg')
ID_IDX = 0
X_IDX = 1
Y_IDX = 2
R_IDX = 3
VX_IDX = 4
VY_IDX = 5
MARKED_IDX = 6
TIME_UNIT = "s"
ENERGY_UNIT = "J"

with open("config/moving_config.json", "r") as f:
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
    BASE_PATH = f"output/moving"

def plot_regr(xs, ys, x_label, y_label, filename, std, slope, intercept):
    fig, ax = plt.subplots()
    ax.errorbar(xs, ys, yerr=std, fmt='o')
    ax.plot(xs, [slope * x + intercept for x in xs], color='red')
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    #include regression in legend
    ax.legend([f"y = {slope:.2g}t + {intercept:.2g}"])
    plt.tight_layout()
    plt.savefig(f"{BASE_PATH}/{filename}.png")



def plot(xs, ys, x_label, y_label,filename):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    ax.scatter(xs, ys)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.set_ylim((0, 1.1 * max(ys)))
    plt.savefig(f"{BASE_PATH}/{filename}.png")


if __name__ == "__main__":
    LIMIT = 10000
    seeds = [f for f in os.listdir(BASE_PATH)]
    print(seeds)
    input()
    for s in seeds:
        SEED_PATH = BASE_PATH + "/" + s
        #MOVING OBSTACLE===========================================================================================================================
        if MOVING_OBSTACLE:
            final_t = 0
            with open(f"{SEED_PATH}/wall_events.txt", "r") as f:
                for line in f:
                    t, id, _, _, _, _, _ = line[:-1].split(" ")
                    if int(id) == 0:
                        final_t = float(t)
                        print("final time",final_t)
                        break

            with open(f"{SEED_PATH}/moving_obstacle_positions.txt", "r") as f:
                positions = []
                for line in f:
                    t, x, y = line[:-1].split(" ")
                    positions.append((float(t), float(x), float(y)))
                    if float(t) >= final_t:
                        break

            msds = dict()
            msdsdevs = dict()
            displacements = dict()
            for p in positions:
                t, x, y = p
                idx = int(t // DT)

                # Calculate the displacement squared
                displacement = ((x - L / 2) ** 2 + (y - L / 2) ** 2)

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

            plot_regr([DT * (i + 1) for i in range(len(msds))], msds.values(), f"Tiempo ({TIME_UNIT})", "Desplazamiento cuadratico medio ($m^2$)", "msd", msdsdevs.values(), slope, intercept)
