import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from PIL import Image
import moviepy.editor as mp
import os
from analysis import poly_analysis

mpl.use('Agg')

def get_runs(filename):
    with open(filename) as f:
        length = int(f.readline())
        core = int(f.readline())
        f.readline()
        states = []
        runs = [states]
        while line := f.readline():
            if line == "\n":
                states = []
                runs.append(states)
            else:
                states.append(np.array([int(x) for x in line[:-1]]))

    return [r for r in runs if len(r) > 0], length, core

def plot_animation_2d(path, q):
    runs, length, core = get_runs(f'{path}/{q}.txt')
    animation_path = f"{path}/{q}_animations"
    if not os.path.exists(animation_path):
        os.makedirs(animation_path)

    for i, r in enumerate(runs):
        images = []
        fig, ax = plt.subplots()
        for j, state in enumerate(r):
            ax.clear()
            state = state.reshape((length, length))
            ax.imshow(state, cmap='binary')
            start = (length//2-core//2)-0.5
            end = (length//2+core//2)-0.5
            ax.scatter([start, start, end, end], [start, end, start, end], c='red', s=5, marker='x')
            #set tick labels to 10 times
            ax.set_xticks(np.arange(0, state.shape[1], state.shape[1] // 10))
            ax.set_yticks(np.arange(0, state.shape[0], state.shape[0] // 10))
            ax.set_xticklabels(np.arange(0, state.shape[1], state.shape[1] // 10) * 10)
            ax.set_yticklabels(np.arange(0, state.shape[0], state.shape[0] // 10) * 10)
            ax.invert_yaxis()

            ax.set_title(f'Estado {j}')
            # ax.grid(color='black', linewidth=2)
            fig.canvas.draw()
            image = np.array(fig.canvas.renderer.buffer_rgba())
            images.append(Image.fromarray(image))

        images[0].save(f'{animation_path}/{q}_{i}.gif', save_all=True, append_images=images[1:], loop=0, interval=1000)
        clip = mp.VideoFileClip(f'{animation_path}/{q}_{i}.gif')
        clip.write_videofile(f'{animation_path}/{q}_{i}.mp4')

def plot_animation_3d(path, q):
    runs, length, core = get_runs(f'{path}/{q}.txt')
    animation_path = f"{path}/{q}_animations"
    if not os.path.exists(animation_path):
        os.makedirs(animation_path)

    for i, r in enumerate(runs):
        images = []
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        for j, state in enumerate(r):
            ax.clear()
            state = state.reshape((length, length, length))
            ax.set_xlim(0, length)
            ax.set_ylim(0, length)
            ax.set_zlim(0, length)
            x, y, z = state.nonzero()
            a = radius(state, length)
            if np.sum(state) > 0:
                colors =plt.cm.cool((a-a.min())/float((a-a.min()).max()))
            else:
                colors = 'black'
            ax.scatter(x, y, z, c=colors, marker='o', s=1)
            ax.set_title(f'Estado {j}')
            ax.view_init(j, 3*j)
            fig.canvas.draw()
            image = np.array(fig.canvas.renderer.buffer_rgba())
            images.append(Image.fromarray(image))
        images[0].save(f'{animation_path}/{q}_{i}.gif', save_all=True, append_images=images[1:], loop=0, interval=1000)
        clip = mp.VideoFileClip(f'{animation_path}/{q}_{i}.gif')
        clip.write_videofile(f'{animation_path}/{q}_{i}.mp4')


def add_line(ax, ms, label):
    ax.plot(range(len(ms)), ms, label=label)

def radius(state, length):
    return np.abs(np.array(state.nonzero()).T - np.array([length/2] * state.ndim)).sum(axis=1) if state.nonzero()[0].shape[0] != 0 else np.array([0])


BASE_PATH = './output'
def analyze_rule(rulename, obs_function):
    dim = 2 if '2D' in rulename else 3
    path = f"{BASE_PATH}/{rulename}"
    quantities = [int(f.replace(".txt","")) for f in os.listdir(path) if f.endswith('.txt')]
    quantities.sort()
    mass_fig, mass_ax = plt.subplots()
    radius_fig, radius_ax = plt.subplots()
    dict_msss = dict()
    for q in quantities:
        runs, length, core = get_runs(f'{path}/{q}.txt')
        # set only min limit as 0, the max limit is implicit

        print(rulename, q, len(runs))
        mss = [[np.sum(state) for state in run ] for run in runs ]
        rss = [[radius(state.reshape((length, ) * dim), length).max() for state in run] for run in runs]
        dict_msss[q] = mss
        add_line(mass_ax, mss[0], f"{q} : {100 * q / core ** dim} %")
        add_line(radius_ax, rss[0], f"{q} : {100 * q / core ** dim} %")
        p_mass_fig, p_mass_ax = plt.subplots()
        for i, ms in enumerate(mss):
            add_line(p_mass_ax, ms, f"muestra {i}")
        p_mass_ax.set_ylim(bottom=0)
        p_mass_ax.legend(bbox_to_anchor=(1.05, 1.0), loc='upper left')
        p_mass_fig.tight_layout()
        p_mass_fig.savefig(f"{path}/{q}_mass.png")

    mass_ax.set_ylim(bottom=0)
    mass_ax.legend(bbox_to_anchor=(1.05, 1.0), loc='upper left')
    mass_fig.tight_layout()
    mass_fig.savefig(f"{path}/{rulename}_mass.png")

    radius_ax.set_ylim(bottom=0)
    radius_ax.legend(bbox_to_anchor=(1.05, 1.0), loc='upper left')
    radius_fig.tight_layout()
    radius_fig.savefig(f"{path}/{rulename}_radius.png")

    obss = [[obs_function(ms) for ms in mss] for mss in dict_msss.values()]
    masses_avg = np.array([np.mean(obs) for obs in obss])
    masses_std = np.array([np.std(obs) for obs in obss])
    fig, ax = plt.subplots()
    ax.errorbar(quantities, masses_avg, masses_std, fmt='o', linewidth=2, capsize=6)
    ax.set_xticks(quantities)
    plt.savefig(f"{path}/{rulename}_obs.png")


if __name__ == '__main__':
    fnames = [f for f in os.listdir(BASE_PATH) if f.endswith('2D') or f.endswith('3D') ]
    print(fnames)
    # ['decay3D', 'even2D', 'gol2D', 'fill2D', 'odd2D', 'gol3D', 'expansion3D']

    analyze_rule("fill2D", np.max)
    analyze_rule("gol2D", lambda x: x[-1])
    analyze_rule("gol2Dv2", lambda x: x[-1])
    analyze_rule("odd2D", np.max)
    analyze_rule("even2D", lambda x: x[-1])

    # analyze_rule("decay3D", lambda x: x[-1])
    # analyze_rule("decay3Dv2", lambda x: x[-1])
    # analyze_rule("gol3D", lambda x: len(x))




    # plot_animation_3d(f"{BASE_PATH}/decay3Dv2", 500)
    # plot_animation_3d(f"{BASE_PATH}/decay3Dv2", 4000)
    # plot_animation_3d(f"{BASE_PATH}/decay3Dv2", 7200)
    # plot_animation_3d(f"{BASE_PATH}/decay3D", 7200)
    # plot_animation_3d(f"{BASE_PATH}/decay3D", 4000)
    # plot_animation_2d(f"{BASE_PATH}/fill2D", 25)
    # plot_animation_2d(f"{BASE_PATH}/fill2D", 10)