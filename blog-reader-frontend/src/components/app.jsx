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
        selectedIndex: 0 // Currently selected blogpost index
    }

    /**
     * Eventhandler for the Buttonlist, allowing for selection of specific posts to show their details
     * @param index Index of the Element in blogpost list
     */
    handleSelection = (index) =>{
        const selectedIndex = index
        this.setState( // Update state to change selected blogpost to the newly selected one
            {selectedIndex}
        )
    }

    /**
     * @returns {JSX.Element} Either an empty div if no posts are registered yet or a React.Fragment containing the
     * buttonList, Details page and wordTable
     */
    getBlogPostList = () =>{
        if(this.props.blogPosts !== undefined && this.props.blogPosts.length > 0){
            return (
                <React.Fragment>
                    <div className="split left" data-testid='postList'>
                        <BlogPostList blogPosts={this.props.blogPosts} handleSelection={this.handleSelection}/>
                    </div>
                    <div className="split center" data-testid='postDetails'>
                        <BlogPostDetails blogPost={this.props.blogPosts[this.state.selectedIndex]}/>
                    </div>
                    <div className="split right" data-testid='wordCountTable'>
                        <WordTable wordCountArray={this.props.blogPosts[this.state.selectedIndex].wordCounts}/>
                    </div>
                </React.Fragment>
            );
        }else{
            return <div data-testid='emptyapp'></div>
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
