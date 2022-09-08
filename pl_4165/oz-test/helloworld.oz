if (~2 < ~5) then
    {Browse less}
else
    {Browse more}
end

local
    X
    %% function definition
    fun {Max X Y}
       if X >= Y then X else Y end
    end
  in
    %% function application
    {Browse {Max 7 3}}
  end

  local
    fun {Factorial N}
       if N==0
       then 1
       else N * {Factorial N-1}
       end
    end
 in
    %% 10! = 3628800
    {Browse
     {Factorial 10}}
 end


% fun {mdc S}
%     if S==0
%     then 1
%     else S * {mdc S-1}
%     end
% end


local S1 S2 X in
    {String.token "3:" &: S1 S2}
    {String.toInt S1 X}
    {Browse X}
    if X == nil then {Browse S2} else end
    
end