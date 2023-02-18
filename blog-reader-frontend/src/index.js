import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import Connect from './websocket';

const root = ReactDOM.createRoot(document.getElementById('root'));
let blogPosts;

Connect("ws://localhost:8080",
    onSocketOpened,
    OnSocketClosed,
    OnSocketMessageReceived
);

function onSocketOpened(event){
    console.log("Connected.");
}

function OnSocketClosed(event){
    console.log("Disconnected");
}
function OnSocketMessageReceived(event){
    const parsedPosts = JSON.parse(event.data);
    if(newPosts(blogPosts, parsedPosts)){
        console.log("Rendering.");
        blogPosts = parsedPosts;
        root.render(
            <React.StrictMode>
                <App blogPosts={blogPosts}/>
            </React.StrictMode>
        );
    }
}

function newPosts(blogPosts, parsedPosts){
    if(blogPosts === undefined || parsedPosts.length > blogPosts.length) return true;
    parsedPosts.forEach(parsedPost => {
        if (!(blogPosts.includes(parsedPost))) {
            return true;
        }
    });
    return false;
}


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
