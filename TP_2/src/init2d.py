import numpy as np
import matplotlib.pyplot as plt

#write s to file
def write_state(state, filename):
    with open(filename, 'w') as f:
        f.write(f"{state.shape[0]}\n")
        f.write(f"{state.shape[1]}\n")
        f.write(' '.join(map(lambda x: str(int(x)), state.flatten())))

def plot_state(state):
    plt.imshow(state, cmap='binary')
    plt.show()

def read_state(filename):
    with open(filename) as f:
        rows = f.readline()
        cols = f.readline()
        line = f.readline()
        state = np.array(list(map(int, line.split()))).reshape((int(rows), int(cols)))
    return state

if __name__ == '__main__':
    state = np.zeros((100, 100))
    #core random with 10 1's
    a = np.zeros(100)
    n = 100
    a[:n] = 1
    np.random.shuffle(a)
    core = a.reshape((10, 10))

    state[45:55, 45:55] = core
    plot_state(state)
    print(state)
    filename = f'input/init2d{n}.txt'
    write_state(state, filename)
    state = read_state(filename)
    print(state)
    plot_state(state)