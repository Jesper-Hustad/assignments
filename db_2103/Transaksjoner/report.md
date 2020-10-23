## Øving 5
# Transaksjoner


## Del A

## Opggave 1
### a)
 Klient 2 sin isolasjonsnivå på serializable betyr at update ikke skjer før klient 2 sin commit.

### b)
 Repeatable read på klient 2 hadde ikke låst dataen og klient 1 kunne utført sin update

## Opggave 2
### a)
Begge saldo blir satt til 2 på kontonr=1 og 2


Fordi read uncommited betyr at transaksjoner er ikke isolert fra hverandre.

### b)
Igjen er det bare den siste update setningen som setter en verdi på en tabell som tar effekt

Så kontonr=1 får saldo=2 og kontonr=2 får saldo=1

Om man endrer begge isolasjonsnivå til serializable for eksempel er jo den som commiter sist som tar effekt.

## Opggave 3

Vi kan vite fra å bare lese nivået: read uncommitted at de forandringene som ikke er commited kan forsatt bli lest. Om man går over til committed, repeatable read eller serializable så kommer det ikke lenger til å skje og alle select setningene returnerer det samme.


## Del B

## Opggave 1


```sql
SELECT boktittel.forfatter, boktittel.tittel ,COUNT(eksemplar.isbn) as eksemplarer FROM `boktittel`
INNER join eksemplar on boktittel.isbn = eksemplar.isbn
WHERE boktittel.isbn=?

```

### Java kode

```java


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
```



```sql
update eksemplar set laant_av = ? where isbn = ? and eks_nr = ? and laant_av is null;
```

```java
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
```

Dette er java kode for å sette opp sql connection og lukke den.
```java
Connection connection = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/jesperfh?user=jesperfh&password=------");

        result.close();
        preparedStatement.close();
        connection.close();
```