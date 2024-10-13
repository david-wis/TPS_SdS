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

def plot(xs, ys, x_label, y_label,filename, logarithmic=False, scatter=False, plot=True, s=20, xticks=False):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    if plot:
        ax.plot(xs, ys)
        ax.set_xticks(xs)
        ax.set_xticklabels(xs, rotation=45)
    if scatter:
        ax.scatter(xs, ys, s=s)

    ax.set_xlabel(x_label)
    if logarithmic:
        ax.set_yscale('log')
    ax.set_ylabel(y_label)
    plt.tight_layout()
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

def plot_aggregated(xss, yss, ls, x_label, y_label, filename, legend_title=None, logarithmic=False, scatter=False, plot=True, s=20):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    for xs, ys, l in zip(xss, yss, ls):
        if plot:
            ax.plot(xs, ys, label=l)
        if scatter:
            ax.scatter(xs, ys, label=l, s=s)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.set_ylim(0, np.max(yss) * 2)
    handles, labels = ax.get_legend_handles_labels()
    ax.legend(handles=handles[::2], labels=labels[::2], title = legend_title)
    if logarithmic:
        ax.set_yscale('log')
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

def plot_sqrt_regression_error(ks, ws, x_label, y_label, filename):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))

    a = np.sum(ks)
    b = np.sum(-2 * np.dot(np.sqrt(ks), ws))
    c = np.sum(np.square(ws))
    print(f"C* = {-b/(2*a)}")

    vx = -b / (2 * a)
    vy = a * vx ** 2 + b * vx + c

    # Plot cuadratic curve ax^2 + bx + c
    x = np.linspace(0, 2, 100)
    y = a * x**2 + b * x + c
    ax.plot(x, y)

    ax.axhline(y=vy, color='lightgrey', linestyle='--')
    ax.axvline(x=vx, color='lightgrey', linestyle='--')

    # zorder
    ax.scatter(vx, vy, color='red', zorder=5)
    ax.annotate(f"({vx:.3f}, {vy:.3g})", (vx, vy), textcoords="offset points", xytext=(0,15), ha='center', color='red')

    ax.set_xlabel("c")
    ax.set_ylabel("E(c)")
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()

    ks_star = np.linspace(min(KS), max(KS), 100)
    ws_star = vx * np.sqrt(ks_star)
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    ax.plot(ks_star, ws_star)
    ax.scatter(ks, ws, c="red", zorder=5)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.legend([f"$y = {vx:.3g} " + "\sqrt{k}$"])
    plt.savefig(f"{BASE_PATH}/max_ws.png")
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
            plot(ts, ys, "Tiempo (s)", "$y_{max}$ (m)", f"max_position_{w}", scatter=True, plot=False, s=2)
        BASE_PATH = f'output/2/{k}'
        plot(ws, max_ys, "$\omega\ (s^{-1})$", "$A_{max}$ (m)", f"max_position", scatter=True, plot=True)
        max_yss.append(max_ys)

    BASE_PATH = "output/2"
    plot_aggregated(wss, max_yss, [f"{k} kg/$s^2$" for k in KS], "$\omega\ (s^{-1})$", "$A_{max}$ (m)", f"max_position_aggregated", legend_title="k", scatter=True, plot=True)

    max_ws = [max([yw for yw in zip(ys, ws)])[1] for ys, ws in zip(max_yss, wss)]
    plot_sqrt_regression_error(KS, max_ws, "$k\ (Kg/s)$", "$\omega_0\ (s^{-1})$", f"max_w_error")


