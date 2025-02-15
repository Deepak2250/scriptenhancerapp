import { generateCodeChallenge, generateCodeVerifier  , generateRandomString} from "../../utils/pkce";

export const LoginWithGithub = async () => {
    const codeVerifier = generateCodeVerifier();
    const codeChallenge = await generateCodeChallenge(codeVerifier);

    // Store the code verifier in session storage (temporary)
    sessionStorage.setItem('code_verifier', codeVerifier);

    const params = new URLSearchParams({
      client_id: 'Ov23li7XMZBxnH3K31ME', // Replace with your GitHub client ID
      redirect_uri: 'http://localhost:5173/oauth2/callback/github', // Must match GitHub OAuth app settings
      scope: 'user:email', // Requested scopes
      response_type: 'code',
      state: generateRandomString(16), // Optional but recommended for security
      code_challenge: codeChallenge,
      code_challenge_method: 'S256',
      prompt: 'login', // This forces GitHub to show the login page
    });

    // Redirect the user to GitHub's authorization endpoint
    window.location.href = `https://github.com/login/oauth/authorize?${params.toString()}`;
  };