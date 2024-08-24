import numpy as np
import matplotlib.pyplot as plt
from PIL import Image
import moviepy.editor as mp
import os
from analysis import poly_analysis

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

def plot_animation(run):
    pass


def add_line(ax, ms, label):
    ax.plot(range(len(ms)), ms, label=label)


BASE_PATH = './output'
def analyze_rule(rulename, obs_function):
    dim = 2 if rulename.endswith('2D') else 3
    path = f"{BASE_PATH}/{fname}"
    quantities = [int(f.replace(".txt","")) for f in os.listdir(path) if f.endswith('.txt')]
    quantities.sort()
    first_masses = []
    fig, ax = plt.subplots()
    dict_msss = dict()
    for q in quantities:
        runs, length, core = get_runs(f'{path}/{q}.txt')
        mss = [[np.sum(state) for state in run ] for run in runs ]
        dict_msss[q] = mss
        add_line(ax, mss[0], f"{q} : {100 * q / core ** dim} %")
        print(rulename, q, len(runs))
    ax.legend()
    plt.savefig(f"{path}/{fname}_mass.png")

    obss = [[obs_function(ms) for ms in mss] for mss in dict_msss.values()]
    masses_avg = np.array([np.mean(obs) for obs in obss])
    masses_std = np.array([np.std(obs) for obs in obss])
    fig, ax = plt.subplots()
    ax.errorbar(quantities, masses_avg, masses_std, fmt='o', linewidth=2, capsize=6)
    ax.set_xticks(quantities)
    plt.savefig(f"{path}/{fname}_obs.png")


if __name__ == '__main__':
    fnames = [f for f in os.listdir(BASE_PATH) if f.endswith('2D') or f.endswith('3D') ]
    print(fnames)

    for fname in fnames:
        analyze_rule(fname, np.max)


        # if os.path.exists(f"{path}/{fname}.gif"):
        #     continue
        # print(fname, len(states))
        #
        # fig, ax = plt.subplots()
        # ax.set_title('Cantidad de células vivas')
        # ax.set_xlabel('Frame')
        # mass_list = np.array([np.sum(state) for state in states])
        # ax.plot(mass_list)
        # plt.savefig(f"{path}/{fname}_mass.png")
        # poly_analysis(mass_list[:-1], f"{path}/{fname}_cubic.png", 2)
        # print(mass_list)
        #
        # fig, ax = plt.subplots()
        # ax.set_title('radius')
        # ax.set_xlabel('Frame')
        # # print([state.nonzero()[0].shape for state in states])
        # ax.plot([np.sqrt((np.abs(np.array(state.nonzero()).T - np.array([rows/2, cols/2]))**2)).sum(axis=1).max() if state.nonzero()[0].shape[0] != 0 else 0 for state in states])
        # plt.savefig(f"{path}/{fname}_radius.png")
        #
        # def plot_state(state, ax, index):
        #     ax.imshow(state, cmap='binary')
        #     #set tick labels to 10 times
        #     ax.set_xticks(np.arange(0, state.shape[1], state.shape[1] // 10))
        #     ax.set_yticks(np.arange(0, state.shape[0], state.shape[0] // 10))
        #     ax.set_xticklabels(np.arange(0, state.shape[1], state.shape[1] // 10) * 10)
        #     ax.set_yticklabels(np.arange(0, state.shape[0], state.shape[0] // 10) * 10)
        #     ax.invert_yaxis()
        #
        #     ax.set_title(f'Estado {index}')
        #     # ax.grid(color='black', linewidth=2)
        #
        # images = []
        # fig, ax = plt.subplots()
        #
        # for i, state in enumerate(states):
        #     plot_state(state, ax, i)
        #     fig.canvas.draw()
        #     image = np.array(fig.canvas.renderer.buffer_rgba())
        #     images.append(Image.fromarray(image))
        #
        # images[0].save(f'{path}/{fname}.gif', save_all=True, append_images=images[1:], loop=0, interval=1000)
        # clip = mp.VideoFileClip(f'{path}/{fname}.gif')
        # clip.write_videofile(f'{path}/{fname}.mp4')