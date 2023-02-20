/**
 * Connect to a websocket at a given address and link event handlers
 * @param address Websocket address ex.: ws://localhost:8080
 * @param openHandler Function handling opening events
 * @param closeHandler Function handling closing events
 * @param messageHandler Function handling received messages
 * @param errorHandler Function handling received messages
 */
export default function Connect(address, openHandler, closeHandler, messageHandler, errorHandler)
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
    exampleSocket.onerror = (event) =>{
        errorHandler(event)
    }
}