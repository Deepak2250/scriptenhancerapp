import React from 'react';

const ConfirmationModal = ({ showModal, onConfirm, onCancel, itemName }) => {
  if (!showModal) return null; // Don't render the modal if it's not visible

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white p-6 rounded-md shadow-lg w-80">
        <h3 className="text-xl font-semibold mb-4">Are you sure?</h3>
        <p className="text-gray-700 mb-6">
          Do you really want to delete <strong>{itemName}</strong>? This action cannot be undone.
        </p>
        <div className="flex justify-between">
          <button
            onClick={onConfirm}
            className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600"
          >
            Confirm
          </button>
          <button
            onClick={onCancel}
            className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmationModal;
