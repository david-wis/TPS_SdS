import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import json
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
    BASE_PATH = f"output/moving/"

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
    plt.ticklabel_format(style='sci', axis='x', scilimits=(0,0))
    ax.scatter(xs, ys)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.set_ylim((0, 1.1 * max(ys)))
    plt.savefig(f"{BASE_PATH}/{filename}.png")


if __name__ == "__main__":
    LIMIT = 10000

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

    temperatures = [t for t in temperatures]
    plot([INTERVAL * i for i in range(len(temperatures))], temperatures, f"Tiempo ({TIME_UNIT})", f"Energía cinética del sistema ({ENERGY_UNIT})","temperature")

    #MOVING OBSTACLE===========================================================================================================================
    if MOVING_OBSTACLE:
        final_t = 0
        with open(f"{BASE_PATH}/wall_events_1.txt", "r") as f:
            for line in f:
                t, id, _, _, _, _, _ = line[:-1].split(" ")
                if int(id) == 0:
                    final_t = float(t)
                    print("final time",final_t)
                    break

        with open(f"{BASE_PATH}/moving_obstacle_positions_{V}.txt", "r") as f:
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
