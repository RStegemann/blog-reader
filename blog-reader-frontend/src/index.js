import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './components/app';
import {Connect, Disconnect} from './websocket';

// Define root element
const root = ReactDOM.createRoot(document.getElementById('root'));
// Declare blogPosts list
let blogPosts;

// Connect to websocket
Connect("ws://localhost:8080",
    onSocketOpened,
    OnSocketClosed,
    OnSocketMessageReceived,
    OnSocketError
);

/**
 * Handler for socket opening events, just logs the connection for now
 * @param event Event information, received from websocket
 */
function onSocketOpened(event){
    console.log("Connected.");
}

/**
 * Handler for socket closing events
 * @param event Event information, received from websocket
 */
function OnSocketClosed(event){
}

/**
 * Handler for socket errors
 * @param event Event information, received from websocket
 */
function OnSocketError(event){
    Disconnect();
}

/**
 * Handler for received socket messages
 * @param event Event information, received from websocket
 */
function OnSocketMessageReceived(event){
    try{ // Try parsing event data as json, in case of syntax error the message gets ignored
        const parsedPosts = JSON.parse(event.data);
        if(checkNewPosts(blogPosts, parsedPosts)){ // If the data contained new posts, redraw the website
            blogPosts = parsedPosts;
            root.render(
                <React.StrictMode>
                    <App blogPosts={blogPosts}/>
                </React.StrictMode>
            );
        }
    }catch (syntaxError){
        // Received incorrect json, ignoring result
    }
}

/**
 * Compare received posts with list of currently known posts
 * @param blogPosts Current posts
 * @param parsedPosts Newly received posts
 * @returns {boolean} True/False depending on whether the new Post List contained new data
 */
function checkNewPosts(blogPosts, parsedPosts){
    if(blogPosts === undefined || parsedPosts.length > blogPosts.length) return true;
    parsedPosts.forEach(parsedPost => {
        if (!(blogPosts.includes(parsedPost))) {
            return true;
        }
    });
    return false;
}
