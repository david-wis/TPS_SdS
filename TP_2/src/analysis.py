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