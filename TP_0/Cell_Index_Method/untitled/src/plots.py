import matplotlib.pyplot as plt
import numpy as np

def plot_grid(ax, L, M, particles, selected, neighbors):
    ax.set_xticks(np.arange(-.5, M-1, 1))
    ax.set_yticks(np.arange(-.5, M-1, 1))
    ax.set_xticklabels(np.arange(0, M, 1))
    ax.set_yticklabels(np.arange(0, M, 1))
    ax.imshow(np.zeros((M,M)))
    ax.grid(color='white', linewidth=2)

fig, axs = plt.subplots(1, 1, figsize=(10, 10))
with open("initial_state.txt", "r") as f:

    particles = []
    for line in f:
        particles.append(list(map(int, line.split())))

with open("output.txt", "r"):
    L = float(f.readline())
    M = int(f.readline())
    N = int(f.readline())
    Rc = float(f.readline())
    points = dict()
    for line in f:
        pid, nids = f.split(":")
        nids = nids.split(" ")
        points[pid] = nids

