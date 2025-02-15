import axios from "axios";
import { jwtDecode } from "jwt-decode";
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import { LoginWithGithub } from "./LoginWithGithub";
import { LoginWithGoogle } from "./LoginWithGoogle";

const LoginForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const [serversideErrors, setServerSideErrors] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const [github, setGithub] = useState(false);
  const navigate = useNavigate();

  // Handling the onSubmiut form button
  const onSubmit = async (data) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/auth/login",
        data
      );
      const jwtToken = response.headers.get("Authorization");

      if (jwtToken && jwtToken.startsWith("Bearer ")) {
        const bearerToken = jwtToken.split(" ")[1]; // Splits "Bearer <token>" into ["Bearer", "<token>"]
        const user = jwtDecode(bearerToken);
        console.log("Extracted Token:", bearerToken);
        if (rememberMe) {
          localStorage.setItem("jwttoken", bearerToken);
          localStorage.setItem("exp", user.exp);
        } else {
          sessionStorage.setItem("jwttoken", bearerToken);
          sessionStorage.setItem("exp", user.exp);
        }
        //  {<Link to={"/"}></Link>}
        window.location.href = "/"; // Redirect to login
      }

      setServerSideErrors(false);
    } catch (error) {
      console.error("Signup Failed:", error.response?.data || error.response);
      setServerSideErrors(true);
      if (error.response?.data?.errors) {
        alert("Validation Errors: " + error.response.data.errors.join(", "));
      } else {
        alert("Signup Failed. Please try again.");
      }
    }
  };

  /// Github

  const onGihubClick = () => {
    setGithub(true);
  };

  return (
    <div className="min-h-screen w-100 flex items-center justify-center px-4">
      <div className="w-full max-w-md bg-[#262727] border border-[#5a5a5a] shadow-2xl rounded-2xl overflow-hidden">
        <div className="p-6">
          <h2 className="text-3xl font-bold text-white text-center">Login</h2>
          <p
            className={
              serversideErrors
                ? "text-red-600 text-center mt-2"
                : "text-white text-center mt-2"
            }
          >
            {serversideErrors
              ? "The email or password is incorrect."
              : "Please login to your account"}
          </p>
          <form onSubmit={handleSubmit(onSubmit)} className="mt-6">
            <div className="mb-4">
              <label
                htmlFor="email"
                className="block text-white text-sm font-semibold mb-2"
              >
                Email
              </label>
              <input
                type="email"
                id="email"
                placeholder="Enter your email"
                {...register("email", {
                  required: "Email is required",
                  pattern: {
                    value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,
                    message: "Invalid email format",
                  },
                })}
                className={`w-full px-4 py-2 border rounded-lg text-black focus:ring-2 focus:ring-blue-400 focus:outline-none ${
                  errors.email ? "border-red-500" : "border-gray-300"
                }`}
              />
              {errors.email && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.email.message}
                </p>
              )}
            </div>
            <div className="mb-4">
              <label
                htmlFor="password"
                className="block text-white text-sm font-semibold mb-2"
              >
                Password
              </label>
              <input
                type="password"
                id="password"
                placeholder="Enter your password"
                {...register("password", {
                  required: "Password is required",
                  minLength: {
                    value: 6,
                    message: "Password must be at least 6 characters",
                  },
                })}
                className={`w-full px-4 py-2 border rounded-lg text-black focus:ring-2 focus:ring-blue-400 focus:outline-none ${
                  errors.password ? "border-red-500" : "border-gray-300"
                }`}
              />
              {errors.password && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.password.message}
                </p>
              )}
            </div>
            <div className="flex items-center justify-between">
              <label className="flex items-center text-white">
                <input
                  type="checkbox"
                  className="mr-2 leading-tight focus:ring-blue-400"
                  onChange={() => setRememberMe(!rememberMe)}
                  checked={rememberMe}
                />
                <span className="text-sm">Remember me</span>
              </label>
              <a href="#" className="text-sm text-yellow-400 hover:underline">
                Forgot Password?
              </a>
            </div>
            <button
              type="submit"
              className="w-full mt-6  bg-yellow-400 hover:bg-yellow-700 text-black font-bold py-2 rounded-lg shadow-md hover:shadow-lg transform hover:scale-105 transition-transform"
            >
              Login
            </button>

            <button
              type="button" // Use type="button" to avoid form submission
              className="w-full mt-6  bg-yellow-400 hover:bg-yellow-700 text-black font-bold py-2 rounded-lg shadow-md hover:shadow-lg transform hover:scale-105 transition-transform"
              onClick={LoginWithGithub} // Directly call LoginWithGithub
            >
              Login with GitHub
            </button>

            <button
              type="button" // Use type="button" to avoid form submission
              className="w-full mt-6  bg-yellow-400 hover:bg-yellow-700 text-black font-bold py-2 rounded-lg shadow-md hover:shadow-lg transform hover:scale-105 transition-transform"
              onClick={LoginWithGoogle} // Directly call LoginWithGithub
            >
              Login with Google
            </button>

          </form>
          <p className="text-center text-white mt-4">
            Don't have an account?{" "}
            <Link
              to={"/signup"}
              className="text-yellow-400 font-semibold hover:underline"
            >
              Sign up
            </Link>
          </p>
        </div>
      </div>
      {github && <LoginWithGithub />}
    </div>
  );
};

export default LoginForm;
