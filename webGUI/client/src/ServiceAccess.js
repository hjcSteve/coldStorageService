import React, { Component ,useState} from 'react';
function ServiceAccess() {
    const [ response, setResponse] = useState('');
    const storerequest = async () => {
        const response = await fetch('/storerequest');
        const body = await response.json();
        
        if (response.status !== 200) {
          throw Error(body.message) 
        }
        setResponse(body.message)
        return body;
    };

    
    

    return(
        <button onClick={ storerequest} >store request </button>   

        
    )
    
}

export default ServiceAccess;