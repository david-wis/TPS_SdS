import os

import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib.ticker import FuncFormatter
import json
import math
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

with open("config/config2.json", "r") as f:
    config = json.load(f)
    M = config["m"]
    KS = config["ks"]
    A = config["A"]
    L0 = config["l0"]
    N = config["N"]+1
    DT2 = config["dt2"]
    TF = config["tf"]
    WS = config["ws"]

def plot(xs, ys, x_label, y_label,filename, logarithmic=False, scatter=False):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    ax.plot(xs, ys)
    if scatter:
        ax.scatter(xs, ys)
    ax.set_xlabel(x_label)
    if logarithmic:
        ax.set_yscale('log')
    ax.set_ylabel(y_label)
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

def plot_aggregated(xss, yss, ls, x_label, y_label, filename, legend_title=None, logarithmic=False, scatter=False):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    for xs, ys, l in zip(xss, yss, ls):
        ax.plot(xs, ys, label=l)
        if scatter:
            ax.scatter(xs, ys, label=l)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    handles, labels = ax.get_legend_handles_labels()
    ax.legend(handles=handles[::2], labels=labels[::2], title = legend_title)
    if logarithmic:
        ax.set_yscale('log')
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()


if __name__ == "__main__":
    # Load data
    max_yss = []
    wss = []
    for k in KS:
        print(f"K:{k}")
        max_ys = []
        BASE_PATH = f'output/2/{k}'
        ws = sorted([ float(d) for d in os.listdir(BASE_PATH) if os.path.isdir(f"{BASE_PATH}/{d}")])
        wss.append(ws)
        for w in ws:
            print(f"W:{w}")
            BASE_PATH = f'output/2/{k}/{w}'
            data = np.loadtxt(f'{BASE_PATH}/max.txt')
            ts = data[:, 0]
            ys = np.abs(data[:, 1])
            max_ys.append(np.max(ys))
            plot(ts, ys, "Tiempo (s)", "Posición máxima (m)", f"max_position_{w}", scatter=False)
        BASE_PATH = f'output/2/{k}'
        plot(ws, max_ys, "$\omega\ (s^{-1})$", "Posición máxima (m)", f"max_position", scatter=True)
        max_yss.append(max_ys)

    BASE_PATH = "output/2"
    plot_aggregated(wss, max_yss, [f"{k} kg/$s^2$" for k in KS], "$\omega\ (s^{-1})$", "Posición máxima (m)", f"max_position_aggregated", legend_title="k", scatter=True)

    max_ws = [max([yw for yw in zip(ys, ws)])[1] for ys, ws in zip(max_yss, wss)]
    plot([math.sqrt(k) for k in KS], max_ws, "$k^{1/2}\ (Kg/s)$", "$\omega_0\ (s^{-1})$", f"max_w", scatter=True)


