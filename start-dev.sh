#!/bin/bash

# MediCase Development Startup Script
echo "🏥 Starting MediCase Development Environment..."
echo ""

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo "🔍 Checking prerequisites..."

if ! command_exists java; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

if ! command_exists node; then
    echo "❌ Node.js is not installed. Please install Node.js 18 or higher."
    exit 1
fi

if ! command_exists npm; then
    echo "❌ npm is not installed. Please install npm."
    exit 1
fi

echo "✅ Prerequisites check passed!"
echo ""

# Check if MongoDB is running
echo "🔍 Checking MongoDB connection..."
if ! command_exists mongosh; then
    echo "⚠️  MongoDB shell (mongosh) not found. Please ensure MongoDB is installed and running."
    echo "   You can install it from: https://www.mongodb.com/docs/mongodb-shell/install/"
else
    echo "✅ MongoDB shell found!"
fi
echo ""

# Start backend
echo "🚀 Starting Spring Boot Backend..."
cd backend
chmod +x mvnw
./mvnw spring-boot:run &
BACKEND_PID=$!
echo "Backend started with PID: $BACKEND_PID"
echo ""

# Wait a bit for backend to start
echo "⏳ Waiting 30 seconds for backend to start up..."
sleep 30

# Start frontend
echo "🎨 Starting Angular Frontend..."
cd ../frontend

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "📦 Installing frontend dependencies..."
    npm install
fi

npm start &
FRONTEND_PID=$!
echo "Frontend started with PID: $FRONTEND_PID"
echo ""

echo "🎉 MediCase is starting up!"
echo ""
echo "📍 Application URLs:"
echo "   Frontend: http://localhost:4200"
echo "   Backend API: http://localhost:8080/api"
echo "   API Documentation: http://localhost:8080/swagger-ui.html"
echo ""
echo "🔑 Demo Accounts:"
echo "   Patient: patient@medicase.com / password123"
echo "   Doctor: doctor@medicase.com / password123" 
echo "   Admin: admin@medicase.com / password123"
echo ""
echo "📝 Note: Make sure MongoDB is running on localhost:27017"
echo ""
echo "To stop the application, press Ctrl+C"

# Function to cleanup processes
cleanup() {
    echo ""
    echo "🛑 Stopping MediCase..."
    kill $BACKEND_PID 2>/dev/null
    kill $FRONTEND_PID 2>/dev/null
    echo "✅ MediCase stopped successfully!"
    exit 0
}

# Trap Ctrl+C
trap cleanup INT

# Wait for user to stop
wait