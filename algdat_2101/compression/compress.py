import sys

def file_to_byes(path):
    f=open(path,"rb")
    file_bytes = f.read()
    return file_bytes

def bytes_to_file(bytes_array, path):
    f=open(path,"wb")
    f.write(bytes_array)
    f.close()

# lempel-ziv compression
def lz_compress(byte_array):
    MAX_SIZE = 255
    codebook = [[] for i in range(MAX_SIZE)]
    last_index = MAX_SIZE
    result = bytearray()

    current_matches = bytearray()

    for b in byte_array:
        
        try:
            # find refrence matching current block
            last_index = codebook.index([*current_matches,b])

        except ValueError:

            # add new block for future refrences and remove first element to keep size constant
            codebook.append([*current_matches,b])
            codebook.pop(0)
            
            # add refrence and value to output
            result.extend(((last_index), b))

            # reset vars for next block
            last_index=MAX_SIZE
            current_matches = bytearray()

        else:
            # found match for previous refrence, continue
            current_matches.append(b)

    result.append(last_index)
    
    return result

# huffman compression
from bitstring import BitArray

class Node:
    def __init__(self, freq, val, left=None, right=None):
        self.freq = freq
        self.val = val
        self.left = left
        self.right = right

    def __gt__(self, node_2):
        self.freq > node_2.freq

def parse_tree(tree, code=''):

    if tree.val is not None:
        return [(tree.val,code)]
    else:
        left = parse_tree(tree.left,code + '0') if tree.left is not None else ()
        right = parse_tree(tree.right,code + '1') if tree.right is not None else ()

        return [*left, *right]
        
def create_huffman_tree(freq_table: bytes):
    
    # remove bytes with no frequency and create (freq, val) tuples
    freq_tuples = [Node(freq_table[i], i) for i in range(len(freq_table)) if freq_table[i] != 0]

    while len(freq_tuples) > 1:

        freq_tuples.sort(key=lambda a: a.freq)

        # get smallest possible left and right node
        
        left = freq_tuples.pop(0)
        right = freq_tuples.pop(0)

        # sum the tuple frequency
        node_freq = left.freq + right.freq

        # combine tuple value as new branch
        # node_branch = (left[1], right[1])
        new_node = Node(node_freq, None, left, right)
        
        freq_tuples.append(new_node)
 
    return freq_tuples.pop()

# helper method binary padding for integers
def bin_pad(number, padding):
    return f'0b{bin(number)[2:]:0>{padding}}'

def hf_compress(byte_array):
    MAX_SIZE = 256

    freq_table = [0 for i in range(MAX_SIZE)]
    for b in byte_array: freq_table[b] += 1
    
    hf_tree = create_huffman_tree(freq_table)
    hf_codes = parse_tree(hf_tree)

    result = BitArray(bin_pad(len(hf_codes),8))


    for n in hf_codes: 
        
        byte_key = bytes([n[0]])
        result.append(byte_key)

        code_length = bin_pad(len(n[1]),4)
        result.append(code_length)
        result.append('0b' + n[1])


    for b in byte_array:
        code = [c[1] for c in hf_codes if c[0] == b].pop()
        result.append('0b' + code)

    # find required padding to make bits go into bytes
    padding_size = 8 - len(result.bin) % 8
    result.prepend(bin_pad(padding_size,8))

    return result.tobytes()

# main

path = sys.argv[1]

f = file_to_byes(path)

lz = lz_compress(f)

hf = hf_compress(lz)

new_file_name =  path + '.lzhf'

bytes_to_file(hf, new_file_name)

print(f"created '{new_file_name}' with {100*(1-(len(hf)/len(f))):0.1f}% space reduction")

