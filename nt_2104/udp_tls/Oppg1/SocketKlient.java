package Oppg1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class SocketKlient {
  public static void main(String[] args) throws IOException {

    final int SERVER_PORTNR = 2200;
    final int KLIENT_PORTNR = 3300;
    byte[] buffer;

    System.out.println("Velkommen til kalkulator klienten");

    DatagramSocket datagramSocket = new DatagramSocket(KLIENT_PORTNR);

    while (true) {

      System.out.println("Skriv tall operator tall | Eksempel: '5 + 2' | Skriv exit for å stoppe");


      /* Bruker en scanner til å lese fra kommandovinduet */
      Scanner leserFraKommandovindu = new Scanner(System.in);

      /* Setter opp socket til tjenerprogrammet */

      InetAddress ip = InetAddress.getLocalHost();

      /* Leser tekst fra kommandovinduet (brukeren) */
      String enLinje = leserFraKommandovindu.nextLine();

      /* Sender tekst til server i en packet */
      buffer = enLinje.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, ip, SERVER_PORTNR);
      datagramSocket.send(sendPacket);

      /* Slutt program hvis brukeren skriver exit */
      if(enLinje.equals("exit")){
        System.out.println("Avslutter program");
        datagramSocket.close();
        break;
      }

      /* Mottar tekst fra server i en packet */
      buffer = new byte[65535];
      DatagramPacket mottaPacket = new DatagramPacket(buffer, buffer.length);
      datagramSocket.receive(mottaPacket);

      /* Gjør om og printer motatt tekst til bruker */
      System.out.println("Motatt: " + new String(buffer, 0, buffer.length));


    }

  }
}
