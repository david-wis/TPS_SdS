import matplotlib.pyplot as plt
import matplotlib as mpl
import math
import json
import numpy as np
from matplotlib.ticker import FuncFormatter
mpl.use('Agg')
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

ID_IDX = 0
X_IDX = 1
Y_IDX = 2
R_IDX = 3
VX_IDX = 4
VY_IDX = 5
MARKED_IDX = 6
BASE_PATH = "output"
PRESSURE_UNIT = "Kg / $s^2$"
TIME_UNIT = "s"
ENERGY_UNIT = "J"

def plot(xs, ys, x_label, y_label, filename):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='both', scilimits=(-5,5))
    ax.plot(xs, ys)
    ax.scatter(xs, ys)
    ax.set_ylim((0, 1.1 * max(ys)))
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.yaxis.set_major_formatter(formatter)
    plt.tight_layout()
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

def plot_aggregated(xss, yss, ls, x_label, y_label, filename):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='both', scilimits=(-5,5))
    for xs, ys, l in zip(xss, yss, ls):
        ax.plot(xs, ys, label=l)
        ax.scatter(xs, ys, label=l)
    ax.set_ylim((0, 1.1 * max([max(ys) for ys in yss])))
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.yaxis.set_major_formatter(formatter)
    ax.legend()
    # remove legend for scatter
    handles, labels = ax.get_legend_handles_labels()
    ax.legend(handles=handles[::2], labels=labels[::2])

    plt.tight_layout()
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

with open("config/static_config.json", "r") as f:
    config = json.load(f)
    DT = config["DT"]
    INTERVAL = config["INTERVAL"]
    MASS = config["M"]
    L = config["L"]
    RADIUS = config["R"]
    OBS_RADIUS = config["OBSTACLE_RADIUS"]
    EPSILON = 1e-6
    N = config["N"]
    MOVING_OBSTACLE = config["MOVING_OBSTACLE"]

