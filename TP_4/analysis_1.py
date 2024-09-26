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
    DT = config["dt"]
    M = config["m"]
    K = config["k"]
    GAMMA = config["gamma"]
    TF = config["tf"]
    R0 = config["r0"]

def plot(xs, ys, x_label, y_label,filename):
    fig, ax = plt.subplots()
    plt.ticklabel_format(style='sci', axis='x', scilimits=(-5,5))
    ax.plot(xs, ys)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    plt.savefig(f"{BASE_PATH}/{filename}.png")

if __name__ == "__main__":
    BASE_PATH = "output"
    # Load data
    data = np.loadtxt(f"{BASE_PATH}/verlet_state.txt")
    xs = data[:, 0]
    vs = data[:, 1]
    ts = np.arange(0, TF, DT)
    print(len(ts), len(xs))
    # Plot data
    plot(ts, xs, "Tiempo (s)", "Posición (m)", f"positions")
    plot(ts, vs, "Tiempo (s)", "Velocidad (m/s)", f"velocities")