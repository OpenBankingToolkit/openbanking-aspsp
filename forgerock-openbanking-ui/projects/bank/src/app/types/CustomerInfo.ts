export interface CustomerInfo {
    "partyId": string;
    "title"?: string;
    "initials"?: string;
    "familyName"?: string;
    "givenName"?: string;
    "email"?: string;
    "phoneNumber"?: string;
    "birthdate"?: string[];
    "userID"?: string;
    "address"?: Address;
}

export interface Address {
  "addressType"?: string;
  "streetAddress"?: string[];
  "postalCode"?: string;
  "country"?: string
}
