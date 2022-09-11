% Author: Jesper Hustad

% a)
fun {Length List} in
    fun {ListRec List Depth} in
        if List.2 == Nil then Depth else
            {ListRec List Depth+1}
        end
    end
    {List.length List 0}
end

% b)
fun {Take List Count} in
    fun {TakeRec List Count Result} in
        if Count == 0 then Result else
            {TakeRec List.2 Count-1 {List.append Result List.1}}
        end
    end
    if Count > {Length List} then List else
        {List.take List Count []}
    end
end

% c)
fun {Drop List Count} in
    if Count < {Lenght list} then nil end
    if Count == 0 then List else {Drop List.2 Count-1} end
end

% d)
fun {Append List1 List2} in
    if List2.1 == nil then List1 else
        {Append List1|List2.1 List2.2}
    end
end

% e)
fun {Member List Element} in
    for X in List do
        if X == Element then true end
    end
    false
end

% f)
fun {Position List Element} in
    fun {PositionRec List Element P} in
        if List.1 == Element then P else
            {PositionRec List.2 Element P+1}
        end
    end
    {PositionRec List Element 0}
end
