import React from "react";
import {jwtDecode} from "jwt-decode";
import { useNavigate } from "react-router-dom";

const Profile = () => {
  // Assuming you have a function to get the user email from localStorage/sessionStorage
  const navigate = useNavigate();
  const token = localStorage.getItem("jwttoken") || sessionStorage.getItem("jwttoken");
  const user = jwtDecode(token);
  console.log(user);
  

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex justify-center items-center">
      {/* Profile Card Container */}
      <div className="bg-white p-8 rounded-lg shadow-xl w-full sm:w-96">
        <div className="text-center">
          {/* Profile Picture (Optional) */}
          <img
            src="https://via.placeholder.com/150"
            alt="Profile"
            className="w-32 h-32 rounded-full mx-auto mb-6"
          />
          {/* Username */}
          <h2 className="text-2xl font-semibold text-gray-800 mb-2">User Profile</h2>
          <p className="text-sm text-gray-600 mb-6">Welcome to your profile page</p>
        </div>

        {/* Profile Details */}
        <div className="space-y-4">
          {/* Email */}
          <div className="flex justify-between items-center">
            <span className="text-gray-600 font-medium">Email:</span>
            <span className="text-gray-800">{user.sub}</span>
          </div>
        </div>

        {/* Logout Button */}
        <div className="mt-6 flex justify-center gap-3">
          <button
            className="bg-blue-500 text-white py-2 px-6 rounded-full text-sm hover:bg-blue-600 transition duration-200"
            onClick={() => {
              localStorage.removeItem("jwttoken");
              sessionStorage.removeItem("jwttoken");
              window.location.href = "/"; // Redirect to login
            }}
          >
            Log Out
          </button>

          <button
            className="bg-blue-500 text-white py-2 px-6 rounded-full text-sm hover:bg-blue-600 transition duration-200"
            onClick={() => {
             navigate("/history")
            }}
          >
            History
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile;


