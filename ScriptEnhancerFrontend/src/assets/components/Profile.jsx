import React, { useEffect, useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { ThreeDot } from "react-loading-indicators";
import ConfirmationModal from "./ConfirmationModal";

const Profile = ({ checkTokenExpiry, tokenDecoded, token }) => {
  // Assuming you have a function to get the user email from localStorage/sessionStorage\

  const [isImageAvailable, setImageAvailable] = useState(false);
  const [url, setUrl] = useState(null);
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef(null); //Making a useRef to connect the file opening and selecting
  const [confirmation, setConfirmation] = useState(false);
  const [ogUrl, setOgUrl] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [deletes , setDeletes] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [selectedImage, setSelectedImage] = useState(null); // making the state for the email
  const deletionModal = useRef(null);

  const navigate = useNavigate();
  
  useEffect(() => {
    checkTokenExpiry();
    fetchProfileImage();
  }, []);

  const fetchProfileImage = async () => {
    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/user/getprofile`,
        {
          headers: {
            Authorization: `Bearer ${token}}`,
          },
        }
      );

      if (response.ok) {
        setLoading(false);
        const imageUrl = await response.blob();
        console.log("The image blob is here : " + imageUrl);

        setImageAvailable(true);
        setDeletes(true);
        let extractedUrl = URL.createObjectURL(imageUrl);
        console.log("The extractedUrl url is here : " + extractedUrl);
        setOgUrl(extractedUrl);
        setUrl(extractedUrl);
      }
    } catch (error) {
      setDeletes(false);
      setLoading(false);
      setOgUrl(null);
      console.log("There is no image : " + error);
      setImageAvailable(false);
      console.error("Error fetching profile image:", error);
    } finally {
      setLoading(false);
    }
  };

  // When the user clicks "Upload Image" or "Update Image"
  const handleButtonClick = () => {
    fileInputRef.current.click(); // This opens the file selection dialog
  };

  const handleFileChange = async (event) => {
    const file = event.target.files && event.target.files[0];
    if (file) {
      // Preview the selected image immediately
      const preview = URL.createObjectURL(file);
      setUrl(preview);
      setImageAvailable(true);
      setDeletes(false)
      setConfirmation(true);
      setSelectedFile(file);
    } else {
      setDeletes(true);
      setConfirmation(false);
      ogUrl ? setUrl(ogUrl) : setImageAvailable(false);
    }
  };

  const handleConfirmationClick = async () => {
    console.log("The selected file is : " ,selectedFile);
    
    if (selectedFile) {
      const formData = new FormData();
      formData.append("multipartFile", selectedFile); // Append the file object

      try {
        const response = await axios.post(
          "http://localhost:8080/api/user/uploadprofile",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data", // Let axios set the correct boundary
              Authorization: `Bearer ${token}`,
            },
          }
        );
       setDeletes(true);
        setConfirmation(false);
        setImageAvailable(true);
        setOgUrl(URL.createObjectURL(selectedFile));
        setUrl(URL.createObjectURL(selectedFile));
        console.log("Image uploaded successfully", response.data);
        // Optionally, update your UI or state here
      } catch (error) {
        setDeletes(false);
        console.error("Error uploading image:", error);
        // Handle error appropriately (e.g., notify the user)
      }
    } else {
      alert("choose the image first");
    }
  };

  const handleCancelClick = () => {
    setConfirmation(false);
    ogUrl ? setUrl(ogUrl) : setImageAvailable(false);
  };

  const handleDeleteClick = ()=>{
   deletionModal.current = setShowModal(true);
   setSelectedImage(ogUrl);
}

const handleConfirmDeletion = async() =>{
  setShowModal(false);
  try {
     await axios.delete(
      "http://localhost:8080/api/user/deleteimage",
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    setUrl(null);
    setOgUrl(null);
    setImageAvailable(false);
}

catch(error){
console.log(error);

}
}

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex justify-center items-center">
      {/* Profile Card Container */}
      <div className="bg-white p-8 rounded-lg shadow-xl w-full sm:w-96">
        <div className="text-center">
          {/* Profile Picture (Optional) */}

          <input
            id="fileInput"
            type="file"
            accept="image/*"
            className="hidden"
            ref={fileInputRef} //The useRef is referring here
            onChange={handleFileChange} //It takes a event like selecting files
          />

          {loading ? (
            // When loading is true, show a loading indicator
            <div className="w-full min-h-40 flex justify-center items-center mb-5">
              <ThreeDot
                variant="bounce"
                color="black"
                size="small"
                text=""
                textColor=""
              />
            </div>
          ) : !isImageAvailable ? (
            // When not loading and no image is available
            <div className="w-full min-h-40 max-h-40 flex flex-col justify-center items-center gap-7 mb-5">
              <div className="text-xl font-semibold text-red-600">No Image</div>

              <button
                onClick={handleButtonClick}
                className="bg-blue-500 min-w-30 max-w-30 text-white py-2 px-6 rounded-full text-sm hover:bg-blue-600 transition duration-200"
              >
                Upload Image
              </button>
            </div>
          ) : (
           // When image is available
            <div className="w-full min-h-40 max-h-40 flex flex-col justify-center items-center gap-7 mb-5">
              <img src={url} alt="Profile" className="w-32 h-32 " />
              <div className="flex flex-row justify-evenly items-center gap-2">
                {confirmation && (
                  <button
                    onClick={handleConfirmationClick}
                    className="bg-blue-500 min-w-28 max-w-28 text-white py-2 px-2  rounded-full text-sm hover:bg-blue-600 transition duration-200"
                  >
                    Confirm
                  </button>
                )}
                <button
                  onClick={handleButtonClick}
                  className="bg-blue-500 min-w-28 max-w-28 text-white py-2 px-2  rounded-full text-sm hover:bg-blue-600 transition duration-200"
                >
                  Update Image
                </button>
                {deletes && (
                  <button
                  onClick={handleDeleteClick}
                  ref={deletionModal}
                  className="bg-blue-500 min-w-28 max-w-28 text-white py-2 px-2 rounded-full text-sm hover:bg-blue-600 transition duration-200"
                >
                  Delete
                </button>
                )}
                {/* When this button got clicked then the useRef will go to the fileInput.current and then click in the filefolders to select a file */}
                {confirmation && (
                  <button
                    onClick={handleCancelClick}
                    className="bg-blue-500 min-w-28 max-w-28 text-white py-2 px-2 rounded-full text-sm hover:bg-blue-600 transition duration-200"
                  >
                    Cancel
                  </button>
                )}
              </div>
            </div>
          )}

          {/* Username */}
          <h2 className="text-2xl font-semibold text-gray-800 mb-2">
            User Profile
          </h2>
          <p className="text-sm text-gray-600 mb-6">
            Welcome to your profile page
          </p>
        </div>

        {/* Profile Details */}
        <div className="space-y-4">
          {/* Email */}
          <div className="flex justify-between items-center">
            <span className="text-gray-600 font-medium">Email:</span>
            <span className="text-gray-800">{tokenDecoded.sub}</span>
          </div>
        </div>

        {/* Logout Button */}
        <div className="mt-6 flex justify-center gap-3">
          <button
            className="bg-blue-500 text-white py-2 px-6 rounded-full text-sm hover:bg-blue-600 transition duration-200"
            onClick={() => {
              localStorage.removeItem("jwttoken");
              sessionStorage.removeItem("jwttoken");
              localStorage.removeItem("exp");
              sessionStorage.removeItem("exp");

              window.location.href = "/"; // Redirect to login
            }}
          >
            Log Out
          </button>

          <button
            className="bg-blue-500 text-white py-2 px-6 rounded-full text-sm hover:bg-blue-600 transition duration-200"
            onClick={() => {
              navigate("/history");
            }}
          >
            History
          </button>
        </div>
        
      </div>
      {showModal && ( // show the confirmation modal if the showmodal is true
        <ConfirmationModal
          showModal={showModal}
          onConfirm={handleConfirmDeletion}
          onCancel={() => setShowModal(false)}
          itemName={selectedImage}
        />
      )}
    </div>
    
  );
};

export default Profile;
