# 🎄 Wishing List for Christmas

## Contributors
- **Ulugebk Yarkiov**  
- **Matthew Claflin**

---

## 🎓 Project Purpose  
This project is designed for **educational purposes** to explore and test **Distributed System Accessibility** using **REST APIs**, **Docker**, and multiple client implementations.

---

## 📄 Description of the Project  
The "Wishing List for Christmas" application enables users to manage a Christmas shopping list through a RESTful API built with **Spring Boot**. The backend provides CRUD operations to manage shopping items, and two client applications interact with the API:

1. **Python Flask Client**  
2. **Python FastAPI Client**  

The project also uses **Swagger UI** for API documentation and testing.

---

## 🚀 Features (First Version)

### Backend (Spring Boot)  
1. **Endpoints** for managing shopping items:
   - **Get all items**: `GET /api/shoppingItems`  
   - **Add a new item**: `POST /api/shoppingItems`  
   - **Get a single item by name**: `GET /api/shoppingItems/{shoppingItemName}`  
   - **Update an item by name**: `PUT /api/shoppingItems/{shoppingItemName}`  
   - **Delete an item by name**: `DELETE /api/shoppingItems/{shoppingItemName}`  

2. **Swagger Integration** for API documentation at `/swagger-ui.html`.

### Clients  
- **Python Flask Client**: Runs on port `5000` and provides an HTML interface to manage the shopping list.  
- **Python FastAPI Client**: Runs on port `8000` and also provides an HTML interface for managing the shopping list.  

### Database  
- **PostgreSQL**: Runs on port `5432` and stores shopping items.

---

## 🐳 Docker Compose Configuration

The project uses `docker-compose.yaml` to orchestrate the backend, clients, and database services.

### **Ports Configuration**
- **PostgreSQL**: `5432`  
- **Spring Boot Backend**: `8080`  
- **Flask Client**: `5000`  
- **FastAPI Client**: `8000`