import matplotlib.pyplot as plt
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib.animation import FuncAnimation
import sys

mpl.use('Agg')

BASE_PATH = "output"


def init_plot(history):
    fig, ax = plt.subplots()
    # make the plot square
    ax.set_aspect('equal')
    ax.set_xlim(0, 0.1)
    ax.set_ylim(0, 0.1)

    scatters = []
    labels = []
    for _ in range(len(history[0])):  # One scatter per particle
        scatter = ax.scatter([], [], s=20, color="blue")  # s sets size; customize if needed
        scatters.append(scatter)
        text = ax.text(0, 0, "")
        labels.append(text)
    return fig, ax, scatters, labels


def update(frame, scatters, labels, ax, history):
    print(frame)
    particles = history[frame]

    for scatter, label, particle in zip(scatters, labels, particles):
        scatter.set_offsets([particle[1], particle[2]])  # particle[1] -> x, particle[2] -> y
        scatter.set_color("red" if particle[-1] == 1 else "blue")  # Change color if needed
        # label.set_position((particle[1], particle[2]))
        # label.set_text(f"{int(particle[0])}")
        # scatter.set_label(f"t: {frame}")


    obstacle = plt.Circle((0.05, 0.05), 0.005, color='grey')
    ax.add_artist(obstacle)
    ax.set_title(f"t: {frame}")
    return scatters



START = 5000
LIMIT = 50

if __name__ == "__main__":
    history = []
    # with open(f"{BASE_PATH}/initial_state.txt", "r") as f:
    #     particles = []
    #     for line in f.readlines():
    #         pid, x, y, r, vx, vy, _ = line[:-1].split(" ")
    #         particles.append(np.array([int(pid), float(x), float(y), float(r), float(vx), float(vy)]))
    #     history.append(particles)

    with open(f"{BASE_PATH}/state.txt", "r") as f:
        particles = []
        counter = 0
        for line in f:

            pid, x, y, r, vx, vy, marked = line[:-1].split(" ")
            if pid == '0':

                if counter >= START:
                    history.append(particles)

                particles = []
                if counter - START > LIMIT:
                    break
                else:
                    counter += 1

            if counter >= START:
                particles.append(np.array([int(pid), float(x), float(y), float(r), float(vx), float(vy), int(marked)]))
        history.append(particles)

    print(len(history))


    fig, ax, scatters, labels = init_plot(history)
    anim = FuncAnimation(
        fig, update, frames=min(LIMIT, len(history)), fargs=(scatters, labels, ax, history),
        interval=10, blit=True  # interval is in milliseconds
    )
    anim.save('output/animation.gif', writer='pillow', fps=10)

