const express = require('express'); //Line 1
const app = express(); //Line 2
const port = process.env.PORT || 5000; //Line 3
const Net = require('net');
const client = new Net.Socket();
// This displays message that the server running and listening to specified port
app.listen(port, () => console.log(`Listening on port ${port}`)); //Line 6

// create a GET route
app.get('/express_backend', (req, res) => { //Line 9
  res.send({ express: 'YOUR EXPRESS BACKEND IS CONNECTED TO REACT' }); //Line 10
}); //Line 11

// create a GET route
app.get('/storerequest', (req, res) => { //Line 9
    client.connect({ port: 8055, host: 'localhost' }, function() {
        // If there is no error, the server has accepted the request and created a new 
        // socket dedicated to us.
        console.log('TCP connection established with the server.');
    
        // The client can now send data to the server by writing to its socket.
        client.write('Hello, server.');
    });
    
    res.send({ express: 'YOUR EXPRESS BACKEND IS CONNECTED TO REACT' }); //Line 10
  }); //Line 11


