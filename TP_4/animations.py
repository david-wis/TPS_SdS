import math

import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
import matplotlib as mpl
import moviepy.editor as mp
import json
BASE_PATH = "output/2"

mpl.use('Agg')

with open("config/config2.json", "r") as f:
    config = json.load(f)
    M = config["m"]
    A = config["A"]
    DT2 = config["dt2"]
    N = config["N"]+1
    L0 = config["l0"]


if __name__ == "__main__":
    # Step 1: Load the data from the file
    K = 1000
    W = 31.622776601683793
    data = np.loadtxt(f'{BASE_PATH}/{K}/{W}/animation.txt')  # Replace 'animation.txt' with your actual file name
    print("data read", len(data))
    ts = np.unique(data[:, 0])
    print("ts len", len(ts))
    pss = []
    for i, t in enumerate(ts):
        # ps = data[data[:, 0] == t][:, 1]  # Extract y positions for the current time step
        ps = data[i * N:(i + 1) * N, 1]
        ps = ps[::-1]
        if np.isnan(ps).any():
            print("nan found")
            break
        pss.append(ps)

    fig, ax = plt.subplots()
    xs = np.arange(N) * L0
    ax.set_xlim(np.min(xs), np.max(xs))  # x-axis represents particle index
    ax.set_xlabel('x (m)')
    ax.set_ylabel('y (m)')

    scat = ax.scatter(xs, pss[0], s=1)
    plot = ax.plot(xs, pss[0])
    time_text = ax.text(0.02, 0.95, '', transform=ax.transAxes)
    ylim = 0.02 * 1.1

    def update(frame):
        global ylim
        ps = pss[frame]
        ylim = max(np.max(np.abs(ps)) * 1.1, ylim)
        ax.set_ylim(-ylim, ylim)  # y-axis represents particle positions
        scat.set_offsets(np.c_[xs, pss[frame]])  # Update particle positions
        plot[0].set_ydata(pss[frame])
        print(frame * DT2, ylim)

        # Update time annotation
        time_text.set_text(f'Time: {ts[frame]:.2f}')
        return scat, time_text

    # Create the animation
    print("starting animation")
    ani = FuncAnimation(fig, update, frames=len(pss), blit=True)

    # Save as GIF
    ani.save(f'{BASE_PATH}/{K}/{W}/animation.gif', writer='pillow', fps=20)

    # Convert GIF to MP4 using moviepy
    clip = mp.VideoFileClip(f'{BASE_PATH}/{K}/{W}/animation.gif')
    clip.write_videofile(f'{BASE_PATH}/{K}/{W}/animation.mp4')
