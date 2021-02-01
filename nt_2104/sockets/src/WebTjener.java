import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class WebTjener {
  public static void main(String[] args) throws IOException {
    final int PORTNR = 80;

    ServerSocket tjener = new ServerSocket(PORTNR);
    System.out.println("Logg for tjenersiden. Nå venter vi...");

    Socket forbindelse = tjener.accept();  // venter inntil noen tar kontakt

    /* Åpner strømmer for kommunikasjon med klientprogrammet */
    InputStreamReader leseforbindelse = null;
    leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
    BufferedReader leseren = new BufferedReader(leseforbindelse);

    PrintWriter skriveren = null;
    skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


    /* Mottar data fra klienten */
    String enLinje = leseren.readLine();  // mottar en linje med tekst


    /* Skriver en HTML av headers motatt fra klienten  */
    StringBuilder headerElementer = new StringBuilder();
    while (!enLinje.equals("")) {  // forbindelsen på klientsiden er lukket
      headerElementer.append("<LI>").append(enLinje).append("</LI>\n");
      enLinje = leseren.readLine();
    }

    String respons = "" +
            "HTTP/1.0 200 OK\n" +
            "Content-Type: text/html; charset=utf-8\n" +
            "\n" +
            "<HTML><BODY>\n" +
            "<H1> Hilsen. Du har koblet deg opp til min enkle web-tjener </h1>\n" +
            "Header fra klient er:\n" +
            "<UL>\n" +
            headerElementer +
            "</UL>\n" +
            "</BODY></HTML>";

    /* Sender HTML data til klienten */
    skriveren.println(respons);

    /* Lukker forbindelsen */
    leseren.close();
    skriveren.close();
    forbindelse.close();

    System.out.println("Fikk en kobling og svarte med HTML side");
  }
}
