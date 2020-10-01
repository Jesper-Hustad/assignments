import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Dllist {
    public static void main(String[] args) {

//        String[] args = new String[]{"15","+","10"};


        if(args.length != 3){
            System.out.println("Wrong arguments, need number a, operator, and number b");
            System.exit(0);
        }


        ListIterator<Integer> a = makedllist(args[0]);
        ListIterator<Integer> b = makedllist(args[2]);
        LinkedList<Integer> result = args[1].equals("+") ? addLinked(a,b) : subLinked(a,b);

        System.out.println("  " + args[0]);
        System.out.println(args[1] + " " + args[2]);
        System.out.println("= " + Arrays.stream(result.toArray()).map(String::valueOf).collect(Collectors.joining("")));

    }

    private static ListIterator<Integer> makedllist(String nums) {

        LinkedList<Integer> numbers= new LinkedList<>();

        char[] aChar = nums.toCharArray();
        IntStream.range(0, aChar.length).map(i -> Integer.parseInt(String.valueOf(aChar[i]))).forEach(numbers::add);

        ListIterator<Integer> iterator = numbers.listIterator();
//      move to last element in linked list
        while(iterator.hasNext()) iterator.next();

        return iterator;
    }


    private static LinkedList<Integer> addLinked(ListIterator<Integer> a, ListIterator<Integer> b){
        return addLinked(a, b, false, new LinkedList<Integer>());
    }

    private static LinkedList<Integer> addLinked(ListIterator<Integer> a, ListIterator<Integer> b, boolean hasCarry, LinkedList<Integer> result) {

        if (!a.hasPrevious() && !b.hasPrevious()){
            if(hasCarry) result.addFirst(1);
            return result;
        }

        int addition = (a.hasPrevious() ? a.previous() : 0 ) + (b.hasPrevious() ? b.previous() : 0 ) + (hasCarry ? 1:0);

        int current = addition % 10;
        result.addFirst(current);

        boolean nextCarry = addition / 10 == 1;
        return addLinked(a, b, nextCarry, result);
    }


    private static LinkedList<Integer> subLinked(ListIterator<Integer> a, ListIterator<Integer> b){
        return subLinked(a, b, false, new LinkedList<Integer>());
    }

    private static LinkedList<Integer> subLinked(ListIterator<Integer> a, ListIterator<Integer> b, boolean hasLoan, LinkedList<Integer> result) {

        if (!a.hasPrevious() && !b.hasPrevious()){
            if(result.getFirst() == 0) result.pop();
            return result;
        }

        int subtraction = (a.hasPrevious() ? a.previous() : 0 ) - (b.hasPrevious() ? b.previous() : 0 ) - (hasLoan ? 1:0);

        boolean needsLoan = subtraction < 0;
        if(needsLoan) subtraction += 10;

        int current = subtraction % 10;
        result.addFirst(current);

        return subLinked(a, b, needsLoan, result);
    }


}
