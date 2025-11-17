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

### 📡 **API externa**
| Endpoint | Método | Descripción |
|---------|--------|-------------|
| `https://...` | GET | Obtención de datos externos utilizados en la app. |

---

## 🧩 **Microservicio interno (Backend propio)**  

### 👤 **Usuarios — `/api/v1/usuarios`**
| Endpoint | Método | Descripción |
|---------|--------|-------------|
| `/api/v1/usuarios` | GET | Obtener lista de todos los usuarios. |
| `/api/v1/usuarios/{id}` | GET | Obtener un usuario por su ID. |
| `/api/v1/usuarios/correo/{correo}` | GET | Obtener un usuario por su correo. |
| `/api/v1/usuarios` | POST | Crear un nuevo usuario. |
| `/api/v1/usuarios/{id}` | PUT | Actualizar un usuario existente. |
| `/api/v1/usuarios/{id}` | DELETE | Eliminar un usuario por su ID. |

---

### 🗂️ **Familias — `/api/v1/familias`**
| Endpoint | Método | Descripción |
|---------|--------|-------------|
| `/api/v1/familias` | GET | Listar todas las familias. |
| `/api/v1/familias/{id}` | GET | Obtener familia por ID. |
| `/api/v1/familias/nombre/{nombre}` | GET | Buscar familia por nombre. |
| `/api/v1/familias` | POST | Crear una nueva familia. |
| `/api/v1/familias/{id}` | PUT | Actualizar una familia. |
| `/api/v1/familias/{id}` | DELETE | Eliminar una familia por ID. |

---

### 🔧 **Conceptos Técnicos — `/api/v1/conceptos-tecnicos`**
| Endpoint | Método | Descripción |
|---------|--------|-------------|
| `/api/v1/conceptos-tecnicos` | GET | Listar todos los conceptos técnicos. |
| `/api/v1/conceptos-tecnicos/{id}` | GET | Obtener concepto técnico por ID. |
| `/api/v1/conceptos-tecnicos/nombre/{nombre}` | GET | Buscar concepto técnico por nombre. |
| `/api/v1/conceptos-tecnicos` | POST | Crear un concepto técnico. |
| `/api/v1/conceptos-tecnicos/{id}` | PUT | Actualizar un concepto técnico. |
| `/api/v1/conceptos-tecnicos/{id}` | DELETE | Eliminar un concepto técnico. |

---

### 📘 **Conceptos Formativos — `/api/v1/conceptos-formativos`**
| Endpoint | Método | Descripción |
|---------|--------|-------------|
| `/api/v1/conceptos-formativos` | GET | Listar todos los conceptos formativos. |
| `/api/v1/conceptos-formativos/{id}` | GET | Obtener concepto formativo por ID. |
| `/api/v1/conceptos-formativos/nombre/{nombre}` | GET | Buscar concepto formativo por nombre. |
| `/api/v1/conceptos-formativos` | POST | Crear un concepto formativo. |
| `/api/v1/conceptos-formativos/{id}` | PUT | Actualizar un concepto formativo. |
| `/api/v1/conceptos-formativos/{id}` | DELETE | Eliminar un concepto formativo. |

---

## 📦 **Captura del APK firmado y .jks**
<img width="621" height="57" alt="image" src="https://github.com/user-attachments/assets/dd7845d7-e3bf-4fd1-8c1a-6b47701d9032" />

---

## ▶️ **Pasos para ejecutar el proyecto**

### 1. **Clonar el repositorio**
```bash
git clone https://github.com/TU-USUARIO/Excelsior-RestaurApp.git
cd Excelsior-RestaurApp
