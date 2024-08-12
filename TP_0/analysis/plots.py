import matplotlib.pyplot as plt
import numpy as np
import sys

BASE_PATH = "."
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
    ax.scatter(particles[:, 0], particles[:, 1], c='red', label='Particles', s=10)
    ax.scatter([selected[0]], [selected[1]], c='blue', label='Selected', s=10)
    ax.scatter(neighbors[:, 0], neighbors[:, 1], c='green', label='Neighbors', s=10)

    # Plot the circle
    circle = plt.Circle(selected, rc, color='orange', fill=False)
    ax.add_artist(circle)

    plt.savefig(f"{filename}.png")


if __name__ == "__main__":
    with open(f"{BASE_PATH}/initial_state.txt", "r") as f:
        particles = []
        for line in f:
            pid, x, y = line[:-1].split(" ")
            particles.append(np.array([float(x), float(y)]))

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