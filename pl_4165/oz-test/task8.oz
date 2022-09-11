% Author: Jesper Hustad

\insert '/Users/jesperhustad/Documents/assignments/pl_4165/oz-test/list.oz'

fun {Push List Element} in
    {Append [Element] List}
end

fun {Peek List} in 
    List.1 
end

fun {Pop List} in
    {Drop List 1}
end


