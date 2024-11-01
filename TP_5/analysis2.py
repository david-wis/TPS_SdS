import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import json
mpl.use('Agg')


ID_IDX = 0
X_IDX = 1
Y_IDX = 2
R_IDX = 3

with open("config/config.json", "r") as f:
    config = json.load(f)
    DT2 = config["DT2"]
    L = config["L"]
    W = config["W"]
    R = config["R"]
    MASS = config["MASS"]
    OBS_RADIUS = config["OBSTACLE_RADIUS"]
    MS = config["MS"]
    A0S = config["A0S"]
    SEEDS = config["SEEDS"]

def plot(xs, ys, x_label, y_label, filename):
    print("Plotting")
    fig, ax = plt.subplots()
    # plt.ticklabel_format(style='sci', axis='both', scilimits=(-5,5))
    ax.scatter(xs, ys)
    ax.set_ylim((0, 1.1 * max(ys)))
    # limit ticks in x axis to only have 5
    # ax.set_xticks(np.linspace(min(xs), max(xs), num=5))
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    plt.tight_layout()
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    print("Saved")
    plt.close()

def plot_lin_regression_error(xs, ys, x_label, y_label, filename):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))

    a = np.sum(np.square(xs))
    b = -2 * np.sum(xs * ys)
    c = np.sum(np.square(ys))

    vx = -b / (2 * a)
    print(f"C* = {vx}")

    vy = a * vx ** 2 + b * vx + c

    # Plot cuadratic curve ax^2 + bx + c
    x = np.linspace(0, 2*vx, 1000)
    y = a * x**2 + b * x + c
    ax.plot(x, y)

    ax.axhline(y=vy, color='lightgrey', linestyle='--')
    ax.axvline(x=vx, color='lightgrey', linestyle='--')

    # zorder
    ax.scatter(vx, vy, color='red', zorder=5)
    ax.annotate(f"({vx:.3f}, {vy:.3g})", (vx, vy), textcoords="offset points", xytext=(0,15), ha='center', color='red')

    ax.set_xlabel("c")
    ax.set_ylabel("E(c)")
    plt.savefig(f"{BASE_PATH}/{filename}_error.png")
    plt.close()

    xs_star = np.linspace(min(xs), max(xs), 100)
    ys_star = vx * xs_star
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    ax.plot(xs_star, ys_star)
    ax.plot(xs, ys, c="red", zorder=5)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.legend([f"$y = {vx:.3g} " + "t$"])
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()
    return vx


if __name__ == "__main__":
    rs = []
    for M in MS:
        print(f"M = {M}")
        qs = []
        for A0 in A0S:
            print(f"\tA0 = {A0}")

            seed = SEEDS[0]
            ts = [0]
            BASE_PATH = f"output/{M}/{A0}/{seed}"
            flux_accum = [0]
            with open(f"{BASE_PATH}/analysis.txt") as f:
                lines = f.readlines()
                for l in lines:
                    t, ids = l.split(":")
                    ids = ids[1:-1].split(", ")
                    flux_accum.append(flux_accum[-1] + len(ids))
                    ts.append(float(t))

            plot(ts, flux_accum, "Tiempo", "Caudal acumulado", "flux_accum_real")

            # find first t > 300
            ts_stationary = np.array(ts[-250:])
            ts_stationary -= ts_stationary[0]
            flux_accum_stationary = np.array(flux_accum[len(flux_accum)-len(ts_stationary):])
            flux_accum_stationary -= flux_accum_stationary[0]
            q = plot_lin_regression_error(ts_stationary, flux_accum_stationary, "Tiempo", "Caudal acumulado", "flux_accum_regression" )
            qs.append(q)
        BASE_PATH = f"output/{M}"
        slope = plot_lin_regression_error(np.array(A0S), np.array(qs), "Aceleración", "Caudal", "a_vs_q")
        # q = a * slope = slope * f/m := f / res
        # slope / m = 1 / res ⇒ res = m / slope
        res = MASS / slope
        rs.append(res)
    BASE_PATH = f"output"
    plot(np.array(MS), np.array(rs), "Numero de obstáculos", "Resistencia", "res_vs_m")

