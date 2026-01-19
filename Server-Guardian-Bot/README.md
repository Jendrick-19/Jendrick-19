# 🕵️‍♂️ Server Guardian Bot (Interactive Edition)

**Asistente de SysAdmin remoto controlado por Telegram.**

Este proyecto es una evolución del monitor clásico. En lugar de solo enviar alertas, este **Bot Interactivo programado en Java** escucha comandos del usuario en tiempo real para devolver métricas del servidor bajo demanda.

### 🚀 Funcionalidades Nuevas (v3.0)
El bot utiliza una arquitectura de *Polling* (consulta continua) para interactuar contigo:

* 💻 **Comando `/pc`:** Realiza una auditoría instantánea del hardware y devuelve:
  * Carga de CPU (%).
  * Uso de RAM (GB usados / Totales).
  * Estado del Disco C: (Espacio libre y ocupado).
* 🌍 **Comando `/ip`:** Consulta APIs externas para reportar la IP Pública actual de la red.
* 🛡️ **Seguridad:** El bot solo responde al ID de chat autorizado (SysAdmin), ignorando a otros usuarios.

### 📸 Demo del Funcionamiento

*Así responde el bot cuando le pides información:*

![Demo del Bot](AQUI_TIENES_QUE_SUBIR_TU_CAPTURA_DE_PANTALLA.png)
*(Sube tu captura funcionando aquí)*

### 🛠️ Tecnologías
* **Lenguaje:** Java (Nativo, sin frameworks pesados).
* **Librerías Clave:** * `java.net.HttpURLConnection` (Para hablar con la API de Telegram).
  * `com.sun.management.OperatingSystemMXBean` (Para extraer datos reales de CPU/RAM).
* **Arquitectura:** Bucle infinito con `Thread.sleep` para gestionar el Polling de mensajes.

### ⚙️ Cómo usarlo en tu equipo

1. **Clona el repositorio:**
   ```bash
   git clone [https://github.com/Jendrick-19/Server-Guardian-Bot.git](https://github.com/Jendrick-19/Server-Guardian-Bot.git)
