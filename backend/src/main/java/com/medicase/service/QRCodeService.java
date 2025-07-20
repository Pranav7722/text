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
import java.util.Base64;
import java.util.UUID;

@Service
public class QRCodeService {

    @Value("${app.qr.base-url}")
    private String baseUrl;

    @Value("${app.qr.image-size}")
    private int imageSize;

    public String generateQRCode(String userId) {
        String qrData = UUID.randomUUID().toString();
        return qrData;
    }

    public String generateQRCodeImage(String qrData) throws WriterException, IOException {
        String qrUrl = baseUrl + "/" + qrData;
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrUrl, BarcodeFormat.QR_CODE, imageSize, imageSize);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        byte[] qrCodeBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }

    public String generateQRCodeDataUrl(String qrData) throws WriterException, IOException {
        String base64Image = generateQRCodeImage(qrData);
        return "data:image/png;base64," + base64Image;
    }

    public boolean isValidQRCode(String qrCode) {
        try {
            UUID.fromString(qrCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}