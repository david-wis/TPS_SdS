import numpy as np
import matplotlib.pyplot as plt

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
    fig, axs = plt.subplots(2,3)
    axs[0,0].imshow(np.array([[1,0,0],[0,0,0],[0,0,0]]), cmap='binary')
    # axs[0,0].grid(color='white', linewidth=2)
    plt.xticks([])
    plt.yticks([])
    axs[0,1].imshow(np.array([[0,1,0],[0,0,0],[0,0,0]]), cmap='binary')
    # axs[0,1].grid(color='white', linewidth=2)
    plt.xticks([])
    plt.yticks([])
    axs[0,2].imshow(np.array([[0,0,1],[0,0,0],[0,0,0]]), cmap='binary')
    # axs[0,2].grid(color='white', linewidth=2)
    plt.xticks([])
    plt.yticks([])
    axs[1,0].imshow(np.array([[1,1,0],[0,0,0],[0,0,0]]), cmap='binary')
    # axs[1,0].grid(color='white', linewidth=2)
    plt.xticks([])
    plt.yticks([])
    axs[1,1].imshow(np.array([[0,1,1],[0,0,0],[0,0,0]]), cmap='binary')
    # axs[1,1].grid(color='white', linewidth=2)
    plt.xticks([])
    plt.yticks([])
    axs[1,2].imshow(np.array([[1,1,1],[0,0,0],[0,0,0]]), cmap='binary')
    # axs[1,2].grid(color='white', linewidth=2)
    plt.xticks([])
    plt.yticks([])
    fig.savefig('../docs/Images/fill_example_1a.png')