package com.example.jwt.security;

public class JwtConstants {

    private JwtConstants() {
    }

    static final String JOURNEY_TYPE = "jrn";
    public static final String JWT_HEADER_NAME = "IB-JOURNEY-TOKEN";
    public static final int DEFAULT_EXPIRY_OFFSET = 900;
    public static final int DEFAULT_THRESHOLD = 300;

    public enum ROLE {

        INTERNET_CUSTOMER_NTF ("NTFCUST"),
        INTERNET_PRSPECT_CUSTOMER_NTF ("NTFPROS"),
        BRANCH_CUSTOMER_MCA ("MCACUST"),
        INTERNET_BANKING_CUSTOMER ("IBCUST");

        private final String role;

        ROLE(String role) {
            this.role = role;
        }

        public String getRole() {
            return this.role;
        }
    }

}
