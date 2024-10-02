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

with open("config/config2.json", "r") as f:
    config = json.load(f)
    M = config["m"]
    KS = config["ks"]
    A = config["A"]
    L0 = config["l0"]
    N = config["N"]
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
        ax.set_yscale('log')
    plt.savefig(f"{BASE_PATH}/{filename}.png")
    plt.close()


if __name__ == "__main__":
    # Load data
    max_yss = []
    for k in KS:
        max_ys = []
        BASE_PATH = f'output/2/{k}'
        for w in WS:
            data = np.loadtxt(f'{BASE_PATH}/{w}/animation.txt')
            ts = np.unique(data[:, 0])
            ps = data[0:N, 1]
            max_ys.append(np.max(np.abs(ps)))
        plot(WS, max_ys, "w", "Max Position", f"max_position", scatter=True)
        max_yss.append(max_ys)
    BASE_PATH = "output/2"
    plot_aggregated(WS, max_yss, KS, "w", "Max Position", f"max_position_aggregated", scatter=True)



