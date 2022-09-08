% Author: Jesper Hustad

% a)
local Circle in
    proc {Circle R} A D C PI in
        PI = 355.0/113.0
        A = PI * R * R
        D = 2.0 * R
        C = PI * D
        {System.showInfo A}
        {System.showInfo D}
        {System.showInfo C}
    end
    { Circle 4.0 }
end

