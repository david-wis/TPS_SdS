import matplotlib.pyplot as plt
import numpy as np
import sys

BASE_PATH = "."
PID_INDEX = 0
X_INDEX = 1
Y_INDEX = 2
R_INDEX = 3
def plot_grid(L, M, particles, selected, neighbors, rc, filename):
    fig, ax = plt.subplots(figsize=(10, 10))

    # Set grid and ticks
    ax.set_xticks(np.arange(0, L, L/M))
    ax.set_yticks(np.arange(0, L, L/M))
    ax.set_xticklabels(np.arange(0, L, L/M))
    ax.set_yticklabels(np.arange(0, L, L/M))
    
    # Plot the grid
    ax.grid(which='both', color='black', linestyle='-', linewidth=2)
    
    # Set limits
    ax.set_xlim(0, L)
    ax.set_ylim(0, L)
    
    # Plot particles
    p = np.array([10,10])
    radius = 3000
    # use circle to plot particles instead of scatter
    for particle in particles:
        circle = plt.Circle(np.array([particle[X_INDEX], particle[Y_INDEX]]), particle[R_INDEX], color='red', fill=True)
        ax.add_artist(circle)

    for neighbor in neighbors:
        circle = plt.Circle(np.array([neighbor[X_INDEX], neighbor[Y_INDEX]]), neighbor[R_INDEX], color='green', fill=True)
        ax.add_artist(circle)

    selected_circle = plt.Circle(np.array([selected[X_INDEX], selected[Y_INDEX]]), selected[R_INDEX], color='blue', fill=True)
    ax.add_artist(selected_circle)

    # Plot the circle
    circle = plt.Circle(np.array([selected[X_INDEX], selected[Y_INDEX]]), rc+selected[R_INDEX], color='orange', fill=False)
    ax.add_artist(circle)

    plt.savefig(f"{filename}.png")


if __name__ == "__main__":
    with open(f"{BASE_PATH}/initial_state.txt", "r") as f:
        particles = []
        for line in f:
            pid, x, y, r = line[:-1].split(" ")
            particles.append(np.array([int(pid), float(x), float(y), float(r)]))

    with open(f"{BASE_PATH}/output.txt", "r") as f:
        L = float(f.readline())
        M = int(f.readline())
        N = int(f.readline())
        Rc = float(f.readline())
        points = dict()
        for line in f:
            pid, nids = line[:-1].split(":")
            pid = int(pid.strip())
            nids = nids.split(" ")
            nids = [int(nid) for nid in nids if nid != ""]
            points[pid] = nids

    print(points)
    if len(sys.argv) < 2:
        pid = 0
    else:
        pid = int(sys.argv[1])
    neighbors = np.array([particles[p] for p in points[pid]])
    particles = np.array(particles)
    plot_grid(L, M, particles, particles[pid], neighbors, Rc, f"plot_{pid}")