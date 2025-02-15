import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { FaRegTrashAlt } from "react-icons/fa";
import ConfirmationModal from "./ConfirmationModal";

const HistoryPage = ({token}) => {
  const [data, setData] = useState([]);
  const [error, setError] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [selectedId, setSelectedId] = useState(null);
  const [loading, setLoading] = useState(false); // New loading state

  useEffect(() => {
    const getData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/scriptenhancer/history" , {
          headers :{
            Authorization: `Bearer ${token}`,
          }
        });
        setData(response.data);
        
        setError(false); // Reset error state when data is successfully fetched
      } catch (error) {
        setError(true); // Set error state if there's an error fetching data
      }
    };

    // checkTokenExpiry();
    getData();
  }, []); // Fetch data only once when the component mounts

  const handleDeleteClick = ((id) =>{
     setSelectedId(id);
     setShowModal(true);

  });

  const handleConfirmDeletion = async () => {

    setShowModal(false);
    if (!selectedId) {
      return
    }
    try {
      const response = await axios.delete(
        `http://localhost:8080/api/scriptenhancer/delete?id=${selectedId}` , {
          headers : {
            Authorization : `Bearer ${token}`,
          }
        }
      );

      // Update the state to exclude the deleted item
      setData((prevData) => prevData.filter((item) => item.id !== selectedId));
    } catch (error) {
      alert("Error deleting item:", error);
    }
  };

  return (
    <div className="min-h-screen bg-[#262727]">
      {/* Hero Section */}
      <section className="bg-[#262727] text-white py-20 text-center">
        <h1 className="text-5xl font-extrabold">History</h1>
        <p className="mt-4 text-xl">See the input and its enhanced output</p>
      </section>

      {loading ? (
        <div className="w-full h-screen flex justify-center items-center">
       <ThreeDot variant="bounce" color="#32cd32" size="medium" text="" textColor="" />
       </div>
      ) : (
     // {/* Main Content */}
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
                      <button onClick={() => handleDeleteClick(item.id)} className='mt-1'>
                        <FaRegTrashAlt />
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
      )}
       {/* Delete Confirmation Modal */}
                                        
       {showModal && ( // show the confirmation modal if the showmodal is true
        <ConfirmationModal
          showModal={showModal}
          onConfirm={handleConfirmDeletion}
          onCancel={() => setShowModal(false)}
          itemName={selectedId}
        />
      )}
    </div>
  );
};

export default HistoryPage;
