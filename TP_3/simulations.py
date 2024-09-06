import matplotlib.pyplot as plt
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
import sys

BASE_PATH = "output"


def init_plot():
    fig, ax = plt.subplots()
    ax.set_xlim(0, 0.1)  # Set limits according to your data
    ax.set_ylim(0, 0.1)

    scatters = []
    for _ in range(len(history[0])):  # One scatter per particle
        scatter = ax.scatter([], [], s=5, color="blue")  # s sets size; customize if needed
        scatters.append(scatter)

    return fig, ax, scatters


def update(frame, scatters, ax):
    particles = history[frame]

    for scatter, particle in zip(scatters, particles):
        scatter.set_offsets([particle[1], particle[2]])  # particle[1] -> x, particle[2] -> y
        # scatter.set_label(f"t: {frame}")

    ax.set_title(f"t: {frame}")
    return scatters



LIMIT = 100

if __name__ == "__main__":
    history = []
    with open(f"{BASE_PATH}/initial_state.txt", "r") as f:
        particles = []
        for line in f.readlines():
            pid, x, y, r, vx, vy = line[:-1].split(" ")
            particles.append(np.array([int(pid), float(x), float(y), float(r), float(vx), float(vy)]))
        history.append(particles)

    with open(f"{BASE_PATH}/state.txt", "r") as f:
        particles = []
        counter = 0
        for line in f:
            pid, x, y, r, vx, vy = line[:-1].split(" ")
            if pid == '0' and len(particles) != 0:
                history.append(particles)
                particles = []
                if counter > LIMIT:
                    break
                else:
                    counter += 1

            particles.append(np.array([int(pid), float(x), float(y), float(r), float(vx), float(vy)]))
        history.append(particles)

    fig, ax, scatters = init_plot()
    print(len(history))

    # Create the animation
    anim = FuncAnimation(
        fig, update, frames=LIMIT, fargs=(scatters, ax),
        interval=100, blit=True  # interval is in milliseconds
    )

    anim.save('output/animation.gif', writer='pillow', fps=10)
