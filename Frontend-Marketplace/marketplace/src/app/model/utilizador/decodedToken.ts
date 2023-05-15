export interface DecodedToken {
    sub: string;
    name: string;
    iat: number;
    role: string;
    // Add any other claims you need here
  }