import numpy as np
from PIL import Image
import matplotlib.pyplot as plt

import moviepy.editor as mp
import os
BASE_PATH = './output'
files = [f for f in os.listdir(BASE_PATH) if f.endswith('3d.txt') ]
files = [f.replace(".txt", "") for f in files] # if not os.path.exists(f"{BASE_PATH}/{f.replace('.txt', '.gif')}")]

for fpath in files:
    with open(f'{BASE_PATH}/{fpath}.txt') as f:
        rows = int(f.readline())
        cols = int(f.readline())
        depth = int(f.readline())
        states = [ np.array(list(map(int, g.split()))).reshape((rows,cols, depth)) for g in f.readlines() ]
    print(len(states))


    fig, ax = plt.subplots()
    ax.set_title('Cantidad de células vivas')
    ax.set_xlabel('Frame')
    ax.plot([np.sum(state) for state in states])
    plt.savefig(f"{BASE_PATH}/{fpath}_mass.png")

    fig, ax = plt.subplots()
    ax.set_title('radius')
    ax.set_xlabel('Frame')
    ax.plot([np.sqrt((np.abs(np.array(state.nonzero()).T - np.array([rows/2, cols/2, depth/2]))**2)).sum(axis=1).max() if state.nonzero()[0].shape[0] != 0 else 0 for state in states])
    plt.savefig(f"{BASE_PATH}/{fpath}_radius.png")

    def plot_state_3d(state, ax, index):
        ax.clear()
        ax.set_xticks([])
        ax.set_yticks([])
        ax.set_zticks([])
        ax.set_xlim(0, state.shape[0])
        ax.set_ylim(0, state.shape[1])
        ax.set_zlim(0, state.shape[2])
        x, y, z = state.nonzero()
        a = np.abs(x) + np.abs(y) + np.abs(z)
        if np.sum(state) > 0:
            colors =plt.cm.cool( (a-a.min())/float((a-a.min()).max()) )
        else:
            colors = 'black'
        ax.scatter(x, y, z, c=colors, marker='o')
        ax.set_title(f'Estado {index}')
        ax.view_init(index , 3*index)
    # Crear una lista de imágenes
    images = []

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    for i, state in enumerate(states):
        plot_state_3d(state, ax, i)
        fig.canvas.draw()

        # Convertir la figura a una imagen de PIL y agregarla a la lista
        image = np.array(fig.canvas.renderer.buffer_rgba())
        images.append(Image.fromarray(image))

    # Guardar las imágenes como un GIF
    images[0].save(f'{BASE_PATH}/{fpath}.gif', save_all=True, append_images=images[1:], loop=0, duration=100)
    clip = mp.VideoFileClip(f'{BASE_PATH}/{fpath}.gif')
    clip.write_videofile(f'{BASE_PATH}/{fpath}.mp4')