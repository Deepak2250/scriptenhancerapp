import React from 'react'
import { useForm } from 'react-hook-form'
const SignUp = () => {

    const { register, handleSubmit, formState: { errors }, } = useForm();

    const onSubmit = (data) => {
        console.log('Form Data:', data);
      };
    
      return (
        <div className="flex items-center justify-center min-h-screen bg-gray-900 text-white">
          <form
            onSubmit={handleSubmit(onSubmit)}
            className="bg-gray-800 p-6 rounded-lg shadow-xl w-96"
          >
            <h1 className="text-2xl font-bold mb-4">React Form</h1>
    
            {/* Email Field */}
            <div className="mb-4">
              <label htmlFor="email" className="block mb-1">
                Email
              </label>
              <input
                type="email"
                id="email"
                {...register('email', {
                  required: 'Email is required',
                  pattern: {
                    value: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,
                    message: 'Invalid email address',
                  },
                })}
                className={`w-full p-2 border ${
                  errors.email ? 'border-red-500' : 'border-gray-700'
                } rounded bg-gray-700`}
                placeholder="Enter your email"
              />
              {errors.email && (
                <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>
              )}
            </div>
    
            {/* Password Field */}
            <div className="mb-4">
              <label htmlFor="password" className="block mb-1">
                Password
              </label>
              <input
                type="password"
                id="password"
                {...register('password', {
                  required: 'Password is required',
                  minLength: {
                    value: 6,
                    message: 'Password must be at least 6 characters long',
                  },
                })}
                className={`w-full p-2 border ${
                  errors.password ? 'border-red-500' : 'border-gray-700'
                } rounded bg-gray-700`}
                placeholder="Enter your password"
              />
              {errors.password && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.password.message}
                </p>
              )}
            </div>
    
            {/* Submit Button */}
            <button
              type="submit"
              className="w-full bg-blue-600 hover:bg-blue-700 text-white p-2 rounded mt-4"
            >
              Submit
            </button>
          </form>
        </div>
      );
    };

export default SignUp