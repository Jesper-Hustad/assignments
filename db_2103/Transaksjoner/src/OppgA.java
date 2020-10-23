import java.sql.*;
import java.util.Scanner;

public class OppgA {

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/jesperfh?user=jesperfh&password=ysJxYhlY");

        String query1 = "SELECT boktittel.forfatter, boktittel.tittel ,COUNT(eksemplar.isbn) as eksemplarer FROM `boktittel`\n" +
                "INNER join eksemplar on boktittel.isbn = eksemplar.isbn\n" +
                "WHERE boktittel.isbn=?";

        PreparedStatement preparedStatement = connection.prepareStatement(query1);

        Scanner sc= new Scanner(System.in);

        System.out.println("Skriv en isbn");
        preparedStatement.setString(1,sc.next());

        ResultSet result = preparedStatement.executeQuery();

        result.next();

        System.out.println(String.format(
                "Forfatter %s, Tittel %s, Eksemplarer %s",
                result.getString("forfatter"),
                result.getString("tittel"),
                result.getString("eksemplarer")
        ));



        String query2 = "update eksemplar set laant_av = ? where isbn = ? and eks_nr = ? and laant_av is null";


        PreparedStatement preparedStatement2 = connection.prepareStatement(query2);

        System.out.println("Hva er navn?");
        preparedStatement2.setString(1,sc.next());

        System.out.println("Hva er isbn?");
        preparedStatement2.setString(2, sc.next());

        System.out.println("Hva er eksemplarnr?");
        preparedStatement2.setString(3, sc.next());


        int result2 = preparedStatement.executeUpdate();

        if(result2 == 0){
            System.out.println("Noe gikk feil, det kan skyldes ugyldig isbn, ugyldig eksemplarnr eller at eksemplaret allerede er utlånt.");
        }else{
            System.out.println("Tingen ble gjort rikitg");
        }


        result.close();
        preparedStatement.close();
        connection.close();


    }

}
