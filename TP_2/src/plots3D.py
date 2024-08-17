import numpy as np
import ipywidgets as widgets
from IPython.display import display
from PIL import Image
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

states = []
with open('gol3d.txt') as f:
    rows = int(f.readline())
    cols = int(f.readline())
    depth = int(f.readline())
    states = [ np.array(list(map(int, g.split()))).reshape((rows,cols, depth)) for g in f.readlines() ]
print(len(states))


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
images[0].save('automata_3d.gif', save_all=True, append_images=images[1:], loop=0, duration=100)


def update_plot(frame):
    plot_state_3d(states[frame], ax)
    fig.canvas.draw()

slider = widgets.IntSlider(min=0, max=len(states)-1, step=1, description='Frame')
widgets.interact(update_plot, frame=slider)
display(fig)