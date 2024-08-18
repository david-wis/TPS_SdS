import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from IPython import display
from PIL import Image

import os
files = [f for f in os.listdir('.') if f.endswith('2d.txt') ]
files = [f.replace(".txt", "") for f in files if not os.path.exists(f.replace('.txt', '.gif'))]

for fpath in files:
# with open('gol2d.txt') as f:
    with open(f'{fpath}.txt') as f:
        rows = int(f.readline())
        cols = int(f.readline())
        states = [ np.array(list(map(int, g.split()))).reshape((rows,cols)) for g in f.readlines() ]
    print(fpath, len(states))

    fig, ax = plt.subplots()
    ax.set_title('Cantidad de células vivas')
    ax.set_xlabel('Frame')
    ax.plot([np.sum(state) for state in states])
    plt.savefig(f"{fpath}_mass.png")

    fig, ax = plt.subplots()
    ax.set_title('radius')
    ax.set_xlabel('Frame')
    ax.plot([np.sqrt((np.abs(np.array(state.nonzero()).T - np.array([rows/2, cols/2]))**2)).sum(axis=1).max() for state in states])
    plt.savefig(f"{fpath}_radius.png")

    def plot_state(state, ax, index):
        ax.imshow(state, cmap='binary')
        # ax.set_xticks(np.arange(-.5, rows-1, 1))
        # ax.set_yticks(np.arange(-.5, cols-1, 1))
        ax.set_title(f'Estado {index}')
        # ax.grid(color='black', linewidth=2)

    # Crear una lista de imágenes
    images = []
    fig, ax = plt.subplots()

    for i, state in enumerate(states):
        plot_state(state, ax, i)
        fig.canvas.draw()

        # Convertir la figura a una imagen de PIL y agregarla a la lista
        image = np.array(fig.canvas.renderer.buffer_rgba())
        images.append(Image.fromarray(image))

    # Guardar las imágenes como un GIF
    images[0].save(f'{fpath}.gif', save_all=True, append_images=images[1:], loop=0)
