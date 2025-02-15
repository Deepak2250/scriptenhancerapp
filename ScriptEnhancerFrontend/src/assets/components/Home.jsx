import React, { useEffect, useState } from 'react';
import './Css/Home.css';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { ThreeDot } from "react-loading-indicators";


const Home = ({token , isAdmin , exp}) => {
  const [userInput, setUserInput] = useState('');
  const [response, setResponse] = useState('Welcome to ScriptEnhancer! Type something to get started.');
  const [loading, setLoading] = useState(false); // New loading state

  const handleInputChange = (e) => {
    setUserInput(e.target.value);
  };

  const handleSubmit = async () => {
    if (token == null) {
      alert("Login first to access the app");
      return;
    }

    if (userInput.trim() === "") {
      alert("Input cannot be empty");
      return;
    }

    setLoading(true); // Start loading animation

    try {
      const requestData = { inputText: userInput };
      const apiResponse = await axios.post("http://localhost:8080/api/scriptenhancer/enhance", 
       requestData
      , 
    {
      headers : {
        Authorization : `Bearer ${token}`,
        "Content-Type" :  "application/json",
      }
    }
    );


      // Assuming the API returns the enhanced story in the 'enhancedText' field
      const enhancedText = apiResponse.data;
      
      setResponse(enhancedText);

      setUserInput(''); // Clear input after submission
    } catch (error) {
      setResponse("An error occurred while enhancing the story. Please try again.");
      console.error(error);
    } finally {
      setLoading(false); // Stop loading animation once the API call is complete
    }
  };

  useEffect(() => {
  
    // checkTokenExpiry();
  }, []);

  return (
    <div className="flex flex-col h-screen">
      <div className="flex flex-row justify-end p-5 gap-5">

        {isAdmin &&
          <div className="bg-[#262727] border border-[#5a5a5a] rounded-lg w-24 h-10 flex justify-center items-center">
          <Link to="/members" className="text-xl text-white font-normal">Members</Link>
          </div>
          }
        <div className="bg-[#262727] border border-[#5a5a5a] rounded-lg w-24 h-10 flex justify-center items-center">
          {token && !exp?(
            <Link to="/profile" className="text-xl text-white font-normal">Profile</Link>
          ) : (
            <Link to="/signup" className="text-xl text-white font-normal">SignUp</Link>
          )}
        </div>
      </div>

      {/* Hero Section */}
      <div className="flex-col flex items-center justify-center h-40">
        <h1 className="text-6xl font-semibold text-white tracking-wider">
          Story Enhancer
        </h1>
      </div>

      <div className="flex flex-col justify-between items-center flex-grow px-4 py-6">
        {/* Response Box */}
        <div
          className="bg-[rgb(38, 39, 39)] border border-[#5a5a5a] p-4 rounded-lg overflow-y-auto shadow-xl"
          style={{ maxHeight: '50vh', minHeight: '20vh', width: '60%' }}
        >
          <p className="text-[#b4b4b4] break-words" >
            
            {loading ? (<ThreeDot variant="bounce" color="#32cd32" size="medium" text="" textColor="" />) 
            :
            (
              response
            )}
             </p>
        </div>
      </div>

      {/* Input Area */}
      <div className="flex items-center mb-11 justify-center">
        <textarea
          className="flex-grow resize-none p-3 text-white bg-[#262727] border border-[#5a5a5a] rounded-md shadow-md focus:outline-none focus:ring-2 focus:ring-gray-800 custom-scrollbar"
          rows={1}
          placeholder="Type your message..."
          value={userInput}
          onChange={handleInputChange}
          style={{ height: '4rem', maxWidth: '50%' }}
        ></textarea>
        <button
          onClick={handleSubmit}
          className="ml-4 px-4 py-2 bg-[rgb(38, 39, 39)] border border-[#5a5a5a] text-white rounded-md shadow-xl bg-[#262727]"
        >
          Send
        </button>
      </div>
    </div>
  );
};

export default Home;
