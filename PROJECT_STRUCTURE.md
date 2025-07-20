# 🏥 MediCase Project Structure

## 📁 Root Directory
```
medicase/
├── README.md                     # Main project documentation
├── PROJECT_STRUCTURE.md          # This file
├── start-dev.sh                  # Development startup script
├── setup-demo-data.sh            # Demo data setup script
├── backend/                      # Spring Boot backend
├── frontend/                     # Angular frontend
└── hello                         # Example file (can be removed)
```

## 🚀 Backend Structure (Spring Boot)
```
backend/
├── pom.xml                       # Maven dependencies and configuration
├── mvnw                          # Maven wrapper script
├── .mvn/                         # Maven wrapper files
├── src/
│   ├── main/
│   │   ├── java/com/medicase/
│   │   │   ├── MedicaseApplication.java              # Main Spring Boot application
│   │   │   ├── model/                                # Data models/entities
│   │   │   │   ├── User.java                         # User entity (Patient/Doctor/Admin)
│   │   │   │   ├── Role.java                         # User role enum
│   │   │   │   ├── MedicalDocument.java              # Medical document entity
│   │   │   │   └── DocumentType.java                 # Document type enum
│   │   │   ├── repository/                           # Data access layer
│   │   │   │   ├── UserRepository.java               # User data operations
│   │   │   │   └── MedicalDocumentRepository.java    # Document data operations
│   │   │   ├── service/                              # Business logic layer
│   │   │   │   ├── UserService.java                  # User management service
│   │   │   │   ├── MedicalDocumentService.java       # Document management service
│   │   │   │   ├── QRCodeService.java                # QR code generation service
│   │   │   │   └── FileStorageService.java           # File upload/storage service
│   │   │   ├── controller/                           # REST API controllers
│   │   │   │   ├── AuthController.java               # Authentication endpoints
│   │   │   │   ├── QRController.java                 # QR code endpoints
│   │   │   │   └── DocumentController.java           # Document management endpoints
│   │   │   ├── security/                             # Security configuration
│   │   │   │   ├── JwtTokenProvider.java             # JWT token management
│   │   │   │   └── JwtAuthenticationFilter.java      # JWT request filter
│   │   │   ├── config/                               # Configuration classes
│   │   │   │   └── SecurityConfig.java               # Spring Security configuration
│   │   │   ├── dto/                                  # Data Transfer Objects
│   │   │   │   ├── AuthRequest.java                  # Login request DTO
│   │   │   │   ├── AuthResponse.java                 # Login response DTO
│   │   │   │   └── UserRegistrationRequest.java      # Registration request DTO
│   │   │   └── exception/                            # Custom exceptions
│   │   │       ├── ResourceNotFoundException.java    # Resource not found exception
│   │   │       ├── UserAlreadyExistsException.java   # Duplicate user exception
│   │   │       └── FileStorageException.java         # File operation exception
│   │   └── resources/
│   │       └── application.yml                       # Application configuration
│   └── test/                                         # Test files
└── uploads/                                          # File upload directory (created at runtime)
```

