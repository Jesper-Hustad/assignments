package Oppg1;

import java.io.*;
import java.net.*;

class SocketTjener {
  public static void main(String[] args) throws IOException {

    final int SERVER_PORTNR = 2200;
    final int KLIENT_PORTNR = 3300;

    System.out.println("Logg for tjenersiden. Nå venter vi...");

    DatagramSocket datagramSocket = new DatagramSocket(SERVER_PORTNR);

    while (true) {

      byte[] buffer = new byte[65535];
      DatagramPacket mottaPacket = new DatagramPacket(buffer, buffer.length);

      datagramSocket.receive(mottaPacket);// venter inntil noen tar kontakt

      System.out.println("Fikk en packet!");

      String input = new String(buffer, 0, buffer.length);

      System.out.println("input er : " + input);

      if(input.trim().equals("exit")){
        System.out.println("Slutter serveren");
        datagramSocket.close();
        break;
      }

      Double result = calculateAnswer(input);

      String output = result == null ? "Noe gikk galt" : Double.toString(result);

      /* Semd resultatet til brukeren */
      buffer = Double.toString(result).getBytes();
      DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), KLIENT_PORTNR);
      datagramSocket.send(sendPacket);

    }

  }

  private static Double calculateAnswer(String input) {


    String[] elementer = input.split(" ");

    Double tallEn = Double.parseDouble(elementer[0]);
    Double tallTo = Double.parseDouble(elementer[2]);

    String operasjon = elementer[1];

    return switch (operasjon) {
      case "+" -> tallEn + tallTo;
      case "-" -> tallEn - tallTo;
      default -> null;
    };
  }
}
