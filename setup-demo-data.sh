#!/bin/bash

# MediCase Demo Data Setup Script
echo "🏥 Setting up MediCase Demo Data..."
echo ""

# Check if mongosh is available
if ! command -v mongosh >/dev/null 2>&1; then
    echo "❌ MongoDB shell (mongosh) is not installed."
    echo "Please install MongoDB and mongosh to run this script."
    echo "Visit: https://www.mongodb.com/docs/mongodb-shell/install/"
    exit 1
fi

echo "📊 Creating demo users in MongoDB..."

# MongoDB script to create demo users
mongosh --eval '
use medicase;

// Clear existing users (optional)
db.users.deleteMany({});

// Create demo users
db.users.insertMany([
  {
    firstName: "John",
    lastName: "Patient",
    email: "patient@medicase.com",
    password: "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", // password123
    phoneNumber: "+1234567890",
    dateOfBirth: "1990-01-15",
    gender: "Male",
    address: "123 Main St, City, State 12345",
    role: "PATIENT",
    enabled: true,
    accountNonExpired: true,
    accountNonLocked: true,
    credentialsNonExpired: true,
    emergencyContact: "+1234567899",
    bloodGroup: "O+",
    allergies: "Penicillin, Nuts",
    qrCodeId: "MED-12345678",
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    firstName: "Dr. Sarah",
    lastName: "Doctor",
    email: "doctor@medicase.com",
    password: "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", // password123
    phoneNumber: "+1234567891",
    dateOfBirth: "1985-05-20",
    gender: "Female",
    address: "456 Hospital Ave, City, State 12345",
    role: "DOCTOR",
    enabled: true,
    accountNonExpired: true,
    accountNonLocked: true,
    credentialsNonExpired: true,
    licenseNumber: "MD123456",
    specialization: "General Medicine",
    hospitalAffiliation: "City General Hospital",
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    firstName: "Admin",
    lastName: "User",
    email: "admin@medicase.com",
    password: "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", // password123
    phoneNumber: "+1234567892",
    dateOfBirth: "1980-12-10",
    gender: "Other",
    address: "789 Admin St, City, State 12345",
    role: "ADMIN",
    enabled: true,
    accountNonExpired: true,
    accountNonLocked: true,
    credentialsNonExpired: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }
]);

print("✅ Demo users created successfully!");
print("");
print("🔑 Demo Account Credentials:");
print("Patient: patient@medicase.com / password123");
print("Doctor: doctor@medicase.com / password123");
print("Admin: admin@medicase.com / password123");
print("");
print("📱 Patient QR Code ID: MED-12345678");
'

if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 Demo data setup completed successfully!"
    echo ""
    echo "You can now start the application and login with the demo accounts."
    echo "Run: ./start-dev.sh"
else
    echo ""
    echo "❌ Failed to setup demo data. Please check your MongoDB connection."
    echo "Make sure MongoDB is running on localhost:27017"
fi