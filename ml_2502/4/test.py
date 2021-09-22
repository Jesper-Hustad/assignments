target = " hello world"

target = "qqqqqqqq"

def char_encode(position, length):
    print(f"{position%length= } {length =}")
    arr = [0.] * length
    arr[position%length] = 1.
    print(f"{arr = }")
    return arr

a = [char_encode(x,len(target)) for x in range(len(target))]
print(a)

# char_encodings = [
#     [1., 0., 0., 0., 0.],  # ' '
#     [0., 1., 0., 0., 0.],  # 'h'
#     [0., 0., 1., 0., 0.],  # 'e'
#     [0., 0., 0., 1., 0.],  # 'l'
#     [0., 0., 0., 0., 1.],  # 'o'
# ]

# 8