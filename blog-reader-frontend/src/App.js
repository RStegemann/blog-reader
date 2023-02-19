import logo from './logo.svg';
import './App.css';
import BlogPostList from "./components/blogPostList";
import {Component} from "react";
import BlogPostDetails from "./components/blogPostDetails";
import WordTable from "./components/wordTable";

export default class App extends Component{
    state={
        selected: this.props.blogPosts[0]
    }

    handleSelection = (index) =>{
        const selected = this.props.blogPosts[index]
        this.setState(
            {selected}
        )
    }

    getBlogPostList = () =>{
        if(this.props.blogPosts !== undefined){
            return (
                <div>
                    <div className="split left">
                        <BlogPostList blogPosts={this.props.blogPosts} handleSelection={this.handleSelection}/>
                    </div>
                    <div className="split center">
                        <BlogPostDetails blogPost={this.state.selected}/>
                    </div>
                    <div className="split right">
                        <WordTable wordCountArray={this.state.selected.wordCounts}/>
                    </div>
                </div>
            );
        }else{
            return <span></span>
        }
    }

    render(){
        return(this.getBlogPostList());
    }
}
