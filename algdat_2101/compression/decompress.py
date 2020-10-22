from bitstring import BitArray
import sys

def file_to_byes(path):
    f=open(path,"rb")
    file_bytes = f.read()
    return file_bytes

def bytes_to_file(bytes_array, path):
    f=open(path,"wb")
    f.write(bytes_array)
    f.close()


def hf_decompress(byte_array: BitArray):

    bit_array = BitArray(byte_array)
    
    # get constant vars from start of file
    end_padding = int(bit_array[0:8].bin ,2)
    n_codes = int(bit_array[8:16].bin ,2)

    bit_array = bit_array[:-1*end_padding]

    codes = []

    pos = 16
    for i in range(n_codes):
        key = bit_array[pos:pos+8].bin
        c_length = int(bit_array[pos+8:pos+8+4].bin ,2)
        code = bit_array[pos+8+4:pos+8+4+c_length].bin
        codes.append((code,key))
        pos += c_length + 8 + 4

    result = bytearray()
    
    prev_find = 0
    offset = 1

    while len(bit_array) > pos:

        selection = bit_array[pos:pos+offset].bin
        found = [c for c in codes if c[0]==selection]

        if found:
            result.append(int(found.pop()[1],2))
            pos += offset
            offset = 1
        else:
            offset += 1

    return result

def lz_decompress(byte_arr):

    refrence_value_pairs = [(byte_arr[i*2], byte_arr[i*2+1]) for i in range(len(byte_arr)//2)]


    MAX_SIZE = 255
    codebook = [() for i in range(MAX_SIZE)]

    result = bytearray()

    for refrence, value in refrence_value_pairs:

        refrence_val = codebook[refrence] if refrence != MAX_SIZE else ()

        codebook.append((*refrence_val,value))
        codebook.pop(0)

        result.extend((*refrence_val, value))

    # add last item if it wasnt refrenced
    if byte_arr[-1] != MAX_SIZE: result.extend((codebook[byte_arr[-1]]))

    return result  

# main

path = sys.argv[1]

f = file_to_byes(path)

hf = hf_decompress(f)

original = lz_decompress(hf)

new_file_name = 'new_'+path[:-5]

bytes_to_file(original, new_file_name)

print(f"decompressed file to: {new_file_name}")