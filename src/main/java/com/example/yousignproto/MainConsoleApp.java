package com.example.yousignproto;

import com.example.yousignproto.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MainConsoleApp implements CommandLineRunner {
    private final HttpUtils httpUtils;

    public MainConsoleApp(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    @Override
    public void run(String... args) throws Exception {

        File fileToSign = new File("blank.pdf");

        String signatureName = "signature";
        String deliveryMode = "email";
        SignatureRequestDto dto = new SignatureRequestDto(signatureName, deliveryMode);

        ObjectMapper objectMapper = new ObjectMapper();
        String signatureRequestJson = objectMapper.writeValueAsString(dto);

        // Create signature request
        Response createSignatureResponse = httpUtils.jsonPostRequest("/signature_requests",signatureRequestJson);
        SignatureRequest signatureRequest = objectMapper.readValue(createSignatureResponse.body().string(), SignatureRequest.class);

        // Add documents
        Response addDocumentResponse = httpUtils.multipartPostRequest("/signature_requests/" + signatureRequest.id() + "/documents", fileToSign);
        DocumentDto documentDto = objectMapper.readValue(addDocumentResponse.body().string(), DocumentDto.class);

        // Add signers
        SignerInfoDto signerInfo = new SignerInfoDto("Salim", "Khengui", "salim.khengui@tripica.com", "en");
        AddSignerDto signer = new AddSignerDto(signerInfo, "electronic_signature", "no_otp");
        String signerJson = objectMapper.writeValueAsString(signer);
        Response addSignerResponse = httpUtils.jsonPostRequest("/signature_requests/"+ signatureRequest.id() + "/signers", signerJson);
        SignerDto signerResponse = objectMapper.readValue(addSignerResponse.body().string(), SignerDto.class);

        // Add signature field
        AddFieldDto addFieldDto = new AddFieldDto("signature",
                signerResponse.id(), 1, 200, 200);
        String addFieldJson = objectMapper.writeValueAsString(addFieldDto);
        httpUtils.jsonPostRequest("/signature_requests/" + signatureRequest.id() + "/documents/" + documentDto.id() + "/fields", addFieldJson);

        // Activate signature request
        Response activateSignatureResponse = httpUtils.jsonPostRequest("/signature_requests/"+ signatureRequest.id() + "/activate", "{}");
        System.out.println(activateSignatureResponse.body().string());
    }
}
