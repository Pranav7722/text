package com.medicase.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeService {
    
    @Value("${qr.size:300}")
    private int qrCodeSize;
    
    @Value("${qr.base-url:http://localhost:4200/patient}")
    private String baseUrl;
    
    public byte[] generateQRCode(String qrCodeId) throws WriterException, IOException {
        String qrCodeData = baseUrl + "/" + qrCodeId;
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        return outputStream.toByteArray();
    }
    
    public String generateQRCodeUrl(String qrCodeId) {
        return baseUrl + "/" + qrCodeId;
    }
}