def analyze_velocity(v):
    #history ==================================================================================================================================
    history = []
    with open(f"{BASE_PATH}/{v}/state.txt", "r") as f:
        particles = []
        counter = 0
        for line in f:
            pid, x, y, r, vx, vy, marked = line[:-1].split(" ")
            if pid == '0' and len(particles) != 0:
                history.append(particles)
                particles = []
                counter += 1
            particles.append(np.array([int(pid), float(x), float(y), float(r), float(vx), float(vy), int(marked)]))
        history.append(particles)
    print(len(history))

    temps = []
    for i, state in enumerate(history):
        if len(state) == 0:
            continue
        temps.append(MASS * sum([(p[VX_IDX] ** 2 + p[VY_IDX] ** 2) for p in state]) / (2 * len(state)))

    # temperatures = [t for t in temperatures]
    # plot([INTERVAL * i for i in range(len(temperatures))], temperatures, f"Tiempo ({TIME_UNIT})", f"Energía cinética del sistema ({ENERGY_UNIT})",f"{v}/temperature")

   #WALL========================================================================================================================================
    wall_pressures = {}
    wall_evs = []
    with (open(f"{BASE_PATH}/{v}/wall_events.txt", "r") as f):
        for line in f:
            t, id, x, y, vx, vy, dirs = line[:-1].split(" ")
            wall_evs.append((float(t), int(id), float(x), float(y), float(vx), float(vy), dirs))

    for i, s in enumerate(wall_evs):
        t, id, x, y, vx, vy, dirs = s
        idx = int(t // DT)
        if idx not in wall_pressures:
            wall_pressures[idx] = 0
        def get_velocity_by_wall(x, y, vx, vy, r, dirs):
            total_v = 0
            # if dirs == "xy": print("double")
            if "x" in dirs:
                total_v += abs(vx)
            elif "y" in dirs:
                total_v += abs(vy)
            # print("zero")
            return total_v
        wall_pressures[idx] += 2 * MASS * get_velocity_by_wall(x, y, vx, vy, RADIUS, dirs) / (DT * 4 * L)

    # plot([DT * i for i in range(len(wall_pressures))], list(wall_pressures.values()), f"Tiempo ({TIME_UNIT})",f"Presión sobre las paredes ({PRESSURE_UNIT})",f"{v}/wall_pressure")

    #OBSTACLE==================================================================================================================================
    obs_pressures = {}
    obs_first_collisions = dict()
    obst_evs = []
    with (open(f"{BASE_PATH}/{v}/obstacle_events.txt", "r") as f):
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

    # plot([DT * i for i in range(len(wall_pressures))], list(wall_pressures.values()), f"Tiempo ({TIME_UNIT})",f"Presión sobre las paredes ({PRESSURE_UNIT})",f"{v}/wall_pressure")
    # plot([DT * i for i in range(len(obs_pressures))], list(obs_pressures.values()), f"Tiempo ({TIME_UNIT})",f"Presión sobre el obstáculo ({PRESSURE_UNIT})", f"{v}/obs_pressure")

    plot_aggregated([[DT * i for i in range(len(wall_pressures))], [DT * i for i in range(len(obs_pressures))]],
                    [list(wall_pressures.values()), list(obs_pressures.values())], ["Paredes", "Obstáculo"], f"Tiempo ({TIME_UNIT})",f"Presión ({PRESSURE_UNIT})",f"{v}/pressure")



    first_times = obs_first_collisions.values()
    first_times = sorted(first_times)
    first_collisions = [i for i in range(len(first_times))]
    plot(first_times, first_collisions, f"Tiempo ({TIME_UNIT})", "Cantidad de primeras colisiones", f"{v}/first_collisions")
    all_collisions = [i for i in range(len(obst_evs))]
    all_times = [e[0] for e in obst_evs]
    plot(all_times, all_collisions, f"Tiempo ({TIME_UNIT})", "Cantidad de colisiones", f"{v}/all_collisions", )

    time_1000 = obst_evs[1000][0]
    time_half = first_times[len(first_times) // 2]
    time_all = first_times[-1]
    return (
            (np.mean(np.array(list(wall_pressures.values()))) + np.mean(np.array(list(obs_pressures.values())))) / 2,
            wall_pressures, obs_pressures,
            first_times, first_collisions,
            all_times, all_collisions,
            time_1000, time_half, time_all,
            temps
    )


if __name__ == "__main__":
    velocities = [1,3,6,10]
    temperatures = [N * MASS * 0.5 * v ** 2 for v in velocities]
    pressures = []
    times_10000 = []
    times_half = []
    times_all = []
    wpss = []
    opss = []
    ftss = []
    fcss = []
    atss = []
    acss = []
    tempss = []
    for v in velocities:
        print(v)
        (mean_pressure, wall_pressures, obs_pressures,
         first_times, first_collisions,
         all_times, all_collisions,
         time_10000, time_half, time_all, temps) = analyze_velocity(v)
        wpss.append(wall_pressures)
        opss.append(obs_pressures)
        fcss.append(first_collisions)
        ftss.append(first_times)
        acss.append(all_collisions)
        atss.append(all_times)
        pressures.append(mean_pressure)
        times_10000.append(time_10000)
        times_half.append(time_half)
        times_all.append(time_all)
        tempss.append(temps)
    plot_aggregated([[DT * i for i in range(len(wps))] for wps in wpss], [list(wps.values()) for wps in wpss], velocities, f"Tiempo ({TIME_UNIT})",f"Presión sobre las paredes ({PRESSURE_UNIT})",f"wall_pressure")
    plot_aggregated([[DT * i for i in range(len(ops))] for ops in opss], [list(ops.values()) for ops in opss], velocities, f"Tiempo ({TIME_UNIT})",f"Presión sobre el obstáculo ({PRESSURE_UNIT})", f"obs_pressure")
    plot_aggregated(ftss, fcss, velocities, f"Tiempo ({TIME_UNIT})", "Cantidad de primeras colisiones", f"first_collisions")
    plot_aggregated(atss, acss, velocities, f"Tiempo ({TIME_UNIT})", "Cantidad de colisiones", f"all_collisions", )
    plot_aggregated([[INTERVAL * i for i in range(len(temps))] for temps in tempss], tempss, velocities, f"Tiempo ({TIME_UNIT})", f"Energía cinética promedio del sistema ({ENERGY_UNIT})",f"temperature")

    plot(temperatures, pressures, f"Energía cinética promedio del sistema ({ENERGY_UNIT})",f"Presión del sistema ({ENERGY_UNIT})","temperature_pressure")

    plot(temperatures, times_10000, f"Energía cinética promedio del sistema ({ENERGY_UNIT})",f"Tiempo para 1000 colisiones ({TIME_UNIT})","temperature_time_10000")
    plot(temperatures, times_half, f"Energía cinética promedio del sistema ({ENERGY_UNIT})",f"Tiempo para el 50% de las primeras partículas ({TIME_UNIT})","temperature_time_half")
    plot(temperatures, times_all, f"Energía cinética del sistema ({ENERGY_UNIT})",f"Tiempo para el 100% de las primeras partículas ({TIME_UNIT})","temperature_time_all")

