import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from IPython import display
from PIL import Image
import moviepy.editor as mp
import os

BASE_PATH = './output'
files = [f for f in os.listdir(BASE_PATH) if f.endswith('2d.txt') ]
files = [f.replace(".txt", "") for f in files if not os.path.exists(f"{BASE_PATH}/{f.replace('.txt', '.gif')}")]
print(files)
for fpath in files:
    with open(f'{BASE_PATH}/{fpath}.txt') as f:
        rows = int(f.readline())
        cols = int(f.readline())
        states = [ np.array(list(map(int, g.split()))).reshape((rows,cols)) for g in f.readlines() ]
    print(fpath, len(states))

    fig, ax = plt.subplots()
    ax.set_title('Cantidad de células vivas')
    ax.set_xlabel('Frame')
    ax.plot([np.sum(state) for state in states])
    plt.savefig(f"{BASE_PATH}/{fpath}_mass.png")

    fig, ax = plt.subplots()
    ax.set_title('radius')
    ax.set_xlabel('Frame')
    # print([state.nonzero()[0].shape for state in states])
    ax.plot([np.sqrt((np.abs(np.array(state.nonzero()).T - np.array([rows/2, cols/2]))**2)).sum(axis=1).max() if state.nonzero()[0].shape[0] != 0 else 0 for state in states])
    plt.savefig(f"{BASE_PATH}/{fpath}_radius.png")

    def plot_state(state, ax, index):
        ax.imshow(state, cmap='binary')
        #set tick labales to 10 times
        ax.set_xticks(np.arange(0, state.shape[1], state.shape[1] // 10))
        ax.set_yticks(np.arange(0, state.shape[0], state.shape[1] // 10))
        ax.set_title(f'Estado {index}')
        # ax.grid(color='black', linewidth=2)

    images = []
    fig, ax = plt.subplots()

    for i, state in enumerate(states):
        plot_state(state, ax, i)
        fig.canvas.draw()
        image = np.array(fig.canvas.renderer.buffer_rgba())
        images.append(Image.fromarray(image))

    images[0].save(f'{BASE_PATH}/{fpath}.gif', save_all=True, append_images=images[1:], loop=0, interval=1000)
    clip = mp.VideoFileClip(f'{BASE_PATH}/{fpath}.gif')
    clip.write_videofile(f'{BASE_PATH}/{fpath}.mp4')