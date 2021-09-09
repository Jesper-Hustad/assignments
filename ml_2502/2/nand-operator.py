import numpy as np
import matplotlib
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import axes3d, art3d

import torch

matplotlib.rcParams.update({'font.size': 11})

W_init = torch.tensor([[3.66], [8.66]], requires_grad=True)
b_init = torch.tensor([[-3.87]], requires_grad=True)


def sigmoid(t):
    return torch.sigmoid(t)


class SigmoidModel:
    def __init__(self):
        self.W = torch.tensor([[8.66], [8.66]], requires_grad=True)
        self.b = torch.tensor([[-3.87]], requires_grad=True)


    # Predictor
    def f(self, x):
        return sigmoid(x @ self.W + self.b)

    # Uses Cross Entropy
    def loss(self, x, y):
        return -torch.mean(torch.multiply(y, torch.log(self.f(x))) + torch.multiply((1 - y), torch.log(1 - self.f(x))))


model = SigmoidModel()

# Observed/training input and output
x_train = torch.tensor([[0, 0], [0, 1], [1, 0], [1, 1]]).float()
y_train = torch.tensor([[1], [0], [0], [0]]).float()

print(f"{x_train[0]}")
print(f"{y_train[0]}")
print(f"{model.loss(x_train[0],y_train[0]) =}")

# set constants
epoch_count = 10000
step_length = 0.1

# Optimize: adjust W and b to minimize loss using stochastic gradient descent
optimizer = torch.optim.SGD([model.W, model.b], step_length)

for epoch in range(epoch_count):
    
    model.loss(x_train, y_train).backward()  # Compute loss gradients

    optimizer.step()  # Perform optimization by adjusting W and b,

    optimizer.zero_grad()  # Clear gradients for next step


fig = plt.figure("Logistic regression: the logical OR operator")

plot1 = fig.add_subplot(111, projection='3d')

plot1_f = plot1.plot_wireframe(np.array([[]]), np.array([[]]), np.array([[]]), color="green", label="$\\hat y=f(\\mathbf{x})=\\sigma(\\mathbf{xW}+b)$")

plot1.plot(x_train[:, 0].squeeze(), x_train[:, 1].squeeze(), y_train[:, 0].squeeze(), 'o', label="$(x_1^{(i)}, x_2^{(i)},y^{(i)})$", color="blue")

plot1_info = fig.text(0.01, 0.02, "")

plot1.set_xlabel("$x_1$")
plot1.set_ylabel("$x_2$")
plot1.set_zlabel("$y$")
plot1.legend(loc="upper left")
plot1.set_xticks([0, 1])
plot1.set_yticks([0, 1])
plot1.set_zticks([0, 1])
plot1.set_xlim(-0.25, 1.25)
plot1.set_ylim(-0.25, 1.25)
plot1.set_zlim(-0.25, 1.25)

table = plt.table(cellText=[[0, 0, 0], [0, 1, 0], [1, 0, 0], [1, 1, 0]],
                  colWidths=[0.1] * 3,
                  colLabels=["$x_1$", "$x_2$", "$f(\\mathbf{x})$"],
                  cellLoc="center",
                  loc="lower right")


def update_figure():

    global plot1_f
    plot1_f.remove()
    x1_grid, x2_grid = np.meshgrid(np.linspace(-0.25, 1.25, 10), np.linspace(-0.25, 1.25, 10))
    y_grid = np.empty([10, 10])
    for i in range(0, x1_grid.shape[0]):
        for j in range(0, x1_grid.shape[1]):
            y_grid[i, j] = model.f( torch.tensor([[x1_grid[i, j], x2_grid[i, j]]]).float() )
    plot1_f = plot1.plot_wireframe(x1_grid, x2_grid, y_grid, color="green")

    plot1_info.set_text(
        "$\\mathbf{W}=\\genfrac{[}{]}{0}{}{%.2f}{%.2f}$\n$b=[%.2f]$\n$loss = -\\frac{1}{N}\\sum_{i=1}^{N}\\left [ y^{(i)} \\log\\/f(\\mathbf{x}^{(i)}) + (1-y^{(i)}) \\log (1-f(\\mathbf{x}^{(i)})) \\right ] = %.2f$"
        % (model.W[0, 0], model.W[1, 0], model.b[0, 0], model.loss(x_train, y_train)))

    table._cells[(1, 2)]._text.set_text("${%.1f}$" % model.f(torch.tensor([[0, 0]]).float()))
    table._cells[(2, 2)]._text.set_text("${%.1f}$" % model.f(torch.tensor([[0, 1]]).float()))
    table._cells[(3, 2)]._text.set_text("${%.1f}$" % model.f(torch.tensor([[1, 0]]).float()))
    table._cells[(4, 2)]._text.set_text("${%.1f}$" % model.f(torch.tensor([[1, 1]]).float()))

    fig.canvas.draw()


update_figure()



plt.show()
