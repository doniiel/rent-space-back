# RentSpaceBack Microservices Project

A microservices-based rental platform built with Spring Boot, Docker, and monitoring (Prometheus/Grafana). The project includes services for user management, listings, bookings, payments, reviews, notifications, and search, orchestrated with Docker Compose and automated via GitHub Actions CI/CD.

## Project Structure
- **Custom Services**: `configservice`, `discoveryservice`, `gatewayservice`, `userservice`, `listingservice`, `notificationservice`, `bookingservice`, `searchservice`, `reviewservice`, `paymentservice`.
- **Infrastructure Services**: PostgreSQL, pgAdmin, Minio, Kafka, Redis, Elasticsearch, Prometheus, Grafana.
- **CI/CD**: GitHub Actions builds and pushes Docker images to Docker Hub.
- **Monitoring**: Prometheus and Grafana for observability.

## Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/RentSpaceBack
   cd RentSpaceBack
   ```
2. **Set permissions**:
   ```bash
   chmod +x setup-permissions.sh
   ./setup-permissions.sh
   ```
3. **Run with Docker Compose**:
   ```bash
   docker-compose up --build
   ```
4. **Access services**:
   - Gateway: `http://localhost:8072`
   - Grafana: `http://localhost:3000` (admin/admin)
   - Prometheus: `http://localhost:9090`
   - pgAdmin: `http://localhost:5050` (admin@admin.com/admin)
   - Minio: `http://localhost:9000` (minioadmin/minioadmin)
   - Eureka Dashboard: `http://localhost:8761`
   - Elasticsearch: `http://localhost:9200`
   - Redis: `localhost:6379`
   - Kafka: `localhost:9092`

## CI/CD
- **GitHub Actions** automates the build and deployment of all custom services.
- On push to the `main` branch, the pipeline:
  1. Builds Spring Boot JARs using Maven.
  2. Creates Docker images with tags `:latest` and `:<commit-sha>`.
  3. Pushes images to Docker Hub: `d0niiel/<service>` (e.g., `d0niiel/configservice`, `d0niiel/userservice`).
- To use the latest images, run:
   ```bash
   docker-compose pull
   docker-compose up -d
   ```

## Permissions
- Custom services run as non-root user `1001` (see Dockerfiles).
- Configuration files and volumes have restricted permissions:
   ```bash
   ./setup-permissions.sh
   ```

## Testing
- Import `RentSpace.postman_collection.json` into Postman to test API endpoints.
- Example endpoints (via gateway):
  - Users: `http://localhost:8072/api/users`
  - Listings: `http://localhost:8072/api/listings`
  - Bookings: `http://localhost:8072/api/bookings`

## Monitoring
- Prometheus collects metrics at `http://localhost:9090`.
- Grafana visualizes metrics at `http://localhost:3000` with preconfigured dashboards.

## Troubleshooting
- **Maven Build Fails**: Check `pom.xml` for missing dependencies. Use `-DskipTests` if tests fail.
- **Docker Push Fails**: Verify `DOCKER_USERNAME` and `DOCKER_PASSWORD` in GitHub Secrets.
- **Services Fail to Start**: Check `docker-compose.yml` for correct image tags and environment variables. Use `docker logs <container>` for debugging.