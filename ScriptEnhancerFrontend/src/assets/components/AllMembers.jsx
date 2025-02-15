import axios from "axios";
import { jwtDecode } from "jwt-decode";
import React, { useEffect, useState } from "react";
import { FaRegTrashAlt } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import ConfirmationModal from "./ConfirmationModal";
import { ThreeDot } from "react-loading-indicators";

// Function to render the user table
const AllMembers = ({ token, isAdmin, tokenDecoded}) => {
  /// taking token , decodedtoken and isAdmin from the parent app page

  const navigate = useNavigate();
  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedEmail, setSelectedEmail] = useState(null); // making the state for the email
   const [loading, setLoading] = useState(false); // New loading state

  // Open delete confirmation modal
  const handleDeleteClick = (email) => { //handling the delete taking the email from the onclick of dustbin button and setting the email in the state of email
                                         //and setting the modal to true for showing the confirmation modal
    setSelectedEmail(email);
    setShowModal(true);
  };

  const handleConfirmDeletion = async () => {
    // handline the submit for deletion
    setShowModal(false);        // if yes then false the modal to show and delete the user but also checks that if the email is null then return it 
    if (!selectedEmail) return;
    console.log(selectedEmail);
    
    try {
      if (isAdmin && tokenDecoded.sub == selectedEmail) {
        alert(
          "You cannot delete yourself from here ! Go to your profile then delete it "
        );
        return; // return if this happens
      }
    
      await axios.delete(
        `http://localhost:8080/api/admin/deleteuser?email=${selectedEmail}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setData((prv) => prv.filter((e) => e.email !== selectedEmail));
    } catch (error) {
      console.log(error);
      alert(error);
    }
  };

  //Render the funtion for the first time of the render
  useEffect(() => {
    const dataResponse = async () => {
      setLoading(true)
      try {
        const response = await axios.get(
          "http://localhost:8080/api/admin/getallusers", // fetching api getallusers
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setLoading(false)
        setData(response.data);
      } catch (error) {
        console.log(error);
      }
      finally{
        setLoading(false)
      }
    };

    // Check the token expiry
    // checkTokenExpiry();
    dataResponse();
  }, []);

  return (
    <div className="container mx-auto p-8 flex flex-col justify-center items-center">
      <div>
        <h1 className="text-3xl text-white font-bold text-center mb-6">
          All Members
        </h1>
      </div>
     
      {loading ? (
        <div className="w-full h-screen flex justify-center items-center">
       <ThreeDot variant="bounce" color="#32cd32" size="medium" text="" textColor="" />
       </div>
      ) : (

      <div className="grid grid-row-[auto] gap-12 w-full h-auto">
        {data.map((user, index) => (
          <div
            key={index}
            className="flex flex-row justify-around items-center w-full min-h-20 max-h-20 bg-[#ffffff]"
          >
            <h2 className="text-black  text-xl font-medium ">{user.email}</h2>
            <p className="text-black  text-xl font-medium">
              {user.roles.map((role) => role.role).join(" , ")}
            </p>
            <p className="text-black text-xl font-medium">
              Total Input/Output: {user.inputAndOutputs.length}
            </p>
            <button
              onClick={() => handleDeleteClick(user.email)}
              className="text-red-600"
            >
              <FaRegTrashAlt />
            </button>
          </div>
        ))}
      </div>
 ) }

      {/* Delete Confirmation Modal */}
                                        
      {showModal && ( // show the confirmation modal if the showmodal is true
        <ConfirmationModal
          showModal={showModal}
          onConfirm={handleConfirmDeletion}
          onCancel={() => setShowModal(false)}
          itemName={selectedEmail}
        />
      )}
    </div>

    
  );
};
export default AllMembers;
