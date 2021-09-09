import numpy as np
import matplotlib
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import axes3d, art3d
import torch

matplotlib.rcParams.update({'font.size': 11})

W1_init = torch.tensor([[-1.0, -1.0], [1.0, -1.0]], requires_grad=True)
b1_init = torch.tensor([[0.0, 0.0]], requires_grad=True)
W2_init = torch.tensor([[1.0], [1.0]], requires_grad=True)
b2_init = torch.tensor([[0.0]], requires_grad=True)


# Also try:
# W1_init = torch.tensor([[7.43929911, 5.68582106], [7.44233704, 5.68641663]], requires_grad=True)
# b1_init = torch.tensor([[-3.40935969, -7.69532299]], requires_grad=True)
# W2_init = torch.tensor([[10.01280117], [-13.79168701]], requires_grad=True)
# b2_init = torch.tensor([[-6.1043458]], requires_grad=True)


def sigmoid(t):
    return torch.sigmoid(t)


class SigmoidModel:
    def __init__(self, W1=W1_init, W2=W2_init, b1=b1_init, b2=b2_init):
        self.W1 = W1
        self.W2 = W2
        self.b1 = b1
        self.b2 = b2

    # First layer function
    def f1(self, x):
        return sigmoid(x @ self.W1 + self.b1)

    # Second layer function
    def f2(self, h):
        return sigmoid(h @ self.W2 + self.b2)

    # Predictor
    def f(self, x):
        return self.f2(self.f1(x))

    # Uses Cross Entropy
    def loss(self, x, y):
        # return torch.nn.functional.binary_cross_entropy_with_logits(self.f(x),y)
        return -torch.mean(torch.multiply(y, torch.log(self.f(x))) + torch.multiply((1 - y), torch.log(1 - self.f(x))))


model = SigmoidModel()

# Observed/training input and output
x_train = torch.tensor([[0, 0], [0, 1], [1, 0], [1, 1]]).float()
y_train = torch.tensor([[0], [1], [1], [0]]).reshape(-1,1).float()

################# TEST ####################
print(f"{x_train[0]}")
print(f"{y_train[0]}")
print(f"{model.loss(x_train[0],y_train[0]) =}")

# set constants
epoch_count = 20000
step_length = 0.05

# Optimize: adjust W and b to minimize loss using stochastic gradient descent
optimizer = torch.optim.SGD([model.W1, model.W2, model.b1, model.b2], step_length)

for epoch in range(epoch_count):

    if(epoch%100 == 0): 
        print(f"epoch: {epoch}  | progress: {(epoch/epoch_count)*100:.2f}%", end = "\r")
    
    model.loss(x_train, y_train).backward()  # Compute loss gradients

    optimizer.step()  # Perform optimization by adjusting W and b,

    optimizer.zero_grad()  # Clear gradients for next step






fig = plt.figure("Logistic regression: the logical XOR operator")

plot3 = fig.add_subplot(projection='3d')

plot3_f = plot3.plot_wireframe(np.array([[]]),
                               np.array([[]]),
                               np.array([[]]),
                               color="green",
                               label="$\\hat y=f(\\mathbf{x})=\\mathrm{f2}(\\mathrm{f1}(\\mathbf{x}))$")

plot3.plot(x_train[:, 0].squeeze(), x_train[:, 1].squeeze(), y_train[:, 0].squeeze(), 'o', label="$(x_1^{(i)}, x_2^{(i)},y^{(i)})$", color="blue")


plot3_info = fig.text(0.3, 0.03, "")

plot3.set_xlabel("$x_1$")
plot3.set_ylabel("$x_2$")
plot3.set_zlabel("$y$")
plot3.legend(loc="upper left")
plot3.set_xticks([0, 1])
plot3.set_yticks([0, 1])
plot3.set_zticks([0, 1])
plot3.set_xlim(-0.25, 1.25)
plot3.set_ylim(-0.25, 1.25)
plot3.set_zlim(-0.25, 1.25)

table = plt.table(cellText=[[0, 0, 0], [0, 1, 0], [1, 0, 0], [1, 1, 0]],
                  colWidths=[0.15] * 3,
                  colLabels=["$x_1$", "$x_2$", "$f(\\mathbf{x})$"],
                  cellLoc="center",
                  bbox=[1.0, 0.0, 0.5, 0.5])


def update_figure():

    global plot3_f
    plot3_f.remove()
    x1_grid, x2_grid = np.meshgrid(np.linspace(-0.25, 1.25, 10), np.linspace(-0.25, 1.25, 10))
    h1_grid = np.empty([10, 10])
    h2_grid = np.empty([10, 10])
    f2_grid = np.empty([10, 10])
    f_grid = np.empty([10, 10])
    for i in range(0, x1_grid.shape[0]):
        for j in range(0, x1_grid.shape[1]):
            h = model.f1(torch.tensor([[x1_grid[i, j], x2_grid[i, j]]]).float())
            h1_grid[i, j] = h[0, 0]
            h2_grid[i, j] = h[0, 1]
            f2_grid[i, j] = model.f2(torch.tensor([[x1_grid[i, j], x2_grid[i, j]]]).float())
            f_grid[i, j] = model.f(torch.tensor([[x1_grid[i, j], x2_grid[i, j]]]).float())


    plot3_f = plot3.plot_wireframe(x1_grid, x2_grid, f_grid, color="green")

    plot3_info.set_text(
        "$loss = -\\frac{1}{N}\\sum_{i=1}^{N}\\left [ y^{(i)} \\log\\/f(\\mathbf{x}^{(i)}) + (1-y^{(i)}) \\log (1-f(\\mathbf{x}^{(i)})) \\right ] = %.2f$" %
        model.loss(x_train, y_train))

    table._cells[(1, 2)]._text.set_text("${%.1f}$" % model.f(torch.tensor([[0, 0]]).float()))
    table._cells[(2, 2)]._text.set_text("${%.1f}$" % model.f(torch.tensor([[0, 1]]).float()))
    table._cells[(3, 2)]._text.set_text("${%.1f}$" % model.f(torch.tensor([[1, 0]]).float()))
    table._cells[(4, 2)]._text.set_text("${%.1f}$" % model.f(torch.tensor([[1, 1]]).float()))

    fig.canvas.draw()


update_figure()



plt.show()
