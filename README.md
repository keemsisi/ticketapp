Backend Application Built with Java
- Java17 

Generate JKS File:
- `keytool -genkey -alias passport-oauth-jwt -keyalg RSA -keystore passport-oauth-jwt.jks -keysize 2048 -storepass passport-oauth-jwt -dname "CN=DevHub, OU=IT, O=devwizardhub, L=City, ST=State, C=US"`