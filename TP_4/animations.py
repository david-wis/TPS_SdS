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
    K = config["k"]
    A = config["A"]
    DT2 = config["dt2"]


if __name__ == "__main__":
    # Step 1: Load the data from the file
    data = np.loadtxt(f'{BASE_PATH}/animation.txt')  # Replace 'animation.txt' with your actual file name

    # Step 2: Get the unique time steps and corresponding particle positions
    ts = np.unique(data[:, 0])  # Extract unique time steps
    pss = []
    for t in ts:
        ps = data[data[:, 0] == t][:, 1]  # Extract y positions for the current time step
        pss.append(ps)

    # Step 3: Set up the figure and axis
    fig, ax = plt.subplots()
    N = len(pss[0])  # Number of particles
    ax.set_xlim(0, N)  # x-axis represents particle index
    ax.set_ylim(np.min(data[:, 1]) * 1.1, np.max(data[:, 1]) *1.1)  # y-axis represents particle positions

    # Initialize scatter plot for the first frame
    scat = ax.scatter(np.arange(N), pss[0], s=1)
    plot = ax.plot(np.arange(N), pss[0])
    time_text = ax.text(0.02, 0.95, '', transform=ax.transAxes)

    # Step 4: Function to update the scatter plot for each frame
    def update(frame):
        print(frame * DT2)
        # Update y positions for all particles at the current time step
        scat.set_offsets(np.c_[np.arange(N), pss[frame]])  # Update particle positions
        plot[0].set_ydata(pss[frame])

        # Update time annotation
        time_text.set_text(f'Time: {ts[frame]:.2f}')
        return scat, time_text

    # Create the animation
    ani = FuncAnimation(fig, update, frames=len(ts), blit=True)

    # Save as GIF
    ani.save(f'{BASE_PATH}/animation.gif', writer='pillow', fps=20)

    # Convert GIF to MP4 using moviepy
    clip = mp.VideoFileClip(f'{BASE_PATH}/animation.gif')
    clip.write_videofile(f'{BASE_PATH}/animation.mp4')
