import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib.animation import FuncAnimation
import json
mpl.use('Agg')

ID_IDX = 0
X_IDX = 1
Y_IDX = 2
R_IDX = 3
VX_IDX = 4
VY_IDX = 5
MARKED_IDX = 6

with open("config/moving_config.json", "r") as f:
    config = json.load(f)
    DT = config["DT"]
    INTERVAL = config["INTERVAL"]
    L = config["L"]
    RADIUS = config["R"]
    OBS_RADIUS = config["OBSTACLE_RADIUS"]
    START = config["START"]
    LIMIT = config["LIMIT"]
    MOVING_OBSTACLE = config["MOVING_OBSTACLE"]
    V = int(config["V"])
    BASE_PATH = "output/" + ("moving" if MOVING_OBSTACLE else int(V)) + "/"

def init_plot(history):
    fig, ax = plt.subplots()
    # make the plot square
    ax.set_aspect('equal')
    ax.set_xlim(0, L)
    ax.set_ylim(0, L)

    scatters = []
    labels = []
    for p in history[0]:  # One scatter per particle
        scatter = ax.scatter([], [], s=25 * (p[R_IDX] / RADIUS)**2 , color="blue")  # s sets size; customize if needed
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

    if not MOVING_OBSTACLE:
        obstacle = plt.Circle((L/2, L/2), OBS_RADIUS, color='grey')
        ax.add_artist(obstacle)

    ax.set_title(f"t: {frame}")
    return scatters


if __name__ == "__main__":
    history = []
    # with open(f"{BASE_PATH}/initial_state.txt", "r") as f:
    #     particles = []
    #     for line in f.readlines():
    #         pid, x, y, r, vx, vy, _ = line[:-1].split(" ")
    #         particles.append(np.array([int(pid), float(x), float(y), float(r), float(vx), float(vy)]))
    #     history.append(particles)

    with open(f"{BASE_PATH}/state_{V}.txt", "r") as f:
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
    anim.save(f'{BASE_PATH}/animation.gif', writer='pillow', fps=10)

