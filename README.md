# 🏥 MediCase - Digital Medical Document Vault

## 🧠 Project Overview

MediCase is a secure digital vault for storing and accessing medical documents using QR codes. Think of it as a "DigiLocker for medical records" - patients can store all their medical history digitally and share it with healthcare providers through a simple QR code scan.

## 🩺 Problem Statement

- Patients lose or forget past reports, prescriptions, or test results
- Different doctors/hospitals don't have access to past medical records
- Elderly or rural patients struggle with paperwork management
- Sensitive medical data needs robust privacy and access control

## ✅ Our Solution

- **Unique QR Code**: Each patient gets a QR code linking to their medical profile
- **Doctor Access**: Healthcare providers can scan QR codes to view authorized medical history
- **Document Management**: Upload and organize reports (PDFs, images, prescriptions)
- **Role-Based Access**: Patients, Doctors, Admins with different permission levels
- **Cross-Platform**: Accessible on web and mobile devices

## 🛠 Tech Stack

| Layer | Technology | Purpose |
|-------|------------|---------|
| Frontend | Angular 17 | Modern, responsive UI with component-based architecture |
| Backend | Spring Boot 3.2 | Scalable REST APIs with security |
| Database | MongoDB | Flexible document storage for medical files |
| Authentication | JWT | Secure login and role-based access control |
| QR Generation | qrcode.js & ZXing | QR code generation and scanning |
| File Storage | GridFS (MongoDB) | Secure medical document storage |
| Styling | Angular Material | Professional healthcare UI components |

## 📋 Features

### Patient Module
- ✅ User registration and authentication
- ✅ Upload medical reports (PDF, images)
- ✅ Generate personal QR code
- ✅ View medical history timeline
- ✅ Manage document privacy settings

### Doctor Module
- ✅ Scan patient QR codes
- ✅ View authorized patient documents
- ✅ Add medical notes and prescriptions
- ✅ Patient search functionality

### Admin Module
- ✅ User management
- ✅ System monitoring
- ✅ Access logs and analytics

## 🚀 Quick Start

### Prerequisites
- Node.js 18+ and npm
- Java 17+
- MongoDB 7.0+
- Git

### Backend Setup
```bash
cd backend
./mvnw spring-boot:run
```
Backend will run on `http://localhost:8080`

### Frontend Setup
```bash
cd frontend
npm install
ng serve
```
Frontend will run on `http://localhost:4200`

### MongoDB Setup
```bash
# Install MongoDB locally or use MongoDB Atlas
mongod --dbpath /your/db/path
```

## 📁 Project Structure

```
medicase/
├── backend/                 # Spring Boot application
│   ├── src/main/java/
│   │   └── com/medicase/
│   │       ├── controller/  # REST API endpoints
│   │       ├── service/     # Business logic
│   │       ├── model/       # Data models
│   │       ├── repository/  # Data access layer
│   │       ├── config/      # Configuration classes
│   │       └── security/    # Authentication & authorization
│   ├── pom.xml             # Maven dependencies
│   └── application.yml     # Configuration
├── frontend/               # Angular application
│   ├── src/app/
│   │   ├── components/     # UI components
│   │   ├── services/       # API services
│   │   ├── models/         # TypeScript interfaces
│   │   ├── guards/         # Route guards
│   │   └── modules/        # Feature modules
│   ├── package.json
│   └── angular.json
├── docs/                   # Documentation
└── README.md
```

## 🔐 Security Features

- JWT-based authentication
- Role-based access control (RBAC)
- HTTPS enforcement
- Input validation and sanitization
- File type validation for uploads
- Rate limiting on API endpoints

## 🎯 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Token refresh

### Patient Management
- `GET /api/patients/profile` - Get patient profile
- `PUT /api/patients/profile` - Update patient profile
- `GET /api/patients/qr` - Generate QR code

### Document Management
- `POST /api/documents/upload` - Upload medical document
- `GET /api/documents` - List patient documents
- `GET /api/documents/{id}` - Download document
- `DELETE /api/documents/{id}` - Delete document

### Doctor Access
- `POST /api/doctor/scan` - Access patient via QR
- `GET /api/doctor/patients` - List accessible patients
- `POST /api/doctor/notes` - Add medical notes

## 🏗 Database Schema

### Users Collection
```json
{
  "_id": "ObjectId",
  "email": "string",
  "password": "string (hashed)",
  "role": "PATIENT | DOCTOR | ADMIN",
  "profile": {
    "firstName": "string",
    "lastName": "string",
    "dateOfBirth": "date",
    "phoneNumber": "string",
    "address": "object"
  },
  "qrCode": "string",
  "createdAt": "date",
  "updatedAt": "date"
}
```

### Documents Collection
```json
{
  "_id": "ObjectId",
  "patientId": "ObjectId",
  "fileName": "string",
  "fileType": "string",
  "fileSize": "number",
  "uploadDate": "date",
  "category": "string",
  "description": "string",
  "gridFSFileId": "ObjectId"
}
```

## 🧪 Testing

### Backend Testing
```bash
cd backend
./mvnw test
```

### Frontend Testing
```bash
cd frontend
npm test              # Unit tests
npm run e2e          # End-to-end tests
```

## 🚀 Deployment

### Using Docker
```bash
docker-compose up -d
```

### Manual Deployment
1. Deploy MongoDB (Atlas recommended)
2. Deploy Spring Boot backend (Render/Railway)
3. Deploy Angular frontend (Vercel/Netlify)

## 🔮 Future Enhancements

- 📱 Mobile app (React Native/Flutter)
- 🔗 Hospital management system integration
- 🌍 Multi-language support
- 🤖 AI-powered health insights
- 📊 Health analytics dashboard
- 🔔 Appointment reminders
- 💊 Medication tracking

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For support, email support@medicase.com or create an issue in this repository.

---

**Built with ❤️ for better healthcare accessibility**