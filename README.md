## spring-oauth2-demo
This is sample project which demonstrate how to enable OAuth2 with Spring Boot. This sample including:
- Public & Private Key for secret code: codelab
- Authoriation Server
- Resource Server
- Client

## how to generate public & private key
- Make sure keytool, which is Java tool, exist in your PATH
- Execute following to generate key pair: jwt.jks
```
    keytool -genkeypair -alias codelab -keyalg RSA -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US" -keypass codelab -keystore jwt.jks -storepass codelab
```
- Execute following to generate certificate: public.cer
```
    keytool -export -keystore jwt.jks -alias codelab -file public.cer
```
- Execute following to generate public key
```
    openssl x509 -inform der -in public.cer -pubkey -noout
```