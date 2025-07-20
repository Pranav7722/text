export interface Address {
  street?: string;
  city?: string;
  state?: string;
  country?: string;
  zipCode?: string;
}

export enum UserRole {
  PATIENT = 'PATIENT',
  DOCTOR = 'DOCTOR',
  ADMIN = 'ADMIN'
}

export interface User {
  id?: string;
  email: string;
  password?: string;
  firstName: string;
  lastName: string;
  fullName?: string;
  phoneNumber?: string;
  role: UserRole;
  address?: Address;
  qrCode?: string;
  enabled?: boolean;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  user: User;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  role?: UserRole;
}