import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib.ticker import FuncFormatter
import json
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

with open("config/config1.json", "r") as f:
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
    ax.plot(xs, ys, linewidth=0.5)
    if scatter:
        ax.scatter(xs, ys)
    ax.set_xlabel(x_label)
    if logarithmic:
        ax.set_xscale('log')
        ax.set_yscale('log')
    ax.set_ylabel(y_label)
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

def plot_aggregated(xs, yss, ls, x_label, y_label, filename, logarithmic=False, scatter=False, styled=False):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    linestyles = ['-', '-.', '--', ':'] if styled else ['-'] * len(yss)
    for ys, l, style in zip(yss, ls, linestyles):
        ax.plot(xs, ys, label=l, linestyle=style)
        if scatter:
            ax.scatter(xs, ys, label=l)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    handles, labels = ax.get_legend_handles_labels()
    if scatter:
        ax.legend(handles=handles[::2], labels=labels[::2])
    else:
        ax.legend()
    if logarithmic:
        ax.set_xscale('log')
        ax.set_yscale('log')
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

def analytic_x(t):
    return R0 * np.exp(-G*t/(2*M)) * np.cos(np.sqrt(K/M - G**2/(4*M**2))*t)

def analytic_v(t):
    return (-(G / (2 * M)) * R0 * np.exp(-G * t / (2 * M)) * np.cos(np.sqrt(K / M - (G / (2 * M)) ** 2) * t) - R0 * np.exp(-G * t / (2 * M)) * np.sin(np.sqrt(K / M - (G / (2 * M)) ** 2) * t) * np.sqrt(K / M - (G / (2 * M)) ** 2))

if __name__ == "__main__":
    # Load data
    integrators = ["analytic", "verlet", "beeman", "gpc"]
    msep_by_integrators = { i : [] for i in integrators[1:]}
    for dt in DTS:
        BASE_PATH = f"output/1/{dt:.2g}"
        tdata = np.loadtxt(f"{BASE_PATH}/state_{integrators[1]}.txt")
        ts = tdata[:, 0]
        analytic_xs = np.array([ analytic_x(t) for t in ts])
        xss = []
        for integrator in integrators[1:]:
            data = np.loadtxt(f"{BASE_PATH}/state_{integrator}.txt")
            xs = data[:, 1]
            xss.append(xs)
            plot_aggregated(ts, [analytic_xs, xs], ["analytic", integrator], "Tiempo (s)", "Posición (m)", f"positions_{integrator}")
            msep = np.mean((analytic_xs - xs)**2)
            msep_by_integrators[integrator].append(msep)
            print(f"MSEP {integrator} dt {dt:.2g}: {msep}")
        plot_aggregated(ts, [analytic_xs] + xss, integrators, "Tiempo (s)", "Posición (m)", f"positions_all", styled=True)


        # plot(DTS, msep_by_integrators[integrator], "dt (s)", "MSEP", f"msep_{integrator}", True, True)
    BASE_PATH = "output/1"
    plot_aggregated(DTS, [msep_by_integrators[i] for i in integrators[1:]], integrators[1:], "dt (s)", "MSEP", f"msep_aggregated", True, True)


