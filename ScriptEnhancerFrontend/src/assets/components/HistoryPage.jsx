import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { FaTrash } from "react-icons/fa";

const HistoryPage = () => {
  const [data, setData] = useState([]);
  const [error, setError] = useState(false);

  useEffect(() => {
    const getData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/scriptenhancer/getallinputandoutput");
        console.log(response.data);
        setData(response.data);
        setError(false); // Reset error state when data is successfully fetched
      } catch (error) {
        console.log(error);
        setError(true); // Set error state if there's an error fetching data
      }
    };

    getData();
  }, []); // Fetch data only once when the component mounts

  const handleSubmit = async (id) => {
    try {
      const response = await axios.delete(
        `http://localhost:8080/api/scriptenhancer/delete/inputout?id=${id}`
      );
      console.log("Delete response:", response.data);

      // Update the state to exclude the deleted item
      setData((prevData) => prevData.filter((item) => item.id !== id));
      // setData((prevData) => [...prevData, { id }]);
    } catch (error) {
      console.error("Error deleting item:", error);
    }
  };

  return (
    <div className="min-h-screen bg-[#262727]">
      {/* Hero Section */}
      <section className="bg-[#262727] text-white py-20 text-center">
        <h1 className="text-5xl font-extrabold">History</h1>
        <p className="mt-4 text-xl">See the input and its enhanced output</p>
      </section>

      {/* Main Content */}
      <div className="container mx-auto py-12 px-6">
        {/* If error occurs while fetching */}
        {error ? (
              // If no items in data, show "No history available"
              <div className='flex justify-center items-center w-full h-fit'>
                <p className='text-6xl text-white font-medium'>No history Available</p>
              </div>
            ) : (
            
              <div className="grid grid-cols-1 gap-12">
                {data.map((item, index) => (
                  <div key={index} className="flex flex-col md:flex-row gap-8 mb-12">
                    <div className='flex justify-center items-start font-medium text-xl text-white gap-2'>
                      {item.id}.
                      <button onClick={() => handleSubmit(item.id)} className='mt-1'>
                        <FaTrash />
                      </button>
                    </div>

                    {/* Input Column */}
                    <div className="bg-[#262727] border border-[#5a5a5a] shadow-lg rounded-lg p-6 w-full md:w-1/2">
                      <h2 className="text-3xl font-semibold mb-4 text-yellow-400">Input</h2>
                      <div className="w-full p-6 rounded-lg bg-[#262727] min-h-[150px] shadow-md shadow-black">
                        <p className="text-lg text-white">{item.inputs}</p>
                      </div>
                    </div>

                    {/* Output Column */}
                    <div className="bg-[#262727] border border-[#5a5a5a] shadow-lg rounded-lg p-6 w-full md:w-1/2">
                      <h2 className="text-3xl font-semibold mb-4 text-red-500">Output</h2>
                      <div className="w-full p-6 rounded-lg bg-[#262727] min-h-[150px] shadow-md shadow-black">
                        <p className="text-lg text-white">{item.outputs}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
      </div>
    </div>
  );
};

export default HistoryPage;
