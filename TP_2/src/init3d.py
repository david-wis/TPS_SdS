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
    state = np.zeros((50, 50, 50))
    a = np.zeros(125)
    n = 125
    a[:n] = 1
    np.random.shuffle(a)
    core = a.reshape((5, 5, 5))

    state[23:28, 23:28, 23:28] = core
    filename = f'input/init3d{n:03}'
#     write_state(state, filename)
    state = read_state(filename)
    print(state)
    plot_state(state, filename)