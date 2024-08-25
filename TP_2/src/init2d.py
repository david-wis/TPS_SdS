import numpy as np
import matplotlib.pyplot as plt

#write s to file

if __name__ == '__main__':
    total_l = 100
    core_l = 10
    for n in [60,70,80,90]:
        fig, axs = plt.subplots(2, 5, figsize=(20, 10))
        filename = f'input/init2d_{n}'
        with open(f"{filename}.txt", 'w') as f:
            f.write(f"{total_l}\n")
            f.write(f"{core_l}\n")

            for i in range(2):
                for j in range(5):
                    state = np.zeros((total_l, total_l))
                    #core random with 10 1's
                    a = np.zeros(core_l**2)
                    a[:n] = 1
                    np.random.shuffle(a)
                    core = a.reshape((core_l, core_l))
                    start = total_l//2-core_l//2
                    end = total_l//2+core_l//2
                    state[start:end, start:end] = core
                    f.write(''.join(map(lambda x: str(int(x)), state.flatten())) + '\n')
                    axs[i,j].imshow(state, cmap='binary')
        plt.savefig(f'{filename}.png')

