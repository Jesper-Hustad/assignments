% Author: Jesper Hustad

% a)
local Max
    fun {Max Number1 Number2}
        if Number1 > Number2 then Number1 else Number2 end
    end
end

% b)
local PrintGreater
    proc {PrintGreater Number1 Number2}
        {System.showInfo {Max Number1 Number2}}
    end
end