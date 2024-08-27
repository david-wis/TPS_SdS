import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
def poly_analysis(mass_list, filename, degree):
    #fit cubic regression model
    xs = list(range(len(mass_list)))

    model = np.poly1d(np.polyfit(xs , mass_list, degree))

    #add fitted cubic regression line to scatterplot
    polyline = np.linspace(0, len(mass_list))
    plt.scatter(xs, mass_list)
    plt.plot(polyline, model(polyline))

    #add axis labels
    plt.xlabel('epoch')
    plt.ylabel('mass')
    plt.title(f'Mass over time:\n {str(model)} \n{(-2) * model.r}')
    #display plot
    plt.savefig(filename)

if __name__ == '__main__':
    def set_ticks(ax):
        ax.set_xticks([0.5,1.5])
        ax.set_yticks([0.5,1.5])
        ax.set_xticklabels([])
        ax.set_yticklabels([])
        ax.grid(color='black', linewidth=1)
    fig, axs = plt.subplots(2,3)
    axs[0,0].imshow(np.array([[1,0,0],[0,0,0],[0,0,0]]), cmap='binary')
    set_ticks(axs[0,0])
    axs[0,1].imshow(np.array([[0,1,0],[0,0,0],[0,0,0]]), cmap='binary')
    set_ticks(axs[0,1])
    axs[0,2].imshow(np.array([[1,0,1],[0,0,0],[0,0,0]]), cmap='binary')
    set_ticks(axs[0,2])
    axs[1,0].imshow(np.array([[1,1,0],[0,0,0],[0,0,0]]), cmap='binary')
    set_ticks(axs[1,0])
    axs[1,1].imshow(np.array([[0,1,1],[0,0,0],[0,0,0]]), cmap='binary')
    set_ticks(axs[1,1])
    axs[1,2].imshow(np.array([[1,1,1],[0,0,0],[0,0,0]]), cmap='binary')
    set_ticks(axs[1,2])

    fig.savefig('../docs/Images/fill_example_1a.png')
    cmap = mpl.colors.LinearSegmentedColormap.from_list("", ['white','grey','green'])
    fig, axs = plt.subplots(2,3)
    axs[0,0].imshow(np.array([[2,1,1],[1,0,1],[1,1,2]]), cmap=cmap)
    set_ticks(axs[0,0])
    axs[0,1].imshow(np.array([[1,2,1],[2,0,1],[1,1,1]]), cmap=cmap)
    set_ticks(axs[0,1])
    axs[0,2].imshow(np.array([[2,1,1],[1,0,2],[1,1,1]]), cmap=cmap)
    set_ticks(axs[0,2])

    fig.savefig('../docs/Images/fill_example_1b.png')