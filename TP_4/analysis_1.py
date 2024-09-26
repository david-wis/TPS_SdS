import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib.ticker import FuncFormatter
import json
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

with open("config/config.json", "r") as f:
    config = json.load(f)
    DTS = config["dts"]
    M = config["m"]
    K = config["k"]
    G = config["g"]
    TF = config["tf"]
    R0 = config["r0"]

def plot(xs, ys, x_label, y_label,filename, logarithmic=False, scatter=False):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    ax.plot(xs, ys)
    if scatter:
        ax.scatter(xs, ys)
    ax.set_xlabel(x_label)
    if logarithmic:
        ax.set_xscale('log')
        ax.set_yscale('log')
    ax.set_ylabel(y_label)
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

def plot_aggregated(xs, yss, ls, x_label, y_label, filename, logarithmic=False, scatter=False):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    for ys, l in zip(yss, ls):
        ax.plot(xs, ys, label=l)
        if scatter:
            ax.scatter(xs, ys, label=l)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    handles, labels = ax.get_legend_handles_labels()
    ax.legend(handles=handles[::2], labels=labels[::2])
    if logarithmic:
        ax.set_xscale('log')
        ax.set_yscale('log')
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

def analytic_x(t):
    return R0 * np.exp(-G*t/(2*M)) * np.cos(np.sqrt(K/M - G**2/(4*M**2))*t)

def analytic_v(t):
    return (-(G / (2 * M)) * R0 * np.exp(-G * t / (2 * M)) * np.cos(np.sqrt(K / M - np.pow(G / (2 * M), 2)) * t) - R0 * np.exp(-G * t / (2 * M)) * np.sin(np.sqrt(K / M - np.pow(G / (2 * M), 2)) * t) * np.sqrt(K / M - np.pow(G / (2 * M), 2)));

if __name__ == "__main__":
    # Load data
    integrators = ["analytic", "verlet", "beeman", "gpc"]
    msep_by_integrators = { i : [] for i in integrators[1:]}
    msev_by_integrators = { i : [] for i in integrators[1:]}
    for integrator in integrators[1:]:
        for dt in DTS:
            BASE_PATH = f"output/{dt:.2g}"
            data = np.loadtxt(f"{BASE_PATH}/state_{integrator}.txt")
            ts = data[:, 0]
            xs = data[:, 1]
            analytic_xs = np.array([ analytic_x(t) for t in ts])
            plot_aggregated(ts, [analytic_xs, xs], ["analytic", integrator], "Tiempo (s)", "Posición (m)", f"positions_{integrator}")
            msep = np.mean((analytic_xs - xs)**2)
            msep_by_integrators[integrator].append(msep)
            print(f"MSEP {integrator} dt {dt:.2g}: {msep}")

            vs = data[:, 2]
            analytic_vs = np.array([ analytic_v(t) for t in ts])
            plot_aggregated(ts, [analytic_vs, vs], ["analytic", integrator], "Tiempo (s)", "Velocidad (m/s)", f"velocities_{integrator}")
            msev = np.mean((analytic_vs - vs)**2)
            msev_by_integrators[integrator].append(msev)
            print(f"MSEV {integrator} dt {dt:.2g}: {msev}")

            # plot(ts, analytic_vs, "Tiempo (s)", "Velocidad (m/s)", f"velocities_analytic")
            np.savetxt(f"{BASE_PATH}/state_analytic.txt", np.array([ts, analytic_xs, analytic_vs]).T, delimiter=" ")
        BASE_PATH = "output"
        plot(DTS, msep_by_integrators[integrator], "dt (s)", "MSEP", f"msep_{integrator}", True, True)
        plot(DTS, msev_by_integrators[integrator], "dt (s)", "MSEV", f"msev_{integrator}", True, True)
    plot_aggregated(DTS, [msep_by_integrators[i] for i in integrators[1:]], integrators[1:], "dt (s)", "MSEP", f"msep_aggregated", True, True)
    plot_aggregated(DTS, [msev_by_integrators[i] for i in integrators[1:]], integrators[1:], "dt (s)", "MSEV", f"msev_aggregated", True, True)

