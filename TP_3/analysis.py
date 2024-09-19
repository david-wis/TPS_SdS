import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import json
import os
from matplotlib.ticker import FuncFormatter

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
D2_UNIT = "$m^2$"

def scientific_notation(x, pos):
    if x == 0:
        return '0'
    exponent = int(np.floor(np.log10(abs(x))))
    mantissa = x / (10 ** exponent)
    if abs(exponent) >= 5:
        return r'${:.2g} \times 10^{{{}}}$'.format(mantissa, exponent)
    else:
        if x - int(x) == 0:
            return str(int(x))
        return f"{x:.2g}"
formatter = FuncFormatter(scientific_notation)

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
    ax.legend([f"y = {slope:.3g} t + {intercept:.2g}"])
    plt.tight_layout()
    plt.savefig(f"{BASE_PATH}/{filename}.png")

def plot_aggregated(xss, yss, ls, x_label, y_label, filename):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='both', scilimits=(-5,5))
    for xs, ys, l in zip(xss, yss, ls):
        ax.plot(xs, ys, label=l)
    ax.set_ylim((0, 1.1 * max([max(ys) for ys in yss])))
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.yaxis.set_major_formatter(formatter)
    ax.legend()
    plt.tight_layout()
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

# def plot(xs, ys, x_label, y_label,filename):
#     fig, ax = plt.subplots()
#     plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
#     ax.plot(xs, ys)
#     ax.set_xlabel(x_label)
#     ax.set_ylabel(y_label)
#     ax.set_ylim((0, 1.1 * max(ys)))
#     plt.savefig(f"{BASE_PATH}/{filename}.png")


if __name__ == "__main__":
    LIMIT = 10000
    seeds = [f for f in os.listdir(BASE_PATH) if os.path.isdir(os.path.join(BASE_PATH, f))]
    print(seeds)
    final_ts = []
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
                        final_ts.append(final_t)
                        break
    final_t = np.mean(final_ts)

    msdss = []
    for s in seeds:
        SEED_PATH = BASE_PATH + "/" + s
        with open(f"{SEED_PATH}/moving_obstacle_positions.txt", "r") as f:
            displacements = []
            for line in f:
                t, x, y = line[:-1].split(" ")
                if float(t) >= len(displacements) * DT:
                    displacements.append((float(x) - L/2)**2 + (float(y) - L/2)**2)
                    if float(t) >= final_t:
                        break
        msdss.append(np.array(displacements))
    msdss_arr = np.array(msdss)
    #calculate msdss mean
    msds = np.mean(msdss_arr, axis=0)
    stds = np.std(msdss_arr, axis=0)
    times = [i * DT for i in range(len(msds))]

    # Perform linear regression
    slope, intercept = np.polyfit(times , msds, 1)
    plot_regr(times, msds, f"Time ({TIME_UNIT})", f"Mean Squared Displacement ({D2_UNIT})", "msd", stds, slope, intercept)
    print(slope/4)
    plot_aggregated([times for i in range(len(seeds))], [msds for msds in msdss], seeds, f"Time ({TIME_UNIT})", f"Mean Squared Displacement ({D2_UNIT})", "msd_agg")
