import java.io.*;
import java.net.*;
import java.util.Locale;

class SocketTjener {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for tjenersiden. Nå venter vi...");


        while (true){
            Socket forbindelse = tjener.accept();  // venter inntil noen tar kontakt

            /* Lager en klient tråd og starter den */
            Thread t = new KlientTraad(forbindelse);
            t.start();
        }


    }
}


class KlientTraad extends Thread{

    Socket forbindelse;

    public KlientTraad(Socket forbindelse){
        this.forbindelse = forbindelse;

    }

    @Override
    public void run(){
        try {

            /* Åpner strømmer for kommunikasjon med klientprogrammet */
            InputStreamReader leseforbindelse = null;
            leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
            BufferedReader leseren = new BufferedReader(leseforbindelse);
            PrintWriter skriveren = null;
            skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

            /* Sender innledning til klienten */
            skriveren.println("Hei, du har kontakt med tjenersiden som gjør mattematiske operasjoner!");

            System.out.println("fikk en kobling til klient");

            while (true) {

                Double tallEn = null;
                Double tallTo = null;

                boolean tallFeil;
                do {
                    tallFeil = false;
                    try{
                        skriveren.println("Skriv tall nr 1");
                        tallEn = Double.parseDouble(leseren.readLine());

                        skriveren.println("Skriv tall nr 2");
                        tallTo = Double.parseDouble(leseren.readLine());

                    } catch(NumberFormatException e) {
                        skriveren.println("Feil format på tall, prøv igjen");
                        leseren.readLine();
                        tallFeil = true;
                    }
                } while(tallFeil);


                boolean gyldigOperasjon;
                do {
                    gyldigOperasjon = true;

                    skriveren.println("Hvilken matematisk operasjon ønsker du å gjøre? [add / sub]");
                    String operasjon = leseren.readLine();

                    switch (operasjon) {

                        case "add":
                            skriveren.println(tallEn + " + " + tallTo + " = " + (tallEn + tallTo));
                            leseren.readLine();
                            break;

                        case "sub":
                            skriveren.println(tallEn + " - " + tallTo + " = " + (tallEn - tallTo));
                            leseren.readLine();
                            break;

                        default:
                            skriveren.println("Operasjonen " + operasjon + " finnes ikke, prøv igjen\n");
                            leseren.readLine();
                            gyldigOperasjon = false;
                    }
                } while(!gyldigOperasjon);



                skriveren.println("Ønsker du å gjøre enda en operasjon? [Y/n]");
                if(!leseren.readLine().toLowerCase(Locale.ROOT).equals("y")){
                    skriveren.println("EXIT VRMCJFFRDV95ZNKZBV5TYDSSSXGJPH");
                    break;
                }
            }

            /* Lukker forbindelsen */
            leseren.close();
            skriveren.close();
            forbindelse.close();

        } catch (IOException e) { e.printStackTrace(); }
    }

}