## 🎨 Frontend Structure (Angular)
```
frontend/
├── package.json                  # npm dependencies and scripts
├── angular.json                  # Angular CLI configuration
├── tsconfig.json                 # TypeScript configuration
├── src/
│   ├── main.ts                   # Application bootstrap
│   ├── index.html                # Main HTML template
│   ├── styles.scss               # Global styles
│   ├── environments/             # Environment configurations
│   │   ├── environment.ts        # Development environment
│   │   └── environment.prod.ts   # Production environment
│   └── app/
│       ├── app.component.ts      # Root component
│       ├── app.component.html    # Root component template
│       ├── app.component.scss    # Root component styles
│       ├── app.config.ts         # Application configuration
│       ├── app.routes.ts         # Main routing configuration
│       ├── core/                 # Core application modules
│       │   ├── services/         # Core services
│       │   │   ├── auth.service.ts           # Authentication service
│       │   │   ├── document.service.ts       # Document management service
│       │   │   └── qr.service.ts             # QR code service
│       │   ├── guards/           # Route guards
│       │   │   └── auth.guard.ts             # Authentication guard
│       │   ├── interceptors/     # HTTP interceptors
│       │   │   └── auth.interceptor.ts       # JWT token interceptor
│       │   └── models/           # TypeScript interfaces
│       │       ├── user.model.ts             # User-related interfaces
│       │       └── document.model.ts         # Document-related interfaces
│       ├── shared/               # Shared components and services
│       │   ├── components/       # Reusable UI components
│       │   └── services/         # Shared services
│       ├── features/             # Feature modules
│       │   ├── auth/             # Authentication feature
│       │   │   ├── auth.routes.ts            # Auth routing
│       │   │   ├── login/                    # Login component
│       │   │   │   ├── login.component.ts
│       │   │   │   ├── login.component.html
│       │   │   │   └── login.component.scss
│       │   │   └── register/                 # Registration component
│       │   │       ├── register.component.ts
│       │   │       ├── register.component.html
│       │   │       └── register.component.scss
│       │   ├── patient/          # Patient portal
│       │   │   ├── patient.routes.ts         # Patient routing
│       │   │   ├── dashboard/                # Patient dashboard
│       │   │   │   ├── patient-dashboard.component.ts
│       │   │   │   ├── patient-dashboard.component.html
│       │   │   │   └── patient-dashboard.component.scss
│       │   │   └── profile/                  # Patient profile
│       │   │       └── patient-profile.component.ts
│       │   ├── doctor/           # Doctor portal
│       │   │   ├── doctor.routes.ts          # Doctor routing
│       │   │   └── dashboard/                # Doctor dashboard
│       │   │       └── doctor-dashboard.component.ts
│       │   └── admin/            # Admin portal
│       │       ├── admin.routes.ts           # Admin routing
│       │       └── dashboard/                # Admin dashboard
│       │           └── admin-dashboard.component.ts
│       └── layouts/              # Layout components
└── node_modules/                 # npm packages
```

## 🔧 Key Technologies & Dependencies

### Backend Dependencies
- **Spring Boot 3.2** - Main framework
- **Spring Security** - Authentication & authorization
- **Spring Data MongoDB** - Database integration
- **JWT (jjwt)** - Token-based authentication
- **ZXing** - QR code generation
- **SpringDoc OpenAPI** - API documentation
- **Commons IO** - File operations

### Frontend Dependencies
- **Angular 17** - Frontend framework
- **Angular Material** - UI component library
- **RxJS** - Reactive programming
- **TypeScript** - Programming language
- **SCSS** - Styling

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- MongoDB 6+
- npm or yarn

### Quick Start
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd medicase
   ```

2. **Setup MongoDB and demo data**
   ```bash
   # Make sure MongoDB is running, then:
   ./setup-demo-data.sh
   ```

3. **Start the application**
   ```bash
   ./start-dev.sh
   ```

4. **Access the application**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080/api
   - API Docs: http://localhost:8080/swagger-ui.html

### Demo Accounts
- **Patient**: patient@medicase.com / password123
- **Doctor**: doctor@medicase.com / password123  
- **Admin**: admin@medicase.com / password123

## 📋 Features Implementation Status

### ✅ Completed
- Project structure setup
- Spring Boot backend with MongoDB
- JWT authentication system
- User registration and login
- Role-based access control
- Angular frontend with Material Design
- Responsive login/register forms
- Patient dashboard with QR code display
- Doctor and admin dashboards (basic)
- API documentation with Swagger

### 🚧 In Progress / Coming Soon
- Document upload functionality
- QR code scanning for doctors
- Patient profile management
- Document sharing controls
- File download and viewing
- Search and filtering
- Admin user management
- System analytics
- Mobile responsiveness enhancements

### 🔮 Future Enhancements
- Mobile app (React Native)
- Real-time notifications
- Advanced document categorization
- Insurance integration
- Telemedicine features
- Multi-language support
- Advanced analytics dashboard

## 🔐 Security Features
- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption with BCrypt
- CORS configuration
- Input validation
- File upload security
- API endpoint protection

## 📚 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/verify` - Token verification

### QR Code
- `GET /api/qr/generate` - Generate QR code image
- `GET /api/qr/url` - Get QR code URL
- `GET /api/qr/patient/{qrCodeId}` - Get patient info by QR code

### Documents
- `POST /api/documents/upload` - Upload document
- `GET /api/documents/patient/{patientId}` - Get patient documents
- `GET /api/documents/{documentId}/download` - Download document
- `DELETE /api/documents/{documentId}` - Delete document

## 🧪 Testing
- Unit tests for backend services
- Integration tests for API endpoints
- Frontend component tests
- End-to-end testing setup

## 🚀 Deployment
- Docker containerization
- Environment-specific configurations
- Production build optimization
- Database migration scripts