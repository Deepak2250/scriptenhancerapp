import axios from "axios";

const Axios = axios.create({
  baseURL: "http://localhost:8080", // Replace with your backend base URL
  headers: {
    "Content-Type": "application/json",
  },
});

export default Axios;
