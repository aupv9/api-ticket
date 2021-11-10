package com.apps.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface QRCodeService {
     BufferedImage generateQRCodeImage(String barcodeText) throws Exception;
     BufferedImage generatePDF417BarcodeImage(String barcodeText) throws Exception;
     String decodeQRCode(File qrCodeimage) throws IOException;
}
