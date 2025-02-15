import { generateCodeChallenge, generateCodeVerifier, generateRandomString } from "../../utils/pkce";

export const LoginWithGoogle = async () => {
    const codeVerifier = generateCodeVerifier();
    const codeChallenge = await generateCodeChallenge(codeVerifier);

    // Store the code verifier in session storage (temporary)
    sessionStorage.setItem('code_verifier', codeVerifier);

    const params = new URLSearchParams({
        client_id: '118378063633-l9pddge69bbu8jjcl3r16c23pqpe2ham.apps.googleusercontent.com', // Replace with your Google OAuth 2.0 client ID
        redirect_uri: 'http://localhost:5173/oauth2/callback/google', // Must match your Google OAuth settings
        scope: 'openid email profile', // Google OAuth 2.0 scopes for email and profile
        response_type: 'code',
        state: generateRandomString(16), // Recommended for security
        code_challenge: codeChallenge,
        code_challenge_method: 'S256',
        prompt: 'consent', // Optional: forces the Google consent page to show
    });

    // Redirect the user to Google's authorization endpoint
    window.location.href = `https://accounts.google.com/o/oauth2/v2/auth?${params.toString()}`;
};
