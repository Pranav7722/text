# 🏥 MediCase - Digital Medical Document Vault

## 🧠 Project Overview
MediCase is a secure digital vault for storing and accessing medical documents using QR codes. Think DigiLocker for medical records - patients can store, organize, and share their medical history with healthcare providers through a simple QR code scan.

## 🩺 Problem We're Solving
- Patients lose or forget past reports, prescriptions, or test results
- Different doctors/hospitals don't have access to past records
- Elderly or rural patients struggle with paperwork
- Sensitive medical data needs privacy and access control

## ✅ Our Solution
- Each patient gets a unique QR code linking to their profile
- Doctors can scan QR codes to view medical history (if authorized)
- Patients can upload and organize reports (PDFs, images)
- Role-based access: Patients, Doctors, Admins
- Accessible on web/mobile, user-friendly for all age groups

## 🛠 Tech Stack

| Layer | Technology | Purpose |
|-------|------------|---------|
| Frontend | Angular 17 | Modern, responsive UI with forms |
| Backend | Spring Boot 3.2 | Scalable, secure REST APIs |
| Database | MongoDB | Flexible document storage |
| Authentication | JWT | Secure login and role-based access |
| File Storage | Local/Cloud | Medical document storage |
| QR Generation | Libraries | Quick profile access |

## 📋 Features

### Patient Module
- ✅ Sign up / Login
- ✅ Upload medical reports (PDF, images)
- ✅ Generate/Download personal QR code
- ✅ View medical history timeline
- ✅ Manage document sharing permissions

### Doctor Module
- ✅ Scan patient QR codes
- ✅ View shared documents (if authorized)
- ✅ Add notes or prescriptions
- ✅ Patient search functionality

### Admin Module
- ✅ User management
- ✅ Activity monitoring
- ✅ System analytics

## 🚀 Getting Started

### Prerequisites
- Node.js 18+
- Java 17+
- MongoDB 6+
- Angular CLI

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd medicase
```

2. **Backend Setup**
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

3. **Frontend Setup**
```bash
cd frontend
npm install
ng serve
```

4. **Database Setup**
```bash
# Make sure MongoDB is running
mongosh
use medicase
```

### Default Access
- **Application**: http://localhost:4200
- **API**: http://localhost:8080
- **MongoDB**: mongodb://localhost:27017/medicase

## 📱 Usage

### For Patients
1. Register/Login to your account
2. Upload medical documents
3. Generate your unique QR code
4. Share QR with healthcare providers

### For Doctors
1. Login with doctor credentials
2. Scan patient QR code
3. View authorized medical history
4. Add notes/prescriptions

## 🔐 Security Features
- JWT-based authentication
- Role-based access control
- Encrypted file storage
- Audit logging
- Data privacy compliance

## 📈 Future Enhancements
- Mobile app (React Native)
- Insurance integration
- Hospital management systems
- Multilingual support
- Telemedicine integration
- AI-powered document analysis

## 🧑‍🤝‍🧑 Team Contributions
- **Backend Development**: Spring Boot APIs, MongoDB integration, Authentication
- **Frontend Development**: Angular UI, User dashboards, QR integration
- **DevOps**: Deployment, Database management, CI/CD
- **Testing**: End-to-end testing, Documentation

## 📄 API Documentation
API documentation is available at: http://localhost:8080/swagger-ui.html

## 🤝 Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 Support
For support and questions, please create an issue in the repository.

## 📝 License
This project is licensed under the MIT License.