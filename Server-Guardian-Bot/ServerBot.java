import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ServerBot {

    // ⚠️ TUS CREDENCIALES
    private static final String BOT_TOKEN = "7902953950:AAFG3eEYevprwQVJ2LLdvFcMBk_BHicUDCA"; // Tu token
    private static final String CHAT_ID = "6125447323"; // Tu ID

    // Variable para no leer el mismo mensaje dos veces (Offset)
    private static long lastUpdateId = 0;

    public static void main(String[] args) {
        System.out.println("🤖 Bot Interactivo Iniciado. Esperando comandos...");
        enviarMensaje("👋 Hola Alejandro. Estoy listo. Escribe: \n'/pc' para ver el estado.\n'/ip' para ver tu IP.");

        // Bucle Infinito: Pregunta a Telegram cada segundo si hay novedades
        while (true) {
            try {
                leerMensajesNuevos();
                Thread.sleep(1000); // Pausa de 1 segundo para no saturar
            } catch (Exception e) {
                System.out.println("Error en el bucle principal: " + e.getMessage());
            }
        }
    }

    // Función que consulta a Telegram si hay mensajes nuevos
    public static void leerMensajesNuevos() {
        try {
            // offset = lastUpdateId + 1 significa "dame solo los mensajes NUEVOS que no he leído"
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/getUpdates?offset=" + (lastUpdateId + 1);
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            String jsonResponse = content.toString();

            // Si el JSON tiene "update_id", es que hay mensaje
            if (jsonResponse.contains("\"update_id\":")) {
                procesarMensaje(jsonResponse);
            }

        } catch (Exception e) {
            // Ignoramos errores de red puntuales
        }
    }

    // Función "artesanal" para leer el JSON sin librerías externas
    public static void procesarMensaje(String json) {
        try {
            // 1. Sacamos el ID del mensaje para no repetirlo
            // Buscamos el texto "update_id": y cogemos el número que sigue
            int indexUpdateId = json.lastIndexOf("\"update_id\":");
            String temp = json.substring(indexUpdateId + 12); // 12 es el largo de "update_id":
            int finNumero = temp.indexOf(",");
            String updateIdStr = temp.substring(0, finNumero);
            lastUpdateId = Long.parseLong(updateIdStr); // Actualizamos el contador

            // 2. Sacamos el TEXTO que has escrito
            // Buscamos "text":" y cogemos lo que sigue
            if (json.contains("\"text\":\"")) {
                int indexText = json.lastIndexOf("\"text\":\"");
                String tempText = json.substring(indexText + 8);
                int finTexto = tempText.indexOf("\"");
                String comandoUsuario = tempText.substring(0, finTexto);

                System.out.println("Comando recibido: " + comandoUsuario);

                // 3. CEREBRO DEL BOT: Decidimos qué hacer según la palabra
                if (comandoUsuario.equalsIgnoreCase("/pc")) {
                    enviarMensaje("⏳ Analizando sistema... dame un segundo.");
                    enviarReporteCompleto(); // Llama a la función del hardware
                } 
                else if (comandoUsuario.equalsIgnoreCase("/ip")) {
                    String ip = obtenerIPPublica();
                    enviarMensaje("🌍 Tu IP pública es: " + ip);
                }
                else if (comandoUsuario.equalsIgnoreCase("/ayuda")) {
                    enviarMensaje("Comandos disponibles:\n/pc - Ver CPU/RAM/Disco\n/ip - Ver IP Pública");
                }
                else {
                    enviarMensaje("❓ No entiendo '" + comandoUsuario + "'. Prueba /ayuda");
                }
            }

        } catch (Exception e) {
            System.out.println("Error procesando mensaje (puede ser un sticker o emoji): " + e.getMessage());
        }
    }

    // --- AQUÍ ABAJO ESTÁN LAS FUNCIONES QUE YA TENÍAS (Reporte, IP, Enviar) ---

    public static void enviarReporteCompleto() {
        try {
            // CPU y RAM
            Object osBean = ManagementFactory.getOperatingSystemMXBean();
            double cpuLoad = 0;
            long totalRam = 0, freeRam = 0;

            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunBean = (com.sun.management.OperatingSystemMXBean) osBean;
                cpuLoad = sunBean.getSystemCpuLoad() * 100;
                totalRam = sunBean.getTotalPhysicalMemorySize() / (1024 * 1024 * 1024);
                freeRam = sunBean.getFreePhysicalMemorySize() / (1024 * 1024 * 1024);
            }
            long usedRam = totalRam - freeRam;

            // DISCO
            File disco = new File("C:\\");
            long totalDisco = disco.getTotalSpace() / (1024 * 1024 * 1024);
            long libreDisco = disco.getFreeSpace() / (1024 * 1024 * 1024);
            long porcentajeDisco = (totalDisco > 0) ? ((totalDisco - libreDisco) * 100) / totalDisco : 0;

            String reporte = "💻 *TU PC AHORA MISMO* 💻\n\n"
                           + "🧠 *CPU:* " + String.format("%.2f", cpuLoad) + "%\n"
                           + "💾 *RAM:* " + usedRam + "GB / " + totalRam + "GB\n"
                           + "💿 *DISCO:* " + porcentajeDisco + "% Ocupado";

            enviarMensaje(reporte);

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static String obtenerIPPublica() {
        try {
            URL url = new URL("https://api.ipify.org");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            return in.readLine();
        } catch (Exception e) { return "Desconocida"; }
    }

    public static void enviarMensaje(String mensaje) {
        try {
            String mensajeCodificado = URLEncoder.encode(mensaje, "UTF-8");
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=" + mensajeCodificado + "&parse_mode=Markdown";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
        } catch (Exception e) { System.out.println("Error enviando: " + e.getMessage()); }
    }
}