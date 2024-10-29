import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib.animation import FuncAnimation
import json
import moviepy.editor as mp
mpl.use('Agg')

ID_IDX = 0
X_IDX = 1
Y_IDX = 2
R_IDX = 3

with open("config/config.json", "r") as f:
    config = json.load(f)
    DT2 = config["DT2"]
    L = config["L"]
    W = config["W"]
    R = config["R"]
    OBS_RADIUS = config["OBSTACLE_RADIUS"]
    BASE_PATH = "output/cac71"
    START = 0
    LIMIT = 50

def init_plot(history, obstacles):
    fig, ax = plt.subplots()
    # make the plot square
    ax.set_aspect('equal')
    ax.set_xlim(0, L)
    ax.set_ylim(0, W)

    scatters = []
    labels = []
    for p in history[0]:  # One scatter per particle
        scatter = ax.scatter([], [], s=100 * (p[R_IDX])**2 , color="blue")  # s sets size; customize if needed
        scatters.append(scatter)
        text = ax.text(0, 0, "")
        labels.append(text)
    for o in obstacles:
        scatter = ax.scatter([], [], s=100 * (o[R_IDX])**2 , color="grey")  # s sets size; customize if needed
        scatter.set_offsets([o[X_IDX], o[Y_IDX]])
    return fig, ax, scatters, labels




def update(frame, scatters, labels, ax, history):
    print(frame)
    particles = history[frame]

    for scatter, label, particle in zip(scatters, labels, particles):
        scatter.set_offsets([particle[1], particle[2]])  # particle[1] -> x, particle[2] -> y
        scatter.set_color("red" if particle[-1] == 1 else "blue")  # Change color if needed


    ax.set_title(f"t = {(frame * DT2):.3f} s")
    return scatters


if __name__ == "__main__":

    obstacles = np.loadtxt(f"{BASE_PATH}/obstacles.txt")
    with open(f"{BASE_PATH}/animation.txt", "r") as f:
        particles = []
        counter = 0
        history = []
        for line in f:

            pid, x, y, r = line[:-1].split(" ")
            if pid == '0':

                if counter >= START+1:
                    history.append(particles)

                particles = []
                if counter - START > LIMIT:
                    break
                else:
                    counter += 1

            if counter >= START:
                particles.append(np.array([int(pid), float(x), float(y), float(r)]))
        history.append(particles)

    print(len(history))

    fig, ax, scatters, labels = init_plot(history, obstacles)
    anim = FuncAnimation(
        fig, update, frames=min(LIMIT, len(history)), fargs=(scatters, labels, ax, history),
        interval=10, blit=True  # interval is in milliseconds
    )
    anim.save(f'{BASE_PATH}/animation.gif', writer='pillow', fps=20)
    clip = mp.VideoFileClip(f'{BASE_PATH}/animation.gif')
    clip.write_videofile(f'{BASE_PATH}/animation.mp4')

