Connect();
function Connect()
{
    const exampleSocket = new WebSocket("ws://localhost:8080");
    exampleSocket.onopen = (event) =>{
        console.log("Connected.");
    }
    exampleSocket.onclose = (event) =>{
        console.log("Disconnected.");
    }
    exampleSocket.onmessage = (event) =>{
        const obj = JSON.parse(event.data);
        console.log(obj[0].wordCounts.toString())
        //console.log(`Message received: ${event.data}`);
    }
    waitForConnection(exampleSocket)
}

function waitForConnection(socket){
    setTimeout(function(){
        if(socket.readyState !== 1)
            waitForConnection(socket)
    }, 5);
}