# SCalc - Gestión de Jornadas y Tickets

Proyecto desarrollado para la **Actividad 7 - Dual** del ciclo CFGS Desarrollo de Aplicaciones Multiplataforma.
Esta aplicación permite la gestión de tickets mensuales y jornadas diarias, implementando persistencia local, notificaciones y uso de hardware.

## 📋 Cumplimiento de Requisitos (Actividad 7)

A continuación se detalla cómo esta aplicación cumple con los puntos exigidos en la documentación de la actividad:

### 1. Integración de Hardware (Bluetooth) [Requisito PDF]
> *"Integrando la comunicación con dispositivos inalámbricos (Bluetooth o Wi-Fi)"*
- **Implementación:** Se ha añadido una funcionalidad para verificar y activar la conexión Bluetooth, simulando la conexión con una impresora térmica para tickets.
- **Ubicación en código:** `ConfiguracionActivity.java` -> Método `probarBluetooth()`.
- **Permisos:** Se solicitan permisos `BLUETOOTH` y `BLUETOOTH_ADMIN` en el Manifest.

### 2. Reporte de Errores / Feedback [Requisito PDF]
> *"Funcionalidad para poder enviar un reporte de error... incluir un icono en una de las vistas"*
- **Implementación:** Botón dedicado "⚠ Reportar Error" en la pantalla de Configuración. Abre un Intent implícito de correo electrónico pre-rellenado para soporte técnico.
- **Ubicación en código:** `ConfiguracionActivity.java` -> Método `enviarReporte()`.

### 3. Notificaciones Locales [Requisito PDF]
> *"Gestión de preferencias y notificaciones... notificaciones push o locales"*
- **Implementación:** Sistema de notificaciones locales que alerta al usuario en la barra de estado cuando se registra una nueva jornada correctamente en la base de datos.
- **Ubicación en código:** `NuevaJornadaActivity.java` -> Método `lanzarNotificacion()`.

### 4. Persistencia de Datos y Base de Datos Local [Requisito PDF]
> *"Implementar una base de datos local... garantizando la persistencia"*
- **Implementación:** Uso de **SQLite** nativo para almacenar Tickets y Jornadas de forma persistente.
- **Ubicación en código:** `AdminSQLiteOpenHelper.java` y gestión CRUD en las actividades principales.

### 5. Interfaz Gráfica y Navegación
- Diseño de interfaces utilizando XML y componentes estándar de Android.
- Navegación fluida entre `MainActivity`, `Historial`, `Detalle` y `Configuración`.

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java
* **IDE:** Android Studio
* **Base de Datos:** SQLite
* **Versión Min SDK:** 24 (Android 7.0)
* **Versión Target SDK:** 34 (Android 14)

---

## 🚀 Guía de Pruebas para el Evaluador

Para verificar las funcionalidades implementadas:

1.  **Prueba de Base de Datos:**
    * Desde el menú principal, pulsar "Nueva Jornada".
    * Rellenar datos y guardar. Los datos persisten al cerrar la app.

2.  **Prueba de Notificaciones:**
    * Al guardar la jornada en el paso anterior, verificar la **barra de notificaciones** del dispositivo. Debe aparecer el aviso "Jornada Guardada".

3.  **Prueba de Hardware y Reportes:**
    * Ir a la pantalla de **Configuración** (icono de engranaje o menú).
    * Pulsar **"🖨 Probar Impresora Bluetooth"**: Solicitará encender el BT o confirmará si ya está activo.
    * Pulsar **"⚠ Reportar Error"**: Se abrirá la app de Gmail/Outlook con el borrador del reporte.

---

## 👤 Autor
Proyecto realizado por **Salvador Lopez Trigueros**

IES Antonio Gala - 2º DAM