#!/bin/bash

# MediCase Project Setup Script
echo "🏥 Setting up MediCase - Digital Medical Document Vault"
echo "======================================================="

# Check if Java 17 is installed
echo "📋 Checking Java installation..."
if java -version 2>&1 | grep -q "version \"17"; then
    echo "✅ Java 17 found"
else
    echo "❌ Java 17 not found. Please install Java 17"
    exit 1
fi

# Check if Node.js is installed
echo "📋 Checking Node.js installation..."
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version)
    echo "✅ Node.js found: $NODE_VERSION"
else
    echo "❌ Node.js not found. Please install Node.js 18+"
    exit 1
fi

# Check if MongoDB is installed
echo "📋 Checking MongoDB installation..."
if command -v mongod &> /dev/null; then
    echo "✅ MongoDB found"
else
    echo "⚠️  MongoDB not found locally. You can:"
    echo "   1. Install MongoDB locally"
    echo "   2. Use Docker: docker run -d -p 27017:27017 mongo:7.0"
    echo "   3. Use MongoDB Atlas (cloud)"
fi

# Setup Backend
echo ""
echo "🔧 Setting up Backend..."
cd backend

# Make Maven wrapper executable
chmod +x mvnw

# Install dependencies and compile
echo "📦 Installing backend dependencies..."
./mvnw clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Backend setup completed"
else
    echo "❌ Backend setup failed"
    exit 1
fi

# Go back to root directory
cd ..

# Setup Frontend
echo ""
echo "🔧 Setting up Frontend..."
cd frontend

# Install npm dependencies
echo "📦 Installing frontend dependencies..."
npm install

if [ $? -eq 0 ]; then
    echo "✅ Frontend setup completed"
else
    echo "❌ Frontend setup failed"
    exit 1
fi

# Go back to root directory
cd ..

# Create startup scripts
echo ""
echo "📝 Creating startup scripts..."

# Backend startup script
cat > start-backend.sh << 'EOF'
#!/bin/bash
echo "🚀 Starting MediCase Backend..."
cd backend
./mvnw spring-boot:run
EOF

# Frontend startup script
cat > start-frontend.sh << 'EOF'
#!/bin/bash
echo "🚀 Starting MediCase Frontend..."
cd frontend
npm start
EOF

# Make scripts executable
chmod +x start-backend.sh
chmod +x start-frontend.sh

# Create environment file
echo ""
echo "⚙️  Creating environment configuration..."
cat > .env << 'EOF'
# MediCase Environment Configuration

# Database
MONGODB_URI=mongodb://localhost:27017/medicase

# JWT Configuration
JWT_SECRET=medicase-super-secret-key-for-development-2024

# Application URLs
BACKEND_URL=http://localhost:8080
FRONTEND_URL=http://localhost:4200
QR_BASE_URL=http://localhost:4200/patient

# File Upload
MAX_FILE_SIZE=50MB
UPLOAD_PATH=./uploads

# Logging
LOG_LEVEL=INFO
EOF

echo ""
echo "🎉 Setup completed successfully!"
echo ""
echo "🚀 To start the application:"
echo "   1. Start MongoDB (if not using Docker/Atlas)"
echo "   2. Run backend: ./start-backend.sh"
echo "   3. Run frontend: ./start-frontend.sh"
echo ""
echo "🌐 Application URLs:"
echo "   Frontend: http://localhost:4200"
echo "   Backend API: http://localhost:8080/api"
echo ""
echo "📖 For more information, check the README.md file"
echo ""
echo "Happy coding! 💻"