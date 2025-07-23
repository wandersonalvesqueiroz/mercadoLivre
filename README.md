# 🛍️ Produto API

API RESTful desenvolvida com Spring Boot para disponibilizar informações de produto por ID.

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.1.5
- Swagger/OpenAPI (Springdoc)
- Maven

---

## 📁 Estrutura do Projeto

```
src
├── main
│ ├── java
│ │ └── meli
│ │ ├── controller # Endpoints da API
│ │ ├── service # Lógica de negócio
│ │ ├── dto # Objetos de transferência de dados
│ │ ├── config # Configurações globais (CORS, Swagger, etc)
│ │ ├── model # (Gerado automaticamente pelo Swagger)
│ │ └── enums # (Gerado automaticamente pelo Swagger)
│ └── resources
│ ├── application.yml # Configurações Spring
│ ├── products.json # Mock de dados
│ └── swagger.yaml # Especificação OpenAPI
```

---

## ⚙️ Como Executar

### Pré-requisitos

- Java 21
- Maven 3.8+

### Passos

```bash
# Clone o projeto
git clone https://github.com/wandersonalvesqueiroz/mercadoLivre.git
cd produto-ml

# Compile e execute o projeto
./mvnw spring-boot:run
```

> A aplicação será executada em: `http://localhost:8080`

---

## 🧪 Testando a API

Você pode usar o Swagger para testar os endpoints diretamente no navegador.

### Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

---

## 🔄 Endpoints Disponíveis

| Método | Endpoint                  | Descrição                           |
|--------|---------------------------|--------------------------------------|
| GET    | `/api/products/{id}`      | Retorna produto por ID              |

> Os endpoints são gerados automaticamente com base no arquivo OpenAPI (YAML).

---

## 📦 Leitura de Arquivo JSON

O backend carrega os produtos a partir do arquivo `products.json`, localizado em:

```
src/main/resources/products.json
```
---



Desenvolvido por **Wanderson Alves**  
🔗 [LinkedIn](https://www.linkedin.com/in/wandersonalvesqueiroz/)  
📧 wandersonmg18@gmail.com