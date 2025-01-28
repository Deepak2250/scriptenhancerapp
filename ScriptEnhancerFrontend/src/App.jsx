import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Home from './assets/components/Home';
import SignUp from './assets/components/SignUp';
import LoginForm from './assets/components/LoginForm';

const router = createBrowserRouter([
  { path: "/", element: <Home /> },
  { path: "/signup", element: <SignUp /> },
  {path: "/login" , element: <LoginForm/>}
]);

function App() {
  return (
    <RouterProvider router={router} />
  );
}

export default App;
