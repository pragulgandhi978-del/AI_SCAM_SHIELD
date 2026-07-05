#!/bin/bash

# AI Scam Shield - Start all services (Linux/macOS)

echo ""
echo "========================================"
echo "  AI Scam Shield - Starting Services"
echo "========================================"
echo ""

# Check if required tools exist
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed"
    echo "Please install Java 21 or higher"
    exit 1
fi

if ! command -v node &> /dev/null; then
    echo "ERROR: Node.js is not installed"
    echo "Please install Node.js 18 or higher"
    exit 1
fi

echo "[OK] Java and Node.js are installed"
echo ""

# Start Backend in background
echo "Starting Backend (Spring Boot)..."
cd backend
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

# Wait for backend to start
sleep 5

# Start Frontend in background
echo "Starting Frontend (React + Vite)..."
cd frontend
npm install
npm run dev &
FRONTEND_PID=$!
cd ..

echo ""
echo "========================================"
echo "  Services Starting..."
echo "========================================"
echo ""
echo "Backend:  http://localhost:8080"
echo "Frontend: http://localhost:5173"
echo "Health:   http://localhost:8080/api/health"
echo ""
echo "Backend PID:  $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"
echo ""
echo "To stop all services, run:"
echo "  kill $BACKEND_PID $FRONTEND_PID"
echo ""

# Wait for Ctrl+C
trap 'kill $BACKEND_PID $FRONTEND_PID 2>/dev/null; exit' INT

wait
