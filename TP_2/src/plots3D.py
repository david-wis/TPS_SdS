import numpy as np
from PIL import Image
import matplotlib.pyplot as plt
from analysis import poly_analysis
import moviepy.editor as mp
import os
BASE_PATH = './output'
fnames = [f for f in os.listdir(BASE_PATH) if f.endswith('3d') ]

for fname in fnames:
    path = f"{BASE_PATH}/{fname}"
    with open(f'{path}/{fname}.txt') as f:
        rows = int(f.readline())
        cols = int(f.readline())
        depth = int(f.readline())
        states = [ np.array(list(map(int, g.split()))).reshape((rows,cols, depth)) for g in f.readlines() ]
        states = [s for i, s in enumerate(states) if i < 5 or states[i-4].sum() > 0]
    print(len(states))


    fig, ax = plt.subplots()
    ax.set_title('Cantidad de células vivas')
    ax.set_xlabel('Frame')
    mass_list = [np.sum(state) for state in states]
    ax.plot(mass_list)
    plt.savefig(f"{path}/{fname}_mass.png")
    poly_analysis(mass_list, f"{path}/{fname}_cubic.png", 3)

    fig, ax = plt.subplots()
    ax.set_title('radius')
    ax.set_xlabel('Frame')
    ax.plot([np.sqrt((np.abs(np.array(state.nonzero()).T - np.array([rows/2, cols/2, depth/2]))**2)).sum(axis=1).max() if state.nonzero()[0].shape[0] != 0 else 0 for state in states])
    plt.savefig(f"{path}/{fname}_radius.png")

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
    images[0].save(f'{path}/{fname}.gif', save_all=True, append_images=images[1:], loop=0, duration=100)
    clip = mp.VideoFileClip(f'{path}/{fname}.gif')
    clip.write_videofile(f'{path}/{fname}.mp4')