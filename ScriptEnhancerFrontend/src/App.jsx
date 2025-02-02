import React, { useEffect, useState } from 'react';
import { createBrowserRouter, Link, Route, RouterProvider, Routes, useNavigate } from 'react-router-dom';
import Home from './assets/components/Home';
import SignUp from './assets/components/SignUp';
import LoginForm from './assets/components/LoginForm';
import Profile from './assets/components/Profile';
import HistoryPage from'./assets/components/HistoryPage';
import AllMembers from './assets/components/AllMembers';
import ErrorPage from './assets/components/ErrorPage';
import { jwtDecode } from 'jwt-decode';
import ConfirmationModal from './assets/components/ConfirmationModal';

function App() {
  let token = localStorage.getItem("jwttoken") || sessionStorage.getItem("jwttoken");
 let [isAdmin , setIsAdmin] = useState(false);
  let tokenDecoded = null;
  let [exp , setExp] = useState(false);
  const navigate = useNavigate();

 if (token) {
   try {
     tokenDecoded = jwtDecode(token);
   } catch (error) {
     console.error("Invalid token:", error);
   }
 }
 
 const checkTokenExpiry = () => { // checking the token expiry
  const tokenExp =
    localStorage.getItem("exp") || sessionStorage.getItem("exp");
  const currentTimestamp = Math.floor(Date.now() / 1000);
  if (currentTimestamp > tokenExp) {
    setExp(true)
    localStorage.removeItem("jwttoken");
    sessionStorage.removeItem("jwttoken");
    localStorage.removeItem("exp");
    sessionStorage.removeItem("exp");
    // {<Link to={"/error"}></Link>}
    navigate("/")
  }
};


 useEffect(() => {
   if (tokenDecoded) {
     const hasAdminRole = tokenDecoded.roles?.includes("ADMIN");
     setIsAdmin(hasAdminRole);
   } else {
     setIsAdmin(false);
   }
checkTokenExpiry();
 }, []); // Runs only when `token` changes
  
  return (
    <Routes>
      <Route path="/" element={<Home token={token} isAdmin={isAdmin} checkTokenExpiry={checkTokenExpiry} exp={exp} />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="/login" element={<LoginForm tokenDecoded={tokenDecoded} />} />
      <Route path="/profile" element={<Profile checkTokenExpiry={checkTokenExpiry}  tokenDecoded={tokenDecoded} token={token} />} />
      <Route path="/history" element={<HistoryPage token={token} checkTokenExpiry={checkTokenExpiry} />} />
      <Route path="/members" element={<AllMembers token={token} isAdmin={isAdmin} tokenDecoded={tokenDecoded} checkTokenExpiry={checkTokenExpiry} />} />
      <Route path="/error" element={<ErrorPage />} />
      <Route path='/confirmationpage' element = {<ConfirmationModal/>} />
    </Routes>
  );
}

export default App;
