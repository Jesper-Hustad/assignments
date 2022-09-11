% Author: Jesper Hustad

% a)
local Factorial in
    fun {Factorial Start} in
        if Start == 1 then 1.0 else
            {Int.toFloat Start} * {Factorial Start-1}
        end
    end
    {System.showInfo {Factorial 8}}
end