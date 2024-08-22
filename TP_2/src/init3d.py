import numpy as np
import matplotlib.pyplot as plt

#write s to file
def write_state(state, filename):
    with open(f"{filename}.txt", 'w') as f:
        f.write(f"{state.shape[0]}\n")
        f.write(f"{state.shape[1]}\n")
        f.write(f"{state.shape[2]}\n")
        f.write(' '.join(map(lambda x: str(int(x)), state.flatten())))

def plot_state(state, filename):
    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    #set limits
    ax.set_xlim(0, state.shape[0])
    ax.set_ylim(0, state.shape[1])
    ax.set_zlim(0, state.shape[2])
    x, y, z = state.nonzero()
    ax.scatter(x, y, z, c='black')
    plt.savefig(f'{filename}.png')

def read_state(filename):
    with open(f"{filename}.txt") as f:
        rows = int(f.readline())
        cols = int(f.readline())
        depth = int(f.readline())
        line = f.readline()
        state = np.array(list(map(int, line.split()))).reshape((rows, cols, depth))
    return state

if __name__ == '__main__':
    total_l = 50
    state = np.zeros((total_l, total_l, total_l))
    core_l = 20
    a = np.zeros(core_l**3)
    n = 4000
    a[:n] = 1
    np.random.shuffle(a)
    core = a.reshape((core_l, core_l, core_l))
    start = total_l//2-core_l//2
    end = total_l//2+core_l//2
    state[start:end, start:end, start:end] = core
    filename = f'input/init3d{n:03}'
    write_state(state, filename)
    state = read_state(filename)
    print(state)
    plot_state(state, filename)