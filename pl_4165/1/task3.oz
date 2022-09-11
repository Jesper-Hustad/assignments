% Author: Jesper Hustad

% a)
local Z = 30 Y = 300 in
    X = Y * X 
end

% b)
% From Mozart OZ documentation "Each thread is a data-flow thread that blocks on data dependency."
% This means that the thread waits until Y is declared before running

% This is useful because threads can wait until each 
% dependency is resolved without blocking the program

% Y = X binds Y with the value of X, the data-flow thread becomes unblocked and is run
local X Y in
    X = "This is a string"
    thread {System.showInfo Y} end
    Y = X
end
