import React, { useState } from 'react';
import './Css/Home.css';
import { createBrowserRouter , Link, RouterProvider} from 'react-router-dom';

const Home = () => {
  const [userInput, setUserInput] = useState('');
  const [response, setResponse] = useState('Welcome to ScriptEnhancer! Type something to get started.');

  const handleInputChange = (e) => {
    setUserInput(e.target.value);
  };

  const handleSubmit = () => {
    // Simulate a response
    setResponse(`You said: ${userInput}`);
    setUserInput('');
  };

  return (
    <div className="flex flex-col h-screen">

    <div className='flex flex-row justify-end p-5'>
        <div>
           <Link to={"/signup"} className='text-xl'>SignUp</Link>
        </div>
    </div>
      {/* Hero Section */}
      <div className="flex-1 flex items-center justify-center">
        <h1 className="text-6xl font-thin text-white tracking-wider">
          Script Enhancer
        </h1>
      </div>

      <div className="flex flex-col justify-between items-center flex-grow px-4 py-6">
  {/* Response Box */}
  <div
    className="bg-[rgb(38, 39, 39)] border border-gray-950 p-4 rounded-lg overflow-y-auto shadow-xl"
    style={{
      maxHeight: '50vh',
      minHeight: '20vh',
      width: '60%',
    }}
  >
    <p className="text-white break-words">{response}</p>
  </div>
</div>




        {/* Input Area */}
        <div className="flex items-center mb-11 justify-center">
          <textarea
            className="flex-grow resize-none p-3 border rounded-md shadow-md focus:outline-none focus:ring-2 focus:ring-gray-800"
            rows={1}
            placeholder="Type your message..."
            value={userInput}
            onChange={handleInputChange}
            style={{ height : '3rem', maxWidth : '50%' }}
          ></textarea>
          <button
            onClick={handleSubmit}
              className="ml-4 px-4 py-2 bg-[rgb(38, 39, 39)] border border-gray-950 text-white rounded-md shadow-xl"
          >
            Send
          </button>
        </div>
      </div>
  );

};

export default Home;
