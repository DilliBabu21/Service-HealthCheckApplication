// import React, { useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';

// const withAuth = (Component) => {
//   const AuthenticatedComponent = (props) => {
//     const navigate = useNavigate();

//     useEffect(() => {
//       const token = localStorage.getItem('token');

//       if (!token) {
//         navigate('/auth/Login');
//       } else {
//         axios
//           .get(`http://localhost:8878/healthcheck/services`, {
//             headers: {
//               Authorization: `Bearer ${token}`,
//             },
//           })
//           .then((res) => {
//             console.log(res.data);
//           })
//           .catch((error) => {
//             console.log(error);
//             navigate('/auth/Login');
//           });
//       }
//     }, [navigate]);

//     return <Component {...props} />;
//   };

//   return AuthenticatedComponent;
// };

// export default withAuth;
