import '../App.css';
import BlogPostList from "./blogPostList";
import React, {Component} from "react";
import BlogPostDetails from "./blogPostDetails";
import WordTable from "./wordTable";

/**
 * Outermost Component of the Application
 */
export default class App extends Component{
    state={
        selected: this.props.blogPosts[0] // Currently selected blogpost
    }

    /**
     * Eventhandler for the Buttonlist, allowing for selection of specific posts to show their details
     * @param index Index of the Element in blogpost list
     */
    handleSelection = (index) =>{
        const selected = this.props.blogPosts[index]
        this.setState( // Update state to change selected blogpost to the newly selected one
            {selected}
        )
    }

    /**
     * @returns {JSX.Element} Either an empty div if no posts are registered yet or a React.Fragment containing the
     * buttonList, Details page and wordTable
     */
    getBlogPostList = () =>{
        if(this.props.blogPosts !== undefined){
            return (
                <React.Fragment>
                    <div className="split left">
                        <BlogPostList blogPosts={this.props.blogPosts} handleSelection={this.handleSelection}/>
                    </div>
                    <div className="split center">
                        <BlogPostDetails blogPost={this.state.selected}/>
                    </div>
                    <div className="split right">
                        <WordTable wordCountArray={this.state.selected.wordCounts}/>
                    </div>
                </React.Fragment>
            );
        }else{
            return <div></div>
        }
    }

    /**
     * Renders this component
     * @returns {JSX.Element}
     */
    render(){
        return(this.getBlogPostList());
    }
}
