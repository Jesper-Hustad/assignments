% Author: Jesper Hustad

% a)
declare fun {Lex Input} {String.tokens Input 32} end 

% b)
declare fun {Token Lexem}
    if     Lexem == "+" then operator(type:plus)
    elseif Lexem == "-" then operator(type:minus)
    elseif Lexem == "*" then operator(type:multiply)
    elseif Lexem == "/" then operator(type:divide)
    elseif Lexem == "p" then command(print)
    elseif Lexem == "d" then command(duplicate)
    elseif Lexem == "i" then command(inverse)
    elseif Lexem == "c" then command(clear)
    else number({String.toInt Lexem}) end
end

declare fun {Tokenize Lexemes} {Map Lexemes Token} end

% c)
declare fun {Calc Operator S} N1=S.1 N2=S.2.1 in
    % calc = {
    %     plus: (N1, N2) => n1+ n2,
    %     minus: (N1, N2 ) => n1-n2,
    % }
    if     Operator == plus     then N1 + N2
    elseif Operator == minus    then N1 - N2
    elseif Operator == multiply then N1 * N2
    elseif Operator == divide   then N1 / N2
    end
end



% d) e) f) g)
declare fun {Command Command S}
    if     Command == print     then {Show S} S
    elseif Command == duplicate then S.1 | S 
    elseif Command == inverse   then S.1 * ~1 | S.2 
    elseif Command == clear     then {List.make 0}
    end
end

declare fun {ParseToken T S} CA in
    CA = Calculations(
        plus:     fun {$ N1 N2} N1 + N2 end 
        minus:    fun {$ N1 N2} N1 - N2 end 
        multiply: fun {$ N1 N2} N1 * N2 end 
        divide:   fun {$ N1 N2} N1 + N2 end 
    )
    case T of 
       number(N)        then N | S
    [] operator(type:O) then {#CA.O S.1 S.2.1} | S.2.2 
    % [] operator(type:O) then {Calc O S} | S.2.2 
    [] command(C)       then {Command C S}
    end
end

declare fun {InterpretRec Token Stack} if Token==nil then Stack else 
    {InterpretRec Token.2 {ParseToken Token.1 Stack}} end 
end

declare fun {Interpret Tokens} {InterpretRec Tokens {List.make 0}} end
    

{Show {Interpret {Tokenize {Lex "1 2 3 + d"}}}}