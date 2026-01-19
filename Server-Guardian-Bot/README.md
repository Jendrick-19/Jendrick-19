# 🕵️‍♂️ Server Guardian Bot

**Herramienta de monitorización automatizada (SysAdmin + Dev)**

Este proyecto es un **Bot de Telegram desarrollado en Java** que monitoriza en tiempo real el estado de un servidor o PC local. Si detecta anomalías (caída de internet o disco lleno), envía una alerta crítica al móvil del administrador.

### 🚀 Funcionalidades
* **Conectividad:** Realiza pings periódicos a DNS externos (8.8.8.8) para verificar la salida a Internet.
* **Almacenamiento:** Monitoriza el espacio libre en disco y alerta si baja de 5GB.
* **Integración API:** Uso de `java.net.HttpURLConnection` y `URLEncoder` para comunicarse con la API de Telegram.

### 📸 Demo del Funcionamiento
*Así se ve cuando el bot detecta que el sistema está operativo:*

![Captura del Bot](AQUI_TIENES_QUE_SUBIR_TU_CAPTURA_DE_PANTALLA.png)

### 🛠️ Tecnologías
* **Lenguaje:** Java (Nativo, sin frameworks pesados).
* **Conceptos:** Hilos (`Thread.sleep`), Peticiones HTTP (GET), Manejo de Ficheros (`java.io.File`).

### ⚙️ Cómo usarlo
1. Clona el repositorio.
2. Crea tu propio bot con @BotFather en Telegram.
3. Edita el archivo `ServerBot.java` y coloca tu `TOKEN` y `CHAT_ID`.
4. Ejecuta y deja que vigile tu equipo.

---
*Proyecto realizado por **Alejandro Montilla** (Estudiante DAM + Técnico ASIR) para automatización de infraestructuras.*
