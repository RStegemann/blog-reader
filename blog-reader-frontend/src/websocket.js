export default function Connect(address, openHandler, closeHandler, messageHandler)
{
    const exampleSocket = new WebSocket(address);
    exampleSocket.onopen = (event) =>{
        openHandler(event);
    }
    exampleSocket.onclose = (event) =>{
        closeHandler(event);
    }
    exampleSocket.onmessage = (event) =>{
        messageHandler(event);
    }
    waitForConnection(exampleSocket)
}

function waitForConnection(socket){
    setTimeout(function(){
        if(socket.readyState !== 1)
            waitForConnection(socket)
    }, 5);
}