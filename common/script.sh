#!/bin/bash

echo "Starting infrastructure (ActiveMQ/Databases)..."
cd common || exit
docker-compose up -d
cd ..

echo "Waiting 15 seconds for infrastructure to initialize..."
sleep 25

# Define the optimal startup order
SERVICES=(
    "ingestion-service"
    "hub-service"
    "delay-stage-service"
    "transit-service"
    "alertbot"
)

echo "Compiling and packaging all services..."
for SERVICE in "${SERVICES[@]}"; do
    echo "Building $SERVICE..."
    cd "$SERVICE" || exit
    # Clean and package the jar, skipping tests for faster startup
    mvn clean package -DskipTests
    cd ..
done

echo "Starting microservices in the background..."
for SERVICE in "${SERVICES[@]}"; do
    echo "Booting $SERVICE..."
    cd "$SERVICE" || exit
    
    # Run the compiled jar in the background and pipe output to a log file
    nohup java -jar target/"${SERVICE}.jar" > "../${SERVICE}.log" 2>&1 &
    
    cd ..
    # Wait a few seconds between starts to avoid overwhelming the CPU/Broker
    sleep 20
done

echo "===================================================="
echo "All services have been started successfully."
echo "Logs are being written to [service-name].log in this directory."
echo "To view live logs for a service, run: tail -f hub-service.log"
echo "To shut down all Java services, run: pkill -f 'java -jar target/'"
echo "To shut down infrastructure, run: cd common && docker-compose down"
echo "===================================================="
