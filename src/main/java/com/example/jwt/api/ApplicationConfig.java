package com.example.jwt.api;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.DatatypeConverter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

@Configuration
public class ApplicationConfig {

    MessageDigest digest;

    {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public KeyPair keyPair() {
        Key pvtKey = null;
/*        ClassPathResource ksFile = new ClassPathResource("cert/jwt-auth-test.jks");
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, "test123".toCharArray());
        return ksFactory.getKeyPair("jwt-auth-test");*/
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new ClassPathResource("cert1/certificate.p12").getInputStream(), "test123".toCharArray());
            //keyStore.

             pvtKey = keyStore.getKey("1", "test123".toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return new KeyPair(null,(PrivateKey)pvtKey);
    }

    @Bean
    public JWKSet jwkSet() throws CertificateException, IOException {
        X509Certificate certificate = generateCertFromDER();
        System.out.println(" X509Certificate :"+ generateCertFromDER().toString());

        com.nimbusds.jose.util.Base64 b64 = new com.nimbusds.jose.util.Base64(DatatypeConverter.printBase64Binary(certificate.getEncoded()));     // X509Certificate

        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) certificate.getPublicKey())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS512)
                .x509CertChain(Arrays.asList(Base64.encode(certificate.getEncoded())))
                .x509CertSHA256Thumbprint(Base64URL.encode(digest.digest(certificate.getEncoded())))
                .keyID("jwt-secret");
        return new JWKSet(builder.build());

    }


    protected  X509Certificate generateCertFromDER() throws CertificateException, IOException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(new ClassPathResource("cert1/certificate.pem").getInputStream());
    }
}
