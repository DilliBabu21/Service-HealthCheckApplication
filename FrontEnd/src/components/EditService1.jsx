import { Alert, Button, MenuItem, Select } from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import CancelIcon from '@mui/icons-material/Cancel';
import QueueIcon from '@mui/icons-material/Queue';





export default function EditService1() {

  let navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/auth/Login');
        }
      }, [navigate]);
    

    const httpMethods = ["GET", "POST", "PUT", "DELETE"];

    const {id} = useParams();


    const [services, setServices] = useState({
        service_name: "",
        url: "",
        email: "",
        httpMethod: "",
        payload: ""
    });

    const { service_name, url, email, httpMethod, payload } = services;

    useEffect(() => {
        loadServices();
    }, []);

    const onInputChange = (e) => {
        setServices({ ...services, [e.target.name]: e.target.value });
    };

   

    const loadServices = async () => {
        const result = await axios.get(`http://localhost:8878/healthcheck/service/${id}`);
        console.log(result.data);
        setServices(result.data);
      };

    const onSubmit = async (e) => {
        e.preventDefault();
        await axios.put(`http://localhost:8878/healthcheck/service/${id}`, services);
    navigate("/home");
  };



  return (
    <div className="container">
      <div className="row">
        <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
          <h2 className="text-center m-4">Edit Service</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className="mb-3">
              <label htmlFor="service_name" className="form-label">
                Name
              </label>
              <input
                type={"text"}
                className="form-control rounded-pill"
                placeholder="Enter Service name"
                name="service_name"
                value={service_name}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="url " className="form-label">
                Url
              </label>
              <input
                type={"text"}
                className="form-control rounded-pill"
                placeholder="Enter Url"
                name="url"
                value={url}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="email" className="form-label">
                Email
              </label>
              <input
                type={"text"}
                className="form-control rounded-pill"
                placeholder="Email"
                name="email"
                value={email}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-1'>
                            <label htmlFor='httpMethod' className='form-label'>
                                HTTP Method
                            </label>
                            <Select
                                value={httpMethod}
                                onChange={(e) => onInputChange(e)}
                                className='form-control rounded-pill' style={{ height: '45px' }}
                             name='httpMethod'>
                                {httpMethods.map((method, index) => (
                                    <MenuItem key={index} value={method}>
                                        {method}
                                    </MenuItem>
                                ))}
                            </Select>
                        </div>
                        {/* This will create a dropdown with HTTP methods as options. You can also modify the styles of the Select component as per your requirement. */}





                        <div className='mb-3'>
                            <label htmlFor='payload' className='form-label'>
                                Payload
                            </label>
                            <input type={"textarea"} className='form-control rounded-pill' value={payload} onChange={(e) => onInputChange(e)} placeholder='Eg "{\"Key\": \"Value\", \"key\": \"value\"}"' name='payload' />
                        </div>
            <div className="text-center">
            {/* <button type="submit" className="btn btn-primary rounded-pill">
              Submit
            </button>
            <Link className="btn btn-danger rounded-pill mx-2" to="/">
              Cancel
            </Link> */}
            <Button style={{
                                                backgroundColor: "#0072ff",

                                            }} variant="contained" type="submit" startIcon={<QueueIcon />} onclick={(e) => onSubmit(e)}>
                                
                                Submit 
                            </Button>

                            <Button style={{
                                                backgroundColor: "#FF4E50",

                                            }} variant="contained" className="btn btn-secondary mx-2" startIcon={<CancelIcon />} color="error" onClick={() => navigate("/home")}>
                                Cancel
                            </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}