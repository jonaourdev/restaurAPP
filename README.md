# **Excelsior Restaur-App**

Excelsior Restaur-App es una aplicación orientada a la difusión, aprendizaje y gestión de conceptos relacionados con la restauración patrimonial. Permite visualizar contenido técnico y formativo, agregar nuevos conceptos mediante autenticación y seleccionar favoritos.

---

## 👥 **Integrantes del equipo**

- **José Naour**  
- **Lucas Ferrada**  
- **Marco Contreras**

---

## 🛠️ **Funcionalidades principales**

Las principales funcionalidades de **Excelsior Restaur-App** son:

1. **Visualización de conceptos técnicos y formativos**  
   Permite explorar conceptos sobre restauración patrimonial y comprender la relación entre ellos.

2. **Ingreso de nuevos conceptos técnicos y formativos**  
   Disponible para usuarios autenticados, quienes pueden agregar contenido propio.

3. **Autenticación**  
   Sistema de login para acceder a funcionalidades exclusivas de la aplicación.

4. **Gestión de favoritos**  
   Los usuarios pueden seleccionar y almacenar conceptos o familias como favoritas.

---

## 🔗 **Endpoints utilizados**

> *Modifica esta sección según tus rutas reales. Mantengo un formato estándar de APIs.*

### 📡 **API externa**
| Endpoint | Método | Descripción |
|---------|--------|-------------|
| `https://...` | GET | Obtención de datos externos utilizados en la app. |

### 🧩 **Microservicio interno (Backend propio)**  
| Endpoint | Método | Descripción |
|---------|--------|-------------|
| `/api/login` | POST | Autenticación de usuarios. |
| `/api/families` | GET | Listado de familias técnicas y formativas. |
| `/api/families/{id}` | GET | Detalles de una familia. |
| `/api/concepts` | POST | Crear concepto técnico/formativo. |
| `/api/favorites` | POST | Agregar concepto a favoritos. |
| `/api/favorites/{userId}` | GET | Listar favoritos de un usuario. |

---

## ▶️ **Pasos para ejecutar el proyecto**

---

## **Captura del APK firmado y .jks**
<img width="621" height="57" alt="image" src="https://github.com/user-attachments/assets/dd7845d7-e3bf-4fd1-8c1a-6b47701d9032" />

---

### 1. **Clonar el repositorio**
```bash
git clone https://github.com/TU-USUARIO/Excelsior-RestaurApp.git
cd Excelsior-RestaurApp
