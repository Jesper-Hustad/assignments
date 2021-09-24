import torch
import torch.nn as nn


class LongShortTermMemoryModel(nn.Module):
    def __init__(self, input_size, encoding_size):
        super(LongShortTermMemoryModel, self).__init__()

        self.lstm = nn.LSTM(input_size, 128)  # 128 is the state size
        self.dense = nn.Linear(128, encoding_size)  # 128 is the state size

    def reset(self):  # Reset states prior to new input sequence
        zero_state = torch.zeros(1, 1, 128)  # Shape: (number of layers, batch size, state size)
        self.hidden_state = zero_state
        self.cell_state = zero_state

    def logits(self, x):  # x shape: (sequence length, batch size, encoding size)
        out, (self.hidden_state, self.cell_state) = self.lstm(x, (self.hidden_state, self.cell_state))
        return self.dense(out.reshape(-1, 128))

    def f(self, x):  # x shape: (sequence length, batch size, encoding size)
        return torch.softmax(self.logits(x), dim=1)

    def loss(self, x, y):  # x shape: (sequence length, batch size, encoding size), y shape: (sequence length, encoding size)
        return nn.functional.cross_entropy(self.logits(x), y.argmax(1))



index_to_char = list(' hatrcflmpson') # array of chars

char_encodings = torch.eye(len(index_to_char)).numpy() # identity matrix
char_enc = char_encodings

def train(string): # turns string into list of character codes, train('hii') = [[1, 0], [0, 1], [0, 1]] 
    return [char_encodings[index_to_char.index(x)] for x in list(string)]

# print(f"{train('hat') = }")

# x_train = torch.tensor([
#     [train('hat ')],
#     [train('rat ')],
#     [train('cat ')],
#     [train('flat')],
#     [train('matt')],
#     [train('cap ')],
#     [train('son ')],
# ])
x_train = torch.tensor([
    [[char_enc[1]], [char_enc[2]], [char_enc[3]], [char_enc[0]]],
    [[char_enc[4]], [char_enc[2]], [char_enc[3]], [char_enc[0]]],
    [[char_enc[5]], [char_enc[2]], [char_enc[3]], [char_enc[0]]],
    [[char_enc[6]], [char_enc[7]], [char_enc[2]], [char_enc[3]]],
    [[char_enc[8]], [char_enc[2]], [char_enc[3]], [char_enc[3]]],
    [[char_enc[5]], [char_enc[2]], [char_enc[9]], [char_enc[0]]],
    [[char_enc[10]], [char_enc[11]], [char_enc[12]], [char_enc[0]]]])  # 'hat ', 'rat ', 'cat ', 'flat', 'matt', 'cap ', 'son '
    

index_to_emoji = ['🎩', '🐀', '🐱', '🏢', '🙃', '🧢', '👦']


emoji_encodings = torch.eye(len(index_to_emoji)).numpy() # identity matrix

y_train = torch.tensor([
    [emoji_encodings[0]] * 4,
    [emoji_encodings[1]] * 4,
    [emoji_encodings[2]] * 4,
    [emoji_encodings[3]] * 4,
    [emoji_encodings[4]] * 4,
    [emoji_encodings[5]] * 4,
    [emoji_encodings[6]] * 4,
])


input_size = len(index_to_char)     # shape in
encoding_size = len(index_to_emoji) # shape out

model = LongShortTermMemoryModel(input_size, encoding_size)


def toEmoji(word):
    model.reset()
    y = None
    for c in list(word):
        x = char_encodings[index_to_char.index(c)]
        y = model.f(torch.tensor([[x]]))
    return index_to_emoji[y.argmax(1)]


optimizer = torch.optim.RMSprop(model.parameters(), 0.001)
for epoch in range(500):
    for i in range(x_train.size()[0]):
        model.reset()
        model.loss(x_train[i], y_train[i]).backward()
        optimizer.step()
        optimizer.zero_grad()

    if epoch % 1 == 0:
        # text = ['ht', 'rats', 'atc', 'flap','mats','rap', 'sot', 'hat', 'rat', 'cat', 'flat', 'matt', 'cap', 'son']
        text = ['ht', 'rt', 'rats', 'atc', 'flap','flat', 'cap', 'son']
        print()
        for t in text:
            print(f"{t}{toEmoji(t)}   ", end = "")