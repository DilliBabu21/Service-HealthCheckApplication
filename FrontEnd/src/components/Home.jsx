import React, { useEffect, useState } from 'react'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import "../App.css";
import { Alert, Badge, Button } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EmailIcon from '@mui/icons-material/Email';
import NetworkCheckIcon from '@mui/icons-material/NetworkCheck';
import EditIcon from '@mui/icons-material/Edit';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import AddTaskIcon from '@mui/icons-material/AddTask';










export default function Home() {


    

    let navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/auth/Login');
        }
      }, [navigate]);
    


    const [services, setServices] = useState([]);

    
    const result = `http://localhost:8878/healthcheck`;
    const token = localStorage.getItem('token');
    const authAxios = axios.create({
        baseUrl : result,
        
        headers:{
            Authorization: `Bearer $(token)`,
        },

    })

    useEffect(() => {
        const token = localStorage.getItem('token');
        authAxios.get(`http://localhost:8878/healthcheck/services`,{
            headers: {
                Authorization: `Bearer ${token}`
              }
        })
            .then((res) => {
                setServices(res.data);
                
                
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    useEffect(() => {
        // setInterval(() => {
            loadServices();

        // }, 1000);
    }, []);

    const loadServices = async () => {
      const token = localStorage.getItem('token');
         const result = await axios.get(`http://localhost:8878/healthcheck/services`,{
             headers: {
                Authorization: `Bearer ${token}`
               
              }

              
        })
        
        setServices(result.data);
    };


    const deleteService = async (id) => {
        await axios.delete(`http://localhost:8878/healthcheck/service/${id}`)
        loadServices();
    };
    const sendEmail = async () => {
        await axios.post(`http://localhost:8878/healthcheck/service/send-email`)
        console.log("Email Sent");
        <Alert>Email Sent</Alert>
        loadServices();
    };
    const checkAllServices = async () => {
    
       await axios.get(`http://localhost:8878/healthcheck/service/checkall`)
       console.log("Checked All Services");
            // setInterval(() => {
                loadServices();
            //   }, 1000);
    };

    const checkService = async (id) => {
        try {
          
            const response = await axios.get(`http://localhost:8878/healthcheck/service/check/${id}`)
            console.log(response.data);

            loadServices();
        } catch (error) {
            console.error(error);
        }

    };

    var d = new Date();
    var ISTTime = new Date(d.getTime() + (330 * 60 * 1000));
    ISTTime.toLocaleString();
    {/* <div className="d-flex justify-content-end mt-3">
             
             {/* <Link className='btn' to='/addService'><Button  variant="contained" startIcon={<AddToPhotosIcon />} color='primary'>Add Service</Button> </Link> */}
    {/* <Button  className='btn'  variant="contained" startIcon={<AddToPhotosIcon />} color='primary' component={Link} to='/addService'>Add Service</Button> */ }
    {/* <Button variant="contained" startIcon={<EmailIcon />} color='primary' onClick={sendEmail}>Send Email</Button> */ }
    {/* </div>  */ }

    return (

        <div >
            <div className='container' style={{ display: 'flex', justifyContent: 'center'}}>




                <div className='py-4'>
                    
                    <div className="d-flex justify-content-end mb-3 ">
                    <div className='btn3'>
                        <Button style={{
        backgroundColor: "#1FA2FF",
        
    }}  variant="contained" startIcon={<AddTaskIcon />} className="mx-1"  onClick={() => navigate(`/addService`)}>Add Service</Button>
                       </div>
                        <div className='btn1'>
                    <Button style={{
                                                backgroundColor: "#e6b400",

                                            }}  variant="contained" startIcon={<CheckCircleIcon />} color='success' onClick={checkAllServices}>Check All</Button>
                       </div>
                       <div className='btn2'>
                        <Button variant="contained" startIcon={<EmailIcon />} color='primary' onClick={sendEmail}>Send Email</Button>
                       </div>
                      
                  
                    </div>
                  
                        

                    <table className="table table-responsive table-hover border shadow" style={{ width: '90%' }}>

                        <thead>
                            <tr>
                                <th scope='col' className="align-middle">Service Id</th>
                                <th scope="col" className="align-middle">Service Name</th>
                                <th scope="col" className="align-middle">Url</th>
                                <th scope="col" className="align-middle">Email</th>
                                <th scope="col" className="align-middle">HTTPMethod</th>
                                <th scope="col" className="align-middle">Status</th>
                                <th scope="col" className="align-middle">Checked Time</th>
                                <th scope='col' className="align-middle">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {services.map((service, index) => (
                                <tr>
                                    <th scope="row" className="align-middle">{index + 1}</th>
                                    <td className="align-middle">{service.service_name}</td>
                                    <td className="align-middle">{service.url}</td>
                                    <td className="align-middle">{service.email}</td>
                                    <td className="align-middle">{service.httpMethod}</td>
                                    <td className="align-middle">
                                        <span className={service.status === "UP" ? "badge bg-success" : service.status === "DOWN" ? "badge bg-danger" : service.status === "UNKNOWN" ? "badge bg-secondary" : "badge bg-warning"}>  {service.status}
                                        </span>

                                    </td>

                                    <td className="align-middle">{new Date(service.checkedAt).toLocaleString("en-IN")}</td>
                                    <td className="align-middle">
                                        <div className="d-flex justify-content-around">
                                            <Button variant="contained"
                                                className="btn btn-primary mx-1" startIcon={<NetworkCheckIcon />}
                                                onClick={() => checkService(service.id)}
                                            >
                                                Check
                                            </Button>
                                            {/* <Button className="contained" ><Link underline= to={`/editservice1/${service.id}`} >Edit</Link></Button> */}

                                            {/* <Link className="btn btn-dark mx-2" to={`/editservice1/${service.id}`} >Edit</Link> */}
                                            <Button style={{
                                                backgroundColor: "#21b6ae",

                                            }} variant="contained" className="mx-1" startIcon={<EditIcon />} onClick={() => navigate(`/editservice1/${service.id}`)}>Edit</Button>
                                            <Button style={{
                                                backgroundColor: "#DB2B39",

                                            }} variant="contained" className="btn btn-primary mx-1" startIcon={<DeleteIcon />} color="error" onClick={() => deleteService(service.id)}>
                                                Delete
                                            </Button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    {/* <div className="d-flex justify-content-center mt-3">
                    <button className="btn btn-primary" onClick={sendEmail}>Send Email</button>
                </div> */}
                </div>
            </div>

        </div>
    )

}    