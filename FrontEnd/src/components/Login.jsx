import { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from 'react-router-dom';

function Login() {

    


  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  let navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await axios.post("http://localhost:8899/api/v1/auth/authenticate", {
        username,
        password,
      });

      // Save the token to local storage
      localStorage.setItem("token", response.data.token);
      // console.log(response.data.token)
      
      navigate("/home");
    } catch (error) {
      setErrorMessage("Invalid username or password");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1>Login</h1>
      {errorMessage && <p>{errorMessage}</p>}
      <label>
        Username:
        <input
          type="text"
          value={username}
          onChange={(event) => setUsername(event.target.value)}
        />
      </label>
      <label>
        Password:
        <input
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
        />
      </label>
      <button type="submit">Login</button>
    </form>
  );
}

export default Login;
