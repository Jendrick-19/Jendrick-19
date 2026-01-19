import java.io.File;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder; // <--- Importante para arreglar el error 400
import java.util.Timer;
import java.util.TimerTask;

public class ServerBot {

    // ⚠️ TUS CREDENCIALES (Recuerda borrarlas antes de subir a GitHub)
    private static final String BOT_TOKEN = "PON_AQUI_TU_TOKEN";
    private static final String CHAT_ID = "PON_AQUI_TU_ID";

    public static void main(String[] args) {
        System.out.println("🤖 Bot Vigilante Iniciado...");

        // PRUEBA DE FUEGO: Enviamos un saludo nada más arrancar
        enviarAlerta("🚀 ¡Hola Alejandro! Soy tu Bot y estoy operativo. Sin error 400.");

        // Bucle de vigilancia (revisa cada 60 segundos)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                realizarChequeos();
            }
        }, 0, 60000); 
    }

    public static void realizarChequeos() {
        try {
            // 1. CHEQUEO DE INTERNET
            boolean hayInternet = InetAddress.getByName("8.8.8.8").isReachable(2000);

            if (hayInternet) {
                System.out.println("✅ Sistema OK - Internet funcionando");
            } else {
                System.out.println("❌ PELIGRO: Sin conexión.");
                enviarAlerta("⚠️ ¡Alejandro! El servidor ha perdido la conexión a Internet.");
            }

            // 2. CHEQUEO DE DISCO (Adaptado a Windows C:)
            File disco = new File("C:"); 
            long espacioLibreGB = disco.getFreeSpace() / (1024 * 1024 * 1024);
            
            if (espacioLibreGB < 5) {
                System.out.println("❌ ALERTA: Disco lleno.");
                enviarAlerta("💾 ESPACIO BAJO: Quedan solo " + espacioLibreGB + "GB libres.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método CORREGIDO (Usa URLEncoder para evitar Error 400)
    public static void enviarAlerta(String mensaje) {
        try {
            // Esto convierte los espacios y emojis a formato web válido
            String mensajeCodificado = URLEncoder.encode(mensaje, "UTF-8");
            
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=" + mensajeCodificado;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Si sale 200 es ÉXITO
            int responseCode = conn.getResponseCode();
            System.out.println("Mensaje enviado a Telegram. Código: " + responseCode);

        } catch (Exception e) {
            System.out.println("Error enviando mensaje: " + e.getMessage());
        }
    }
}
