import numpy as np
import matplotlib.pyplot as plt

#write s to file
def write_state(state, filename):
    with open(f"{filename}.txt", 'w') as f:
        f.write(f"{state.shape[0]}\n")
        f.write(f"{state.shape[1]}\n")
        f.write(' '.join(map(lambda x: str(int(x)), state.flatten())))

def plot_state(state, filename):
    plt.imshow(state, cmap='binary')
    plt.savefig(f'{filename}.png')

def read_state(filename):
    with open(f"{filename}.txt") as f:
        rows = f.readline()
        cols = f.readline()
        line = f.readline()
        state = np.array(list(map(int, line.split()))).reshape((int(rows), int(cols)))
    return state

if __name__ == '__main__':
    state = np.zeros((100, 100))
    #core random with 10 1's
    a = np.zeros(100)
    n = 1
    a[:n] = 1
    np.random.shuffle(a)
    core = a.reshape((10, 10))

    state[45:55, 45:55] = core
    filename = f'input/init2d{n:03}'
    write_state(state, filename)
    state = read_state(filename)
    print(state)
    plot_state(state, filename)