import numpy as np
import matplotlib.pyplot as plt

#write s to file

if __name__ == '__main__':
    n = 35
    filename = f'input/init2d_{n}'
    fig, axs = plt.subplots(2, 5, figsize=(20, 10))
    x, y = 100, 100
    with open(f"{filename}.txt", 'w') as f:
        f.write(f"{x}\n")
        f.write(f"{y}\n")

        for i in range(2):
            for j in range(5):
                state = np.zeros((x, y))
                #core random with 10 1's
                a = np.zeros(100)
                a[:n] = 1
                np.random.shuffle(a)
                core = a.reshape((10, 10))

                state[45:55, 45:55] = core
                f.write(''.join(map(lambda x: str(int(x)), state.flatten())) + '\n')
                axs[i,j].imshow(state, cmap='binary')
    plt.savefig(f'{filename}.png')