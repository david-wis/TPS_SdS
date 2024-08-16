import numpy as np
import matplotlib.pyplot as plt

with open('output.txt') as f:
    rows = int(f.readline())
    cols = int(f.readline())
    history = [ np.array(list(map(int, f.readline().split()))) for _ in range(rows) ]
    #plot gif with history
    fig, ax = plt.subplots()
    ax.set_xticks([])
    ax.set_yticks([])

    
