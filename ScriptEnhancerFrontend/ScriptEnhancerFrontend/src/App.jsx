import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Home from './assets/components/home';
import SignUp from './assets/components/SignUp';

const router = createBrowserRouter([
  { path: "/", element: <Home /> },
  { path: "/signup", element: <SignUp /> }
]);

function App() {
  return (
    <RouterProvider router={router} />
  );
}

export default App;
