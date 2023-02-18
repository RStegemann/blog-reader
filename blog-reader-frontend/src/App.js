import logo from './logo.svg';
import './App.css';
import BlogPostList from "./components/blogPostList";
import {Component} from "react";

export default class App extends Component{

  getBlogPostList = () =>{
    if(this.props.blogPosts !== undefined){
      return <BlogPostList blogPosts={this.props.blogPosts}/>
    }else{
      return <span></span>
    }
  }

  render(){
    return(this.getBlogPostList());
  }
}
