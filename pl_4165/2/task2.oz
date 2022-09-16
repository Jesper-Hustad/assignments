% Author: Jesper Hustad

\insert '/Users/jesperhustad/Documents/assignments/pl_4165/2/task1.oz'

% a)
declare fun {MakeExpression Operator S} N1=S.1 N2=S.2.1 in
    if     Operator == plus     then plus(N1 N2)
    elseif Operator == minus    then minus(N1 N2)
    elseif Operator == multiply then multiply(N1 N2)
    elseif Operator == divide   then divide(N1 N2)
    end
end

declare fun {ExpressionTreeInternal Tokens ExpressionStack} T=Tokens S=ExpressionStack in
    
    % all tokens already processed
    case T of nil then S.1
    
    % add numbers to expression stack
    [] number(N)|_ then {ExpressionTreeInternal T.2  N|S}
    
    % if operator: make corresponding expression and remove 2 from stack
    [] operator(type:O)|_ then {ExpressionTreeInternal T.2 {MakeExpression O S}|S.2.2}
    
    end
end

declare fun {ExpressionTree Tokens} {ExpressionTreeInternal Tokens {List.make 0}} end

% {Show {ExpressionTree {Tokenize {Lex "1 2 + 3 -"}}}}