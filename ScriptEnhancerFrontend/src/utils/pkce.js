 export const generateRandomString = (length) => {
    const array = new Uint32Array(length);
    window.crypto.getRandomValues(array);
    return Array.from(array, (dec) => ('0' + dec.toString(16)).slice(-2)).join('');
  };
  
  const sha256 = async (plain) => {
    const encoder = new TextEncoder();
    const data = encoder.encode(plain);
    const hash = await window.crypto.subtle.digest('SHA-256', data);
    return btoa(String.fromCharCode(...new Uint8Array(hash)))
      .replace(/\+/g, '-')
      .replace(/\//g, '_')
      .replace(/=+$/, '');
  };
  
  export const generateCodeVerifier = () => generateRandomString(64);
  export const generateCodeChallenge = async (verifier) => sha256(verifier);