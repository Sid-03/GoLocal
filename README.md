# GoLocal

A digital food marketplace suite for organic products, including a B2C e-commerce site and a B2B marketplace (GoLocal).

## 🚀 Tech Stack

- **Frontend:** React, Next.js, Tailwind CSS
- **Backend:** Java Spring Boot (Microservices)
- **Database:** PostgreSQL
- **API Gateway:** Spring Cloud Gateway
- **Containerization:** Docker, Docker Compose
- **Deployment:** Kubernetes (planned)

## ✨ Features

- User authentication (JWT)
- Product listing and search
- Inquiry system
- Secure payment integration (Razorpay)
- Admin dashboard (planned)
- Responsive UI

## 🛠️ Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 17+
- Node.js & npm/yarn

### Clone the repo

```sh
git clone https://github.com/Sid-03/GoLocal.git
cd GoLocal
```

### Environment Variables

Create a `.env` file in the root and add:

```
JWT_SECRET=your_jwt_secret
RAZORPAY_KEY=your_razorpay_key
RAZORPAY_SECRET=your_razorpay_secret
```

### Run with Docker Compose

```sh
docker-compose up --build
```

The frontend will be available at `http://localhost:3000` and the API Gateway at `http://localhost:9000`.

## 📦 Project Structure

```
go-local-app/
├── api-gateway/
├── user-service/
├── product-service/
├── inquiry-service/
├── frontend/
├── docker-compose.yml
└── README.md
```

## 📑 License

[MIT](LICENSE)


---

*For more details, see the full documentation in the `/docs` folder (if available).*
