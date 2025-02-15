import React, { useEffect, useState } from "react";
import {
  createBrowserRouter,
  Link,
  Route,
  RouterProvider,
  Routes,
  useNavigate,
} from "react-router-dom";
import Home from "./assets/components/Home";
import SignUp from "./assets/components/SignUp";
import LoginForm from "./assets/components/LoginForm";
import Profile from "./assets/components/Profile";
import HistoryPage from "./assets/components/HistoryPage";
import AllMembers from "./assets/components/AllMembers";
import ErrorPage from "./assets/components/ErrorPage";
import { jwtDecode } from "jwt-decode";
import GitHubCallback from "./assets/components/GitHubCallback";
import GoogleCallBack from "./assets/components/GoogleCallback";

function App() {
  let token =
    localStorage.getItem("jwttoken") || sessionStorage.getItem("jwttoken");
  let [isAdmin, setIsAdmin] = useState(false);
  let tokenDecoded = null;
  let [exp, setExp] = useState(false);
  const navigate = useNavigate();

  if (token) {
    try {
      tokenDecoded = jwtDecode(token);
    } catch (error) {
      console.error("Invalid token:", error);
    }
  }

  const checkTokenExpiry = () => {
    // checking the token expiry
    const tokenExp =
      localStorage.getItem("exp") || sessionStorage.getItem("exp");
    const currentTimestamp = Math.floor(Date.now() / 1000);

    if (currentTimestamp > tokenExp) {
      setExp(true);
      localStorage.clear();
      sessionStorage.clear();
      // {<Link to={"/error"}></Link>}
      navigate("/");
    }
  };

  useEffect(() => {
    if (tokenDecoded) {
      const hasAdminRole = tokenDecoded.roles?.includes("ADMIN");
      setIsAdmin(hasAdminRole);
    } else {
      setIsAdmin(false);
    }

    if (token) {
      checkTokenExpiry();

      const interval = setInterval(() => {
        console.log("Checking for the jwt");
        checkTokenExpiry();
      }, 10000); // Runs every 60 seconds

      return () => clearInterval(interval); // Cleans up when component unmounts
    }
  }, [token]); // Runs only when `token` changes

  return (
    <Routes>
      <Route
        path="/"
        element={<Home token={token} isAdmin={isAdmin} exp={exp} />}
      />
      <Route path="/signup" element={<SignUp />} />
      <Route
        path="/login"
        element={<LoginForm tokenDecoded={tokenDecoded} />}
      />
      <Route
        path="/profile"
        element={<Profile tokenDecoded={tokenDecoded} token={token} />}
      />
      <Route path="/history" element={<HistoryPage token={token} />} />
      <Route
        path="/members"
        element={
          <AllMembers
            token={token}
            isAdmin={isAdmin}
            tokenDecoded={tokenDecoded}
          />
        }
      />
      <Route path="/error" element={<ErrorPage />} />
      <Route
        path="/oauth2/callback/github"
        element={<GitHubCallback></GitHubCallback>}
      />

      <Route
        path="/oauth2/callback/google"
        element={<GoogleCallBack/>}
      />
    </Routes>
  );
}

export default App;
