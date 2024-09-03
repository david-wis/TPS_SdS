import numpy as np
import matplotlib.pyplot as plt



def plot_state(ax, state, length, core):
    #set limits
    ax.set_xlim(0, state.shape[0])
    ax.set_ylim(0, state.shape[1])
    ax.set_zlim(0, state.shape[2])
    x, y, z = state.nonzero()
    start = (length//2-core//2)-0.5
    end = (length//2+core//2)-0.5
    ax.scatter([start, start, start, start, end, end, end, end], [start, end, start, end, start, end, start, end], [start, start, end, end,start, start, end, end], c='red', s=10, marker='x')
    ax.set_xticks([0, 15, 35,50])
    ax.set_yticks([0,15, 35,50])
    ax.set_zticks([0,15, 35,50])
    ax.scatter(x, y, z, c='black')

if __name__ == '__main__':
    total_l = 50
    core_l = 20

    for n in [0]:
        filename = f'input/init3d_{n}'
        fig, axs = plt.subplots( 2, 5, figsize=(20, 10), subplot_kw=dict(projection='3d'))
        with open(f"{filename}.txt", 'w') as f:
            f.write(f"{total_l}\n")
            f.write(f"{core_l}\n")

            for i in range(2):
                for j in range(5):
                    state = np.zeros((total_l, total_l, total_l))
                    a = np.zeros(core_l**3)
                    a[:n] = 1
                    np.random.shuffle(a)
                    core = a.reshape((core_l, core_l, core_l))
                    start = total_l//2-core_l//2
                    end = total_l//2+core_l//2
                    state[start:end, start:end, start:end] = core

                    f.write(''.join(map(lambda x: str(int(x)), state.flatten())) + '\n')
                    plot_state(axs[i,j], state, total_l, core_l)
        plt.savefig(f'{filename}.png')