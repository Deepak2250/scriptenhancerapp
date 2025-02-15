import React, { useEffect } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";

const GitHubCallback = () => {
  useEffect(() => {
    const handleGitHubCallback = async () => {
      const code = new URLSearchParams(window.location.search).get("code");
      const codeVerifier = sessionStorage.getItem("code_verifier"); // Use sessionStorage as per your code

      if (code && codeVerifier) {
        try {
          const params = {
            code: code,
            codeVerifier: codeVerifier
          };

          const response = await axios.post("http://localhost:8080/api/auth/github", params);

           const jwtToken = response.headers.get("Authorization");
          
            if (jwtToken && jwtToken.startsWith("Bearer ")) {
              const extractedJwt = jwtToken.split(" ")[1]; // Splits "Bearer <token>" into ["Bearer", "<token>"]
              const userToken = jwtDecode(extractedJwt);

              sessionStorage.setItem("jwttoken" , extractedJwt)
              sessionStorage.setItem('exp' , userToken.exp)
              // Handle the received JWT or token\
              window.location.href = "/";
            }
          
        } catch (error) {
          alert("Google OAuth Error:", error);
          
        }
      }
    };

    handleGitHubCallback();
  }, []);

  return <>
  
  <div className="flex justify-center items-center w-full h-screen">
  <p className="text-white font-medium">Processing GitHub login...</p>;
  </div>
  </>
};

export default GitHubCallback